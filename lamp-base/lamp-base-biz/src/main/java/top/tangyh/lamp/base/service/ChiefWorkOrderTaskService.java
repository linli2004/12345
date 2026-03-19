package top.tangyh.lamp.base.service;

import top.tangyh.basic.base.service.SuperService;
import top.tangyh.lamp.base.entity.ChiefWorkOrderTask;
import top.tangyh.lamp.base.vo.update.NormalWorkOrderTaskActionVO;

import java.util.List;

/**
 * <p>
 * 业务接口
 * 督办工单子表
 * </p>
 *
 * @author lunar
 * @date 2026-03-13 16:00:00
 */
public interface ChiefWorkOrderTaskService extends SuperService<Long, ChiefWorkOrderTask> {

    boolean batchSignChiefWorkOrder(NormalWorkOrderTaskActionVO signVO);

    boolean batchProcessingChiefWorkOrder(List<NormalWorkOrderTaskActionVO> processingVOList);

    boolean batchBackChiefWorkOrder(List<NormalWorkOrderTaskActionVO> backVOList);

    boolean finishChiefWorkOrder(NormalWorkOrderTaskActionVO finishVO);

    boolean basicSignChiefWorkOrder(NormalWorkOrderTaskActionVO signVO);

    boolean basicBackChiefWorkOrder(NormalWorkOrderTaskActionVO backVO);

    boolean basicFinishChiefWorkOrder(NormalWorkOrderTaskActionVO finishVO);

    boolean basicDirectorAuditChiefWorkOrder(NormalWorkOrderTaskActionVO auditVO);

    boolean basicLeaderAuditChiefWorkOrder(NormalWorkOrderTaskActionVO auditVO);

    boolean townBasicBackChiefWorkOrder(NormalWorkOrderTaskActionVO backVO);

    boolean townBasicFinishChiefWorkOrder(NormalWorkOrderTaskActionVO finishVO);

    boolean revokeChiefWorkOrder(NormalWorkOrderTaskActionVO revokeVO);

    boolean urgeChiefWorkOrder(NormalWorkOrderTaskActionVO urgeVO);

    boolean againProcessingChiefWorkOrder(NormalWorkOrderTaskActionVO processingVO);
}
