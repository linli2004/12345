package top.tangyh.lamp.sop.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.base.R;
import top.tangyh.basic.base.controller.SuperController;
import top.tangyh.basic.base.request.PageParams;
import top.tangyh.basic.context.ContextUtil;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.database.mybatis.conditions.query.LbQueryWrap;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.basic.utils.BeanPlusUtil;
import top.tangyh.lamp.model.vo.save.AudioVO;
import top.tangyh.lamp.model.vo.save.StatusUpdateVO;
import top.tangyh.lamp.sop.entity.SopIsvInfo;
import top.tangyh.lamp.sop.service.SopIsvInfoService;
import top.tangyh.lamp.sop.service.SopIsvKeysService;
import top.tangyh.lamp.sop.utils.RSATool;
import top.tangyh.lamp.sop.vo.query.SopIsvInfoPageQuery;
import top.tangyh.lamp.sop.vo.result.SopIsvInfoResultVO;
import top.tangyh.lamp.sop.vo.result.SopIsvKeysResultVO;
import top.tangyh.lamp.sop.vo.save.IsvKeysGenVO;
import top.tangyh.lamp.sop.vo.save.SopIsvInfoApplyForVO;
import top.tangyh.lamp.sop.vo.save.SopIsvInfoSaveVO;
import top.tangyh.lamp.sop.vo.save.SopIsvInfoSubmitVO;
import top.tangyh.lamp.sop.vo.update.SopIsvInfoOpenUpdateVO;
import top.tangyh.lamp.sop.vo.update.SopIsvInfoUpdateVO;
import top.tangyh.lamp.sop.vo.update.SopIsvKeysUpdateVO;

/**
 * <p>
 * 前端控制器
 * isv信息表
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:47
 *
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/sopIsvInfo")
@Tag(name = "isv信息表")
public class SopIsvInfoController extends SuperController<SopIsvInfoService, Long, SopIsvInfo, SopIsvInfoSaveVO, SopIsvInfoUpdateVO, SopIsvInfoPageQuery, SopIsvInfoResultVO> {
    private final EchoService echoService;
    private final SopIsvKeysService sopIsvKeysService;

    @Override
    public EchoService getEchoService() {
        return echoService;
    }


    /**
     * 获取秘钥信息
     *
     * @param isvId 主表ID
     * @return 秘钥
     */
    @GetMapping("/getKeys")
    public R<SopIsvKeysResultVO> getKeys(@RequestParam Long isvId) {
        return R.success(superService.getKeys(isvId));
    }

    /**
     * 生成秘钥
     *
     * @param param 参数
     * @return 秘钥
     * @throws Exception 异常
     */
    @PostMapping("createKeys")
    public R<RSATool.KeyStore> createKeys(@Validated @RequestBody IsvKeysGenVO param) throws Exception {
        RSATool.KeyFormat format = RSATool.KeyFormat.of(param.getKeyFormat());
        return R.success(superService.createKeys(format));
    }

    /**
     * 修改秘钥
     *
     * @param param 表单数据
     * @return 返回影响行数
     */
    @PostMapping("/updateKeys")
    public R<Boolean> updateKeys(@Validated @RequestBody SopIsvKeysUpdateVO param) {
        return R.success(sopIsvKeysService.saveKeys(param));
    }

    /**
     * 修改状态
     *
     * @param param 表单数据
     * @return 返回影响行数
     */
    @PostMapping("/updateStatus")
    public R<Boolean> updateStatus(@Validated @RequestBody StatusUpdateVO param) {
        return R.success(superService.updateStatus(param));
    }

    /**
     * 审核
     * @param param 审核参数
     * @return ISV 组件
     */
    @PostMapping("/examine")
    public R<Long> examine(@Validated @RequestBody AudioVO param) {
        return R.success(superService.examine(param));
    }


    /**
     * 个人申请
     *
     * @param param
     * @return 返回添加后的主键值
     */
    @PostMapping("/open/applyFor")
    public R<Long> applyFor(@Validated @RequestBody SopIsvInfoApplyForVO param) {
        return R.success(superService.applyFor(param));
    }

    @PostMapping("/open/submit")
    public R<Long> submit(@Validated @RequestBody SopIsvInfoSubmitVO param) {
        return R.success(superService.submit(param));
    }

    @PutMapping("/open/update")
    public R<Long> update(@Validated @RequestBody SopIsvInfoOpenUpdateVO param) {
        return R.success(superService.update(param));
    }


    @PostMapping("/open/page")
    public R<IPage<SopIsvInfoResultVO>> openPage(@Validated @RequestBody PageParams<SopIsvInfoPageQuery> param) {
        IPage<SopIsvInfo> page = param.buildPage();
        SopIsvInfoPageQuery model = param.getModel();
        LbQueryWrap<SopIsvInfo> wrap = Wraps.<SopIsvInfo>lbQ().eq(SopIsvInfo::getName, model.getName())
                .eq(SopIsvInfo::getStatus, model.getStatus())
                .eq(SopIsvInfo::getRemark, model.getRemark())
                .eq(SopIsvInfo::getCreatedBy, ContextUtil.getUserId())
                .eq(SopIsvInfo::getTenantId, ContextUtil.getTenantId());
        superService.page(page, wrap);
        return R.success(BeanPlusUtil.toBeanPage(page, SopIsvInfoResultVO.class));
    }
}


