package top.tangyh.lamp.base.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.exception.BizException;
import top.tangyh.basic.utils.TreeUtil;
import top.tangyh.lamp.base.entity.WorkOrderCategory;
import top.tangyh.lamp.base.manager.WorkOrderCategoryManager;
import top.tangyh.lamp.base.service.WorkOrderCategoryService;
import top.tangyh.lamp.base.vo.query.WorkOrderCategoryPageQuery;
import top.tangyh.lamp.base.vo.save.WorkOrderCategorySaveVO;
import top.tangyh.lamp.base.vo.update.WorkOrderCategoryUpdateVO;
import top.tangyh.lamp.common.constant.DsConstant;

import java.util.*;

/**
 * <p>
 * 业务实现类
 * 工单分类
 * </p>
 *
 * @author lunar
 * @date 2026-02-27 09:33:51
 * @create [2026-02-27 09:33:51] [lunar] [代码生成器生成]
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@DS(DsConstant.BASE_TENANT)
public class WorkOrderCategoryServiceImpl extends SuperServiceImpl<WorkOrderCategoryManager, Long, WorkOrderCategory> implements WorkOrderCategoryService {

    @Override
    public List<WorkOrderCategory> findTree(WorkOrderCategoryPageQuery query) {
        List<WorkOrderCategory> list = superManager.list(Wraps.<WorkOrderCategory>lbQ()
                .eq(WorkOrderCategory::getState, query.getState())
                .orderByAsc(WorkOrderCategory::getSortValue));
        return TreeUtil.buildTree(list);
    }

    @Override
    public Map<String, Object> getPathName(Long id) {
        WorkOrderCategory entity = superManager.getById(id);
        if (entity == null) {
            return Collections.emptyMap();
        }
        List<String> names = new ArrayList<>();
        names.add(entity.getName());
        Long parentId = entity.getParentId();
        while (parentId != null && parentId > 0) {
            WorkOrderCategory parent = superManager.getById(parentId);
            if (parent == null) {
                break;
            }
            names.add(0, parent.getName());
            parentId = parent.getParentId();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", entity.getId());
        map.put("name", String.join("-", names));
        return map;
    }

    @Override
    protected <SaveVO> WorkOrderCategory saveBefore(SaveVO saveVO) {
        WorkOrderCategorySaveVO vo = (WorkOrderCategorySaveVO) saveVO;
        long count = superManager.count(Wraps.<WorkOrderCategory>lbQ().eq(WorkOrderCategory::getName, vo.getName()));
        if (count > 0) {
            throw new BizException("分类名称不可重复");
        }
        return super.saveBefore(saveVO);
    }

    @Override
    protected <UpdateVO> WorkOrderCategory updateBefore(UpdateVO updateVO) {
        WorkOrderCategoryUpdateVO vo = (WorkOrderCategoryUpdateVO) updateVO;
        long count = superManager.count(Wraps.<WorkOrderCategory>lbQ()
                .eq(WorkOrderCategory::getName, vo.getName())
                .ne(WorkOrderCategory::getId, vo.getId()));
        if (count > 0) {
            throw new BizException("分类名称不可重复");
        }
        return super.updateBefore(updateVO);
    }

}
