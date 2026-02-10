package top.tangyh.lamp.system.service.tenant;

import top.tangyh.basic.base.service.SuperCacheService;
import top.tangyh.lamp.system.entity.tenant.DefTenant;
import top.tangyh.lamp.system.vo.result.tenant.DefTenantResultVO;
import top.tangyh.lamp.system.vo.save.tenant.DefTenantInitVO;
import top.tangyh.lamp.system.vo.save.tenant.DefTenantSaveVO;

import java.util.List;

/**
 * <p>
 * 业务接口
 * 企业
 * </p>
 *
 * @author zuihou
 * @date 2021-09-13
 */
public interface DefTenantService extends SuperCacheService<Long, DefTenant> {
    /**
     * 检测 租户编码是否存在
     *
     * @param tenantCode 租户编码
     * @return 是否存在
     */
    boolean check(String tenantCode);


    /**
     * 创建数据库，初始化表， 初始化数据
     *
     * @param tenantConnect 链接信息
     * @return 是否链接成功
     */
    Boolean initData(DefTenantInitVO tenantConnect);


    /**
     * 删除租户数据
     *
     * @param ids id
     * @return 是否成功
     */
    Boolean delete(List<Long> ids);

    /**
     * 删除租户和基础数据
     *
     * @param ids id
     * @return 是否成功
     */
    Boolean deleteAll(List<Long> ids);

    /**
     * 查询所有可用的租户
     *
     * @return 租户信息
     */
    List<DefTenant> findNormalTenant();

    /**
     * 修改租户审核状态
     *
     * @param id             租户id
     * @param status         审核状态
     * @param reviewComments 审核意见
     * @return
     */
    Boolean updateStatus(Long id, String status, String reviewComments);

    /**
     * 查询用户的可用企业
     *
     * @param userId 用户id
     * @return
     */
    List<DefTenantResultVO> listTenantByUserId(Long userId);

    /**
     * 修改租户使用状态
     *
     * @param id    租户id
     * @param state 状态
     * @return
     */
    Boolean updateState(Long id, Boolean state);

    /**
     * 用户自行注册企业信息
     *
     * @param defTenantSaveVO
     * @return
     */
    DefTenant register(DefTenantSaveVO defTenantSaveVO);
}
