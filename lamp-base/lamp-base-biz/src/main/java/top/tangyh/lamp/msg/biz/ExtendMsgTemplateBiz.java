package top.tangyh.lamp.msg.biz;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.utils.ArgumentAssert;
import top.tangyh.lamp.msg.entity.DefMsgTemplate;
import top.tangyh.lamp.msg.entity.ExtendMsgTemplate;
import top.tangyh.lamp.msg.service.DefMsgTemplateService;
import top.tangyh.lamp.msg.service.ExtendMsgTemplateService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zuihou
 * @date 2022/7/21 0021 18:49
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ExtendMsgTemplateBiz {

    private final ExtendMsgTemplateService extendMsgTemplateService;
    private final DefMsgTemplateService defMsgTemplateService;

    public Boolean importMsgTemplate(List<Long> ids) {
        ArgumentAssert.notEmpty(ids, "请选择消息模板");
        List<DefMsgTemplate> defMsgTemplateList = defMsgTemplateService.listByIds(ids);
        ArgumentAssert.notEmpty(defMsgTemplateList, "请选择正确的消息模板");
        List<String> codeList = defMsgTemplateList.stream().map(DefMsgTemplate::getCode).toList();

        List<ExtendMsgTemplate> existMsgTemplateList = extendMsgTemplateService.list(
                Wraps.<ExtendMsgTemplate>lbQ().in(ExtendMsgTemplate::getCode, codeList));
        Set<String> exists = existMsgTemplateList.stream().map(ExtendMsgTemplate::getCode).collect(Collectors.toSet());

        List<ExtendMsgTemplate> extendMsgTemplateList = new ArrayList<>();
        defMsgTemplateList.forEach(item -> {
            if (!exists.contains(item.getCode())) {
                ExtendMsgTemplate extendMsgTemplate = BeanUtil.toBean(item, ExtendMsgTemplate.class);
                extendMsgTemplate.setId(null);
                extendMsgTemplate.setCreatedBy(null);
                extendMsgTemplate.setCreatedTime(null);
                extendMsgTemplate.setUpdatedBy(null);
                extendMsgTemplate.setUpdatedTime(null);
                extendMsgTemplateList.add(extendMsgTemplate);
            }
        });

        return extendMsgTemplateService.saveBatch(extendMsgTemplateList);
    }
}
