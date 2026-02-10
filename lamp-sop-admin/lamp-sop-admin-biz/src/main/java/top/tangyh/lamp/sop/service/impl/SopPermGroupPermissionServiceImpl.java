package top.tangyh.lamp.sop.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.lamp.sop.entity.SopPermGroupPermission;
import top.tangyh.lamp.sop.manager.SopPermGroupPermissionManager;
import top.tangyh.lamp.sop.service.SopPermGroupPermissionService;
import top.tangyh.lamp.sop.vo.save.SopPermGroupPermissionSaveVO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 业务实现类
 * 组权限表
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:47
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SopPermGroupPermissionServiceImpl extends SuperServiceImpl<SopPermGroupPermissionManager, Long, SopPermGroupPermission> implements SopPermGroupPermissionService {


    @Override
    @Transactional(rollbackFor = Exception.class)
    public <SaveVO> SopPermGroupPermission save(SaveVO saveVO) {
        SopPermGroupPermissionSaveVO param = (SopPermGroupPermissionSaveVO) saveVO;
        Long groupId = param.getGroupId();
        List<Long> apiIdList = param.getApiIdList();
        if (CollectionUtils.isEmpty(apiIdList)) {
            return null;
        }

        List<SopPermGroupPermission> existList = superManager.list(Wrappers.<SopPermGroupPermission>lambdaQuery().eq(SopPermGroupPermission::getGroupId, groupId));
        List<Long> existApiIdList = existList.stream().map(SopPermGroupPermission::getApiId).distinct().toList();

        List<SopPermGroupPermission> saveList = apiIdList.stream()
                // 已存在的不添加
                .filter(apiId -> !existApiIdList.contains(apiId))
                .map(apiId -> {
                    SopPermGroupPermission permGroupPermission = new SopPermGroupPermission();
                    permGroupPermission.setGroupId(groupId);
                    permGroupPermission.setApiId(apiId);
                    return permGroupPermission;
                })
                .collect(Collectors.toList());
        superManager.saveBatch(saveList);

        superManager.refreshIsvPerm(groupId);
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long groupId, List<Long> apiIdList) {
        boolean cnt = superManager.remove(Wrappers.<SopPermGroupPermission>lambdaQuery()
                .eq(SopPermGroupPermission::getGroupId, groupId).in(SopPermGroupPermission::getApiId, apiIdList));
        superManager.refreshIsvPerm(groupId);
        return cnt;
    }
}


