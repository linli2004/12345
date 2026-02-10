package top.tangyh.lamp.sop.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSON;
import com.gitee.httphelper.HttpHelper;
import com.gitee.httphelper.result.ResponseResult;
import com.gitee.sop.support.exception.SignException;
import com.gitee.sop.support.util.SignUtil;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.R;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.database.mybatis.conditions.query.LbQueryWrap;
import top.tangyh.basic.exception.BizException;
import top.tangyh.basic.utils.ArgumentAssert;
import top.tangyh.lamp.sop.dto.NotifyRequest;
import top.tangyh.lamp.sop.entity.SopIsvInfo;
import top.tangyh.lamp.sop.entity.SopNotifyInfo;
import top.tangyh.lamp.sop.enums.NotifyStatusEnum;
import top.tangyh.lamp.sop.manager.SopIsvInfoManager;
import top.tangyh.lamp.sop.manager.SopNotifyInfoManager;
import top.tangyh.lamp.sop.properties.NotifyProperties;
import top.tangyh.lamp.sop.service.NotifyService;
import top.tangyh.lamp.sop.service.bo.NotifyBO;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author tangyh
 * @since 2025/12/17 15:40
 */
@Service
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(NotifyProperties.class)
public class NotifyServiceImpl implements NotifyService {
    private final SopNotifyInfoManager sopNotifyInfoManager;
    private final SopIsvInfoManager sopIsvInfoManager;
    private final NotifyProperties notifyProperties;

    @Override
    public R<Long> notify(NotifyRequest request) {
        NotifyBO notifyBO = new NotifyBO();
        BeanUtils.copyProperties(request, notifyBO);
        try {
            SopNotifyInfo notifyInfo = buildRecord(notifyBO);
            return R.success(doNotify(notifyBO, notifyInfo));
        } catch (SignException e) {
            log.error("回调异常，服务端签名失败, request={}", request, e);
            return R.fail(e.getMessage());
        }
    }

    @Override
    public Long notifyImmediately(Long notifyId) {
        SopNotifyInfo notifyInfo = sopNotifyInfoManager.getById(notifyId);
        String content = notifyInfo.getContent();
        NotifyBO notifyBO = JSON.parseObject(content, NotifyBO.class);
        // 发送请求
        try {
            return doNotify(notifyBO, notifyInfo);
        } catch (SignException e) {
            log.error("回调异常，服务端签名失败, notifyId={}", notifyId, e);
            throw new BizException("回调失败，签名错误");
        }
    }

    @Override
    public void retry(LocalDateTime now) {
        ArgumentAssert.notNull(now, "当前时间不能为空");
        LocalDateTime nextTime = now.withSecond(0).withNano(0);
        LbQueryWrap<SopNotifyInfo> wrap = Wraps.<SopNotifyInfo>lbQ().eq(SopNotifyInfo::getNextSendTime, nextTime)
                .eq(SopNotifyInfo::getNotifyStatus, NotifyStatusEnum.SEND_FAIL.getValue());
        List<SopNotifyInfo> tasks = sopNotifyInfoManager.list(wrap);

        if (CollUtil.isEmpty(tasks)) {
            log.info("[notify]无重试记录");
            return;
        }


        MultiThreadTaskProcessor processor = new MultiThreadTaskProcessor();
        // 准备数据：创建N*M个任务列表
        List<List<MultiThreadTaskProcessor.Task>> allTasks = new ArrayList<>();


        // 每个线程处理 threadTasks 个任务
        List<List<SopNotifyInfo>> partition = Lists.partition(tasks, notifyProperties.getThreadTasks());

        int totalLists = partition.size(); // 总共x个list
        int tasksPerList = notifyProperties.getThreadTasks(); // 每个list有x个任务
        int numberOfThreads = notifyProperties.getThreads(); // 使用x个线程
        log.info("总共{}个list, 每个list有{}个任务, 使用{}个线程", totalLists, tasksPerList, numberOfThreads);

        // 初始化任务数据
        for (List<SopNotifyInfo> subTasks : partition) {
            List<MultiThreadTaskProcessor.Task> list = new ArrayList<>();
            for (SopNotifyInfo subTask : subTasks) {
                list.add(() -> {
                    retry(subTask);
                });
            }
            allTasks.add(list);
        }

        // 执行多线程处理
        processor.processTasks(allTasks, numberOfThreads);
    }

    private void retry(SopNotifyInfo notifyInfo) {
        String content = notifyInfo.getContent();
        NotifyBO notifyBO = JSON.parseObject(content, NotifyBO.class);
        try {
            log.info("[notify]开始重试, notifyId={}", notifyInfo.getId());
            if (Objects.equals(notifyInfo.getNotifyStatus(), NotifyStatusEnum.RETRY_OVER.getValue())) {
                log.warn("重试次数已用尽, notifyId={}", notifyInfo.getId());
                return;
            }
            // 发送请求
            doNotify(notifyBO, notifyInfo);
        } catch (SignException e) {
            log.error("[notify]重试签名错误，notifyId={}", notifyInfo.getId(), e);
            throw new RuntimeException("重试失败，签名错误");
        }
    }

    private Long doNotify(NotifyBO notifyBO, SopNotifyInfo notifyInfo) throws SignException {
        notifyInfo.setSendCnt(notifyInfo.getSendCnt() + 1);
        notifyInfo.setLastSendTime(LocalDateTime.now());
        notifyInfo.setNotifyUrl(buildNotifyUrl(notifyBO, notifyInfo));

        String notifyUrl = notifyInfo.getNotifyUrl();
        // 构建请求参数
        Map<String, String> params = buildParams(notifyBO);
        try {
            if (StringUtils.isBlank(notifyUrl)) {
                throw new RuntimeException("回调接口不能为空");
            }

            String json = JSON.toJSONString(params);
            log.info("发送回调请求，notifyUrl={}, content={}", notifyUrl, json);
            ResponseResult responseResult = HttpHelper.postJson(notifyUrl, json).execute();

            // 这里判断收到200认为请求成功
            int status = responseResult.getStatus();
            String resultContent = responseResult.asString();
            notifyInfo.setResultContent(resultContent);
            if (status == HttpStatus.SC_OK) {
                // 更新状态
                notifyInfo.setNotifyStatus(NotifyStatusEnum.SEND_SUCCESS.getValue());
                notifyInfo.setErrorMsg("");
            } else {
                // 回调失败
                log.error("回调状态非200:{}, result={}", status, resultContent);
                throw new RuntimeException(resultContent);
            }
        } catch (Exception e) {
            log.error("回调请求失败, notifyUrl={}, params={}, notifyBO={}", notifyUrl, params, notifyBO, e);
            notifyInfo.setNotifyStatus(NotifyStatusEnum.SEND_FAIL.getValue());
            notifyInfo.setErrorMsg(e.getMessage());

            LocalDateTime nextSendTime = buildNextSendTime(notifyInfo.getSendCnt());
            notifyInfo.setNextSendTime(nextSendTime);

            if (nextSendTime == null) {
                log.error("回调请求次数达到上线, notifyUrl={}, params={}", notifyUrl, params);
                notifyInfo.setNotifyStatus(NotifyStatusEnum.RETRY_OVER.getValue());
            }
        }

        sopNotifyInfoManager.saveOrUpdate(notifyInfo);

        return notifyInfo.getId();
    }


    private Map<String, String> buildParams(NotifyBO notifyBO) throws SignException {
        // 公共请求参数
        Map<String, String> params = new HashMap<>();
        String appId = notifyBO.getAppId();
        params.put("app_id", appId);
        params.put("method", notifyBO.getApiName());
        params.put("format", "json");
        params.put("charset", notifyBO.getCharset());
        params.put("sign_type", "RSA2");
        params.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        params.put("version", notifyBO.getVersion());

        // 业务参数
        Map<String, Object> bizContent = notifyBO.getBizParams();

        params.put("biz_content", JSON.toJSONString(bizContent));
        String content = SignUtil.getSignContent(params);

        String privateKey = sopIsvInfoManager.getPrivatePlatformKey(appId);
        String sign = SignUtil.rsa256Sign(content, privateKey, notifyBO.getCharset());
        params.put("sign", sign);

        return params;
    }

    private String buildNotifyUrl(NotifyBO notifyBO, SopNotifyInfo notifyInfo) {
        String savedUrl = notifyInfo.getNotifyUrl();
        if (StringUtils.isNotBlank(savedUrl)) {
            return savedUrl;
        }
        String notifyUrl = notifyBO.getNotifyUrl();
        if (StringUtils.isBlank(notifyUrl)) {
            SopIsvInfo sopIsvInfo = sopIsvInfoManager.getIsvByAppId(notifyBO.getAppId());
            notifyUrl = sopIsvInfo != null ? sopIsvInfo.getNotifyUrl() : null;
        }
        return notifyUrl;
    }

    private SopNotifyInfo buildRecord(NotifyBO notifyBO) {
        SopNotifyInfo notifyInfo = new SopNotifyInfo();
        notifyInfo.setAppId(notifyBO.getAppId());
        notifyInfo.setApiName(notifyBO.getApiName());
        notifyInfo.setApiVersion(notifyBO.getVersion());
        notifyInfo.setSendCnt(0);
        notifyInfo.setContent(JSON.toJSONString(notifyBO));
        notifyInfo.setNotifyStatus(NotifyStatusEnum.WAIT_SEND.getCode());
        notifyInfo.setErrorMsg("");
        notifyInfo.setRemark(notifyBO.getRemark());
        notifyInfo.setCreatedTime(LocalDateTime.now());
        notifyInfo.setUpdatedTime(LocalDateTime.now());
        notifyInfo.setCreatedBy(0L);
        notifyInfo.setUpdatedBy(0L);

        return notifyInfo;
    }

    /**
     * 构建下一次重试时间
     *
     * @param currentSendCnt 当前发送次数
     * @return 返回null表示重试次数用完
     */
    private LocalDateTime buildNextSendTime(Integer currentSendCnt) {
        String[] split = notifyProperties.getTimeLevel().split(",");
        if (currentSendCnt >= split.length) {
            return null;
        }
        // 1m
        String exp = split[currentSendCnt - 1];
        // 秒,毫秒归零
        LocalDateTime time = LocalDateTime.now().withSecond(0).withNano(0);
        // 最后一个字符，如：m,h,d
        char ch = exp.charAt(exp.length() - 1);
        int value = NumberUtils.toInt(exp.substring(0, exp.length() - 1));
        switch (String.valueOf(ch).toLowerCase()) {
            case "m":
                return time.plusMinutes(value);
            case "h":
                return time.plusHours(value);
            case "d":
                return time.plusDays(value);
            default:
                return null;
        }
    }

}
