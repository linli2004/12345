package top.tangyh.lamp.system.service.application.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.basic.cache.repository.CacheOps;
import top.tangyh.basic.context.ContextUtil;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.database.mybatis.conditions.query.LbQueryWrap;
import top.tangyh.basic.interfaces.echo.EchoService;
import top.tangyh.basic.model.cache.CacheKey;
import top.tangyh.basic.utils.ArgumentAssert;
import top.tangyh.basic.utils.BeanPlusUtil;
import top.tangyh.basic.utils.CollHelper;
import top.tangyh.basic.utils.TreeUtil;
import top.tangyh.lamp.common.cache.tenant.application.AllResourceApiCacheKeyBuilder;
import top.tangyh.lamp.common.cache.tenant.tenant.TenantApplicationCacheKeyBuilder;
import top.tangyh.lamp.common.cache.tenant.tenant.TenantResourceCacheKeyBuilder;
import top.tangyh.lamp.common.constant.DefValConstants;
import top.tangyh.lamp.common.constant.DsConstant;
import top.tangyh.lamp.model.entity.system.SysUser;
import top.tangyh.lamp.system.entity.application.DefApplication;
import top.tangyh.lamp.system.entity.application.DefResource;
import top.tangyh.lamp.system.entity.application.DefTenantApplicationRecord;
import top.tangyh.lamp.system.entity.application.DefTenantApplicationRel;
import top.tangyh.lamp.system.entity.application.DefTenantResourceRel;
import top.tangyh.lamp.system.entity.tenant.DefTenant;
import top.tangyh.lamp.system.enumeration.tenant.ApplicationGrantTypeEnum;
import top.tangyh.lamp.system.manager.application.DefApplicationManager;
import top.tangyh.lamp.system.manager.application.DefResourceManager;
import top.tangyh.lamp.system.manager.application.DefTenantApplicationRecordManager;
import top.tangyh.lamp.system.manager.application.DefTenantApplicationRelManager;
import top.tangyh.lamp.system.manager.application.DefTenantResourceRelManager;
import top.tangyh.lamp.system.manager.tenant.DefTenantManager;
import top.tangyh.lamp.system.service.application.DefTenantApplicationRelService;
import top.tangyh.lamp.system.vo.result.application.DefResourceResultVO;
import top.tangyh.lamp.system.vo.result.application.DefTenantApplicationRelResultVO;
import top.tangyh.lamp.system.vo.save.application.DefTenantApplicationRelSaveVO;
import top.tangyh.lamp.system.vo.update.application.DefTenantApplicationRelUpdateVO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 业务实现类
 * 租户的应用
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
public class DefTenantApplicationRelServiceImpl extends SuperServiceImpl<DefTenantApplicationRelManager, Long, DefTenantApplicationRel> implements DefTenantApplicationRelService {
    private static final String DELETE_APPLICATION_NAME = "应用已删除";
    private static final String DELETE_TENANT_NAME = "租户已删除";
    private final DefApplicationManager defApplicationManager;
    private final DefTenantManager defTenantManager;
    private final DefTenantResourceRelManager defTenantResourceRelManager;
    private final DefTenantApplicationRecordManager defTenantApplicationRecordManager;
    private final DefResourceManager defResourceManager;
    private final EchoService echoService;
    private final UidGenerator uidGenerator;
    private final CacheOps cacheOps;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean grant(DefTenantApplicationRelSaveVO saveVO, SysUser sysUser) {
        if (CollUtil.contains(saveVO.getTenantIdList(), DefValConstants.DEF_TENANT_ID)) {
            // 演示环境专用标识，用于WriteInterceptor拦截器判断演示环境需要禁止用户执行sql，若您无需搭建演示环境，可以删除下面一行代码
            ContextUtil.setStop();
        }

        Set<Long> applicationIdList = saveVO.getApplicationResourceMap().keySet();
        // 查询之前已经授权过的记录
        List<DefTenantApplicationRel> existsList = list(Wraps.<DefTenantApplicationRel>lbQ()
                .in(DefTenantApplicationRel::getApplicationId, applicationIdList)
                .in(DefTenantApplicationRel::getTenantId, saveVO.getTenantIdList())
        );
        // 查询需要被授权的 企业和应用，并转成map
        List<DefApplication> applicationList = defApplicationManager.listByIds(applicationIdList);
        ImmutableMap<Long, String> applicationMap = CollHelper.uniqueIndex(applicationList, DefApplication::getId, DefApplication::getName);
        List<DefTenant> tenantList = defTenantManager.listByIds(saveVO.getTenantIdList());
        ImmutableMap<Long, String> tenantMap = CollHelper.uniqueIndex(tenantList, DefTenant::getId, DefTenant::getName);

        List<DefTenantApplicationRecord> recordList = new ArrayList<>();
        List<DefTenantApplicationRel> grantList = new ArrayList<>();
        List<DefTenantResourceRel> tenantResourceList = new ArrayList<>();
        for (Long applicationId : applicationIdList) {
            if (!applicationMap.containsKey(applicationId)) {
                continue;
            }
            List<Long> resourceIdList = saveVO.getApplicationResourceMap().get(applicationId);
            for (Long tenantId : saveVO.getTenantIdList()) {
                if (!tenantMap.containsKey(tenantId)) {
                    continue;
                }
                long tenantApplicationId = uidGenerator.getUid();

                // 添加授权流水
                addRecord(recordList, applicationMap, tenantMap, applicationId, tenantId, existsList, tenantApplicationId, sysUser);

                // 添加当前授权数据
                addGrant(grantList, applicationId, tenantId, tenantApplicationId, saveVO.getExpirationTime());

                // 授权应用资源
                List<DefTenantResourceRel> collect = resourceIdList.stream().map(resourceId ->
                        DefTenantResourceRel.builder().tenantId(tenantId)
                                .applicationId(applicationId).resourceId(resourceId)
                                .build()
                ).toList();
                tenantResourceList.addAll(collect);
            }
        }
        // 保存授权流水
        boolean flag = defTenantApplicationRecordManager.saveBatch(recordList);

        // 保存租户-应用-资源
        defTenantResourceRelManager.remove(Wraps.<DefTenantResourceRel>lbQ()
                .in(DefTenantResourceRel::getTenantId, saveVO.getTenantIdList())
                .in(DefTenantResourceRel::getApplicationId, applicationIdList));
        defTenantResourceRelManager.saveBatch(tenantResourceList);

        // 重新授权
        superManager.remove(Wraps.<DefTenantApplicationRel>lbQ()
                .in(DefTenantApplicationRel::getTenantId, saveVO.getTenantIdList())
                .in(DefTenantApplicationRel::getApplicationId, applicationIdList)
        );
        superManager.saveBatch(grantList);

        cacheOps.del(saveVO.getTenantIdList().stream().map(TenantApplicationCacheKeyBuilder::builder).toArray(CacheKey[]::new));
        List<CacheKey> taKey = new ArrayList<>();
        for (Long tenantId : saveVO.getTenantIdList()) {
            for (Long applicationId : applicationIdList) {
                taKey.add(TenantResourceCacheKeyBuilder.builder(tenantId, applicationId));
            }
        }
        cacheOps.del(taKey);
        return flag;
    }

    private void addGrant(List<DefTenantApplicationRel> grantList, Long applicationId, Long tenantId,
                          Long tenantApplicationId, LocalDateTime expirationTime) {
        DefTenantApplicationRel tenantApplicationRel = new DefTenantApplicationRel();
        tenantApplicationRel.setApplicationId(applicationId);
        tenantApplicationRel.setTenantId(tenantId);
        tenantApplicationRel.setExpirationTime(expirationTime);
        tenantApplicationRel.setId(tenantApplicationId);
        grantList.add(tenantApplicationRel);
    }


    private void addRecord(List<DefTenantApplicationRecord> recordList,
                           ImmutableMap<Long, String> applicationMap, ImmutableMap<Long, String> companyMap,
                           Long applicationId, Long tenantId, List<DefTenantApplicationRel> existsList, Long tenantApplicationId, SysUser sysUser) {
        // 查找续期的情况
        boolean isExists = existsList.stream().anyMatch(exists ->
                exists.getTenantId().equals(tenantId) && exists.getApplicationId().equals(applicationId));

        DefTenantApplicationRecord record = new DefTenantApplicationRecord();

        record.setOperateByName(sysUser.getNickName());
        record.setApplicationId(applicationId);
        record.setApplicationName(applicationMap.getOrDefault(applicationId, DELETE_APPLICATION_NAME));
        record.setTenantName(companyMap.getOrDefault(tenantId, DELETE_TENANT_NAME));
        record.setTenantId(tenantId);
        record.setGrantType(isExists ? ApplicationGrantTypeEnum.RENEWAL.getCode() : ApplicationGrantTypeEnum.GRANT.getCode());
        record.setTenantApplicationRelId(tenantApplicationId);
        recordList.add(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancel(List<Long> ids, SysUser sysUser) {
        // 演示环境专用标识，用于WriteInterceptor拦截器判断演示环境需要禁止用户执行sql，若您无需搭建演示环境，可以删除下面一行代码
        ContextUtil.setStop();

        List<DefTenantApplicationRel> relList = superManager.listByIds(ids);
        if (relList.isEmpty()) {
            return false;
        }
        Set<Long> applicationIdSet = relList.stream().map(DefTenantApplicationRel::getApplicationId).collect(Collectors.toSet());
        Set<Long> tenantIdSet = relList.stream().map(DefTenantApplicationRel::getTenantId).collect(Collectors.toSet());

        List<DefApplication> applicationList = defApplicationManager.listByIds(applicationIdSet);
        ImmutableMap<Long, String> applicationMap = CollHelper.uniqueIndex(applicationList, DefApplication::getId, DefApplication::getName);
        List<DefTenant> companyList = defTenantManager.listByIds(tenantIdSet);
        ImmutableMap<Long, String> companyMap = CollHelper.uniqueIndex(companyList, DefTenant::getId, DefTenant::getName);

        List<DefTenantApplicationRecord> recordList = relList.stream().map(rel -> {
            DefTenantApplicationRecord record = new DefTenantApplicationRecord();
            record.setTenantApplicationRelId(rel.getId());
            record.setGrantType(ApplicationGrantTypeEnum.CANCEL.getCode());
            record.setOperateByName(sysUser.getNickName());
            record.setApplicationId(rel.getApplicationId());
            record.setApplicationName(applicationMap.getOrDefault(rel.getApplicationId(), DELETE_APPLICATION_NAME));
            record.setTenantName(companyMap.getOrDefault(rel.getTenantId(), DELETE_TENANT_NAME));
            record.setTenantId(rel.getTenantId());
            return record;
        }).toList();
        defTenantApplicationRecordManager.saveBatch(recordList);

        LbQueryWrap<DefTenantResourceRel> delWraps = Wraps.lbQ();
        relList.forEach(item ->
                delWraps.or(del -> del.eq(DefTenantResourceRel::getTenantId, item.getTenantId()).eq(DefTenantResourceRel::getApplicationId, item.getApplicationId()))
        );
        defTenantResourceRelManager.remove(delWraps);

        cacheOps.del(tenantIdSet.stream().map(TenantApplicationCacheKeyBuilder::builder).toArray(CacheKey[]::new));
        List<CacheKey> taKey = new ArrayList<>();
        for (Long tenantId : tenantIdSet) {
            for (Long applicationId : applicationIdSet) {
                taKey.add(TenantResourceCacheKeyBuilder.builder(tenantId, applicationId));
            }
        }
        cacheOps.del(taKey);
        return removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean renewal(DefTenantApplicationRelUpdateVO updateVO, SysUser sysUser) {
        DefTenantApplicationRel defTenantApplicationRel = super.getById(updateVO.getId());
        ArgumentAssert.notNull(defTenantApplicationRel, "找不到该授权信息");
        boolean flag = superManager.update(Wraps.<DefTenantApplicationRel>lbU().set(DefTenantApplicationRel::getExpirationTime, updateVO.getExpirationTime())
                .eq(DefTenantApplicationRel::getId, updateVO.getId()));

        DefApplication application = defApplicationManager.getById(defTenantApplicationRel.getApplicationId());
        DefTenant tenant = defTenantManager.getById(defTenantApplicationRel.getTenantId());
        if (tenant != null && DefValConstants.DEF_TENANT_ID.equals(tenant.getId())) {
            // 演示环境专用标识，用于WriteInterceptor拦截器判断演示环境需要禁止用户执行sql，若您无需搭建演示环境，可以删除下面一行代码
            ContextUtil.setStop();
        }

        DefTenantApplicationRecord record = new DefTenantApplicationRecord();
        record.setGrantType(ApplicationGrantTypeEnum.RENEWAL.getCode());
        record.setOperateByName(sysUser.getNickName());
        record.setApplicationId(defTenantApplicationRel.getApplicationId());
        record.setApplicationName(application != null ? application.getName() : DELETE_APPLICATION_NAME);
        record.setTenantApplicationRelId(updateVO.getId());
        record.setTenantName(tenant != null ? tenant.getName() : DELETE_TENANT_NAME);
        record.setTenantId(defTenantApplicationRel.getTenantId());
        defTenantApplicationRecordManager.save(record);

        defTenantResourceRelManager.remove(Wraps.<DefTenantResourceRel>lbQ().eq(DefTenantResourceRel::getApplicationId, defTenantApplicationRel.getApplicationId())
                .eq(DefTenantResourceRel::getTenantId, defTenantApplicationRel.getTenantId()));
        if (CollUtil.isNotEmpty(updateVO.getResourceIdList())) {
            List<DefTenantResourceRel> collect = updateVO.getResourceIdList().stream().map(resourceId ->
                    DefTenantResourceRel.builder()
                            .applicationId(defTenantApplicationRel.getApplicationId())
                            .tenantId(defTenantApplicationRel.getTenantId())
                            .resourceId(resourceId)
                            .build()
            ).toList();
            defTenantResourceRelManager.saveBatch(collect);
        }
        // 系统的所有URI与资源编码的缓存key
        cacheOps.del(AllResourceApiCacheKeyBuilder.builder());
        // 租户拥有的应用
        cacheOps.del(TenantApplicationCacheKeyBuilder.builder(defTenantApplicationRel.getTenantId()));
        // 租户拥有的资源
        cacheOps.del(TenantResourceCacheKeyBuilder.builder(defTenantApplicationRel.getTenantId(), defTenantApplicationRel.getApplicationId()));
        cacheOps.del(TenantResourceCacheKeyBuilder.builder(defTenantApplicationRel.getTenantId(), null));
        return flag;
    }

    @Override
    public DefTenantApplicationRelResultVO getDetailById(Long id) {
        DefTenantApplicationRel tar = getById(id);
        if (tar == null) {
            return null;
        }
        DefTenantApplicationRelResultVO resultVO = new DefTenantApplicationRelResultVO();
        BeanUtil.copyProperties(tar, resultVO);

        List<DefResource> resourceList = defResourceManager.findByApplicationId(Collections.singletonList(tar.getApplicationId()));
        List<DefResourceResultVO> resultList = BeanPlusUtil.toBeanList(resourceList, DefResourceResultVO.class);
        echoService.action(resultList);
        resultVO.setResourceList(TreeUtil.buildTree(resultList));

        List<Long> resourceIdList = defTenantResourceRelManager.listObjs(
                Wraps.<DefTenantResourceRel>lbQ()
                        .select(DefTenantResourceRel::getResourceId)
                        .eq(DefTenantResourceRel::getTenantId, tar.getTenantId())
                        .eq(DefTenantResourceRel::getApplicationId, tar.getApplicationId()), Convert::toLong);
        resultVO.setCheckedList(resourceIdList);

        return resultVO;
    }
}
