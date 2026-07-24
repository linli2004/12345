package top.tangyh.lamp.base.service;

import top.tangyh.basic.base.service.SuperService;
import top.tangyh.lamp.base.entity.WorkOrderTemporary;
import top.tangyh.lamp.base.vo.query.WorkOrderTemporaryPageQuery;
import top.tangyh.lamp.base.vo.result.WorkOrderTemporaryResultVO;


/**
 * <p>
 * 业务接口
 * 工单办理暂存
 * </p>
 *
 * @author lunar
 * @date 2026-03-12 11:50:36
 * @create [2026-03-12 11:50:36] [lunar] [代码生成器生成]
 */
public interface WorkOrderTemporaryService extends SuperService<Long, WorkOrderTemporary> {

    /**
     * 结案待审无暂存时，构造单派工单的结案审批默认回填数据。
     * 多派、无办结来源或非结案页签返回 null。
     */
    WorkOrderTemporaryResultVO buildFinishAuditTemporary(WorkOrderTemporaryPageQuery query);
}


