package top.tangyh.lamp.system.service.application.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.google.common.collect.Multimap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.service.impl.SuperCacheServiceImpl;
import top.tangyh.basic.context.ContextUtil;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.database.mybatis.conditions.query.LbQueryWrap;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.basic.utils.ArgumentAssert;
import top.tangyh.basic.utils.BeanPlusUtil;
import top.tangyh.basic.utils.CollHelper;
import top.tangyh.basic.utils.TreeUtil;
import top.tangyh.lamp.common.constant.AppendixType;
import top.tangyh.lamp.common.constant.DefValConstants;
import top.tangyh.lamp.common.constant.DsConstant;
import top.tangyh.lamp.file.service.AppendixService;
import top.tangyh.lamp.model.enumeration.system.ResourceTypeEnum;
import top.tangyh.lamp.model.vo.save.AppendixSaveVO;
import top.tangyh.lamp.system.entity.application.DefApplication;
import top.tangyh.lamp.system.entity.application.DefResource;
import top.tangyh.lamp.system.entity.application.DefUserApplication;
import top.tangyh.lamp.system.manager.application.DefApplicationManager;
import top.tangyh.lamp.system.manager.application.DefResourceManager;
import top.tangyh.lamp.system.manager.application.DefTenantApplicationRelManager;
import top.tangyh.lamp.system.manager.application.DefUserApplicationManager;
import top.tangyh.lamp.system.service.application.DefApplicationService;
import top.tangyh.lamp.system.vo.result.application.ApplicationResourceResultVO;
import top.tangyh.lamp.system.vo.result.application.DefApplicationResultVO;
import top.tangyh.lamp.system.vo.result.application.DefResourceResultVO;
import top.tangyh.lamp.system.vo.save.application.DefApplicationSaveVO;
import top.tangyh.lamp.system.vo.update.application.DefApplicationUpdateVO;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * <p>
 * 业务实现类
 * 应用
 * </p>
 *
 * @author zuihou
 * @date 2021-09-15
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@DS(DsConstant.DEFAULTS)
public class DefApplicationServiceImpl extends SuperCacheServiceImpl<DefApplicationManager, Long, DefApplication>
        implements DefApplicationService {
    private final DefResourceManager defResourceManager;
    private final EchoService echoService;
    private final AppendixService appendixService;
    private final DefUserApplicationManager defUserApplicationManager;
    private final DefTenantApplicationRelManager defTenantApplicationRelManager;

    @Override
    public List<Long> findApplicationByEmployeeId(Long employeeId) {
        return defTenantApplicationRelManager.findApplicationByEmployeeId(employeeId);
    }

    @Override
    public List<DefApplicationResultVO> findMyApplication(Long tenantId, String name) {
        if (tenantId == null) {
            tenantId = ContextUtil.getTenantId();
        }
        List<DefApplicationResultVO> list = superManager.findMyApplication(tenantId, name);
        list.forEach(item -> {
            //0-过期 1-有效
            String state = item.getExpirationTime() == null || item.getExpirationTime().isAfter(LocalDateTime.now()) ? "1" : "0";
            item.setState(state);
        });
        return list;
    }

    @Override
    public List<DefApplicationResultVO> findRecommendApplication(Long tenantId, String name) {
        if (tenantId == null) {
            tenantId = ContextUtil.getTenantId();
        }
        return superManager.findRecommendApplication(tenantId, name);
    }

    @Override
    public Boolean check(Long id, String name) {
        LbQueryWrap<DefApplication> wrap = Wraps.<DefApplication>lbQ().eq(DefApplication::getName, name)
                .ne(DefApplication::getId, id);
        return superManager.count(wrap) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <SaveVO> DefApplication save(SaveVO saveVO) {
        DefApplicationSaveVO applicationSaveVO = (DefApplicationSaveVO) saveVO;
        ArgumentAssert.isFalse(check(null, applicationSaveVO.getName()), "应用名称重复");
        DefApplication defApplication = BeanPlusUtil.toBean(applicationSaveVO, DefApplication.class);
        defApplication.setAppKey(RandomUtil.randomString(12));
        defApplication.setAppSecret(RandomUtil.randomString(36));
        defApplication.setIsVisible(true);
        superManager.save(defApplication);
        appendixService.save(AppendixSaveVO.build(defApplication.getId(), AppendixType.System.DEF__APPLICATION__LOGO, applicationSaveVO.getAppendixIcon()));
        return defApplication;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<Long> idList) {
        boolean b = super.removeByIds(idList);
        appendixService.removeByBizId(idList, AppendixType.System.DEF__APPLICATION__LOGO);
        return b;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <UpdateVO> DefApplication updateById(UpdateVO updateVO) {
        DefApplicationUpdateVO applicationUpdateVO = (DefApplicationUpdateVO) updateVO;
        ArgumentAssert.isFalse(check(applicationUpdateVO.getId(), applicationUpdateVO.getName()), "应用名称重复");
        DefApplication defApplication = BeanPlusUtil.toBean(applicationUpdateVO, DefApplication.class);
        superManager.updateById(defApplication);

        appendixService.save(AppendixSaveVO.build(defApplication.getId(), AppendixType.System.DEF__APPLICATION__LOGO, applicationUpdateVO.getAppendixIcon()));
        return defApplication;
    }

    @Override
    public List<ApplicationResourceResultVO> findApplicationResourceList() {
        List<DefApplication> list = list(Wraps.<DefApplication>lbQ().orderByAsc(DefApplication::getSortValue));
        List<Long> applicationIdList = list.stream().map(DefApplication::getId).toList();
        List<DefResource> resourceList = defResourceManager.list(Wraps.<DefResource>lbQ().eq(DefResource::getState, true)
                .in(DefResource::getApplicationId, applicationIdList).orderByAsc(DefResource::getSortValue));

        return buildAppResResult(list, BeanPlusUtil.toBeanList(resourceList, DefResourceResultVO.class));
    }

    @Override
    public List<ApplicationResourceResultVO> findAvailableApplicationResourceList() {
        Long tenantId = ContextUtil.getTenantId();
        List<DefApplication> list = superManager.findApplicationListByTenantId(tenantId);
        List<Long> applicationIdList = list.stream().map(DefApplication::getId).toList();
        List<DefResource> resourceList;
        if (CollUtil.isNotEmpty(applicationIdList)) {
            List<String> codes = Arrays.asList(ResourceTypeEnum.MENU.getCode(), ResourceTypeEnum.BUTTON.getCode(), ResourceTypeEnum.FIELD.getCode());
            // 不查询数据权限
            resourceList = defResourceManager.findResourceListByTenantIdAndApplicationIds(tenantId, applicationIdList, codes);
        } else {
            resourceList = Collections.emptyList();
        }
        return buildAppResResult(list, BeanPlusUtil.toBeanList(resourceList, DefResourceResultVO.class));
    }

    private List<ApplicationResourceResultVO> buildAppResResult(List<DefApplication> list, List<DefResourceResultVO> resultList) {
        // 回显 @Echo 标记的字段
        echoService.action(resultList);
        // 按应用ID分组
        Multimap<Long, DefResourceResultVO> resourceMap = CollHelper.iterableToMultiMap(resultList,
                DefResourceResultVO::getApplicationId, resource -> resource);

        return list.stream().map(item -> {
            Collection<DefResourceResultVO> resources = resourceMap.containsKey(item.getId()) ? resourceMap.get(item.getId()) : Collections.emptyList();
            return ApplicationResourceResultVO.builder()
                    .defApplication(item)
                    .resourceList(TreeUtil.buildTree(resources))
                    .build();
        }).toList();
    }

    @Override
    public List<ApplicationResourceResultVO> findAvailableApplicationDataScopeList() {
        Long tenantId = ContextUtil.getTenantId();
        // 查租户拥有的应用
        List<DefApplication> list = superManager.findApplicationListByTenantId(tenantId);
        List<Long> applicationIdList = list.stream().map(DefApplication::getId).toList();
        List<Long> resourceIdList;
        if (CollUtil.isNotEmpty(applicationIdList)) {
            // 查租户拥有的资源
            List<DefResource> dataScopeList = defResourceManager.findResourceListByTenantIdAndApplicationIds(tenantId, applicationIdList, Collections.singletonList(ResourceTypeEnum.DATA.getCode()));

            // 将id和treePath截取后 合并成list，其中treePath存放的是该节点的所有父节点ID
            Stream<Long> dataScopeIdStream = dataScopeList.parallelStream().map(DefResource::getId);
            Stream<Long> parentIdStream = dataScopeList.parallelStream()
                    // 将父节点路径截取为父ID数组
                    .map(item -> StrUtil.splitToArray(item.getTreePath(), DefValConstants.TREE_PATH_SPLIT))
                    // 数组流 转 字符串流
                    .flatMap(Arrays::stream)
                    // 去除空数据
                    .filter(ObjectUtil::isNotEmpty)
                    // 类型转换
                    .map(Convert::toLong);
            // 合并 数据权限ID 和 父ID
            resourceIdList = Stream.concat(dataScopeIdStream, parentIdStream).distinct().toList();
        } else {
            resourceIdList = Collections.emptyList();
        }
        List<DefResource> resourceList = CollUtil.isEmpty(resourceIdList) ? Collections.emptyList() :
                defResourceManager.list(Wraps.<DefResource>lbQ().in(DefResource::getId, resourceIdList).orderByAsc(DefResource::getSortValue));
        // 将应用和应用的资源封装后返回
        return buildAppResResult(list, BeanPlusUtil.toBeanList(resourceList, DefResourceResultVO.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDefApp(Long applicationId, Long userId) {
        defUserApplicationManager.remove(Wraps.<DefUserApplication>lbQ().eq(DefUserApplication::getUserId, userId));
        return defUserApplicationManager.save(DefUserApplication.builder()
                .applicationId(applicationId).userId(userId).build());
    }

    @Override
    public DefApplication getDefApp(Long userId) {
        DefUserApplication one = defUserApplicationManager.getOne(Wraps.<DefUserApplication>lbQ().eq(DefUserApplication::getUserId, userId), false);
        if (one == null) {
            return null;
        }
        return superManager.getByIdCache(one.getApplicationId());
    }
}
