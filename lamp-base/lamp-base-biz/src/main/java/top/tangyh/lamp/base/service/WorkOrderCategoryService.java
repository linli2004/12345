package top.tangyh.lamp.base.service;

import top.tangyh.basic.base.service.SuperService;
import top.tangyh.lamp.base.entity.WorkOrderCategory;
import top.tangyh.lamp.base.vo.query.WorkOrderCategoryPageQuery;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * 业务接口
 * 工单分类
 * </p>
 *
 * @author lunar
 * @date 2026-02-27 09:33:51
 * @create [2026-02-27 09:33:51] [lunar] [代码生成器生成]
 */
public interface WorkOrderCategoryService extends SuperService<Long, WorkOrderCategory> {

    /**
     * 查询树结构
     *
     * @param query 参数
     * @return 树
     */
    List<WorkOrderCategory> findTree(WorkOrderCategoryPageQuery query);

    /**
     * 查询分类路径名称
     *
     * @param id 主键
     * @return 路径名称
     */
    Map<String, Object> getPathName(Long id);
}


