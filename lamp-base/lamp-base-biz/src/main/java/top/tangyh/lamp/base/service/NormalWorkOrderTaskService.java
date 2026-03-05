package top.tangyh.lamp.base.service;

import top.tangyh.basic.base.service.SuperService;
import top.tangyh.lamp.base.entity.NormalWorkOrderTask;
import top.tangyh.lamp.base.vo.update.NormalWorkOrderTaskActionVO;

import java.util.List;


/**
 * <p>
 * 业务接口
 * 工单子表
 * </p>
 *
 * @author lunar
 * @date 2026-03-03 11:45:59
 * @create [2026-03-03 11:45:59] [lunar] [代码生成器生成]
 */
public interface NormalWorkOrderTaskService extends SuperService<Long, NormalWorkOrderTask> {

    boolean batchSignNormalWorkOrder(NormalWorkOrderTaskActionVO signVO);

    boolean batchProcessingNormalWorkOrder(List<NormalWorkOrderTaskActionVO> processingVOList);

    boolean batchBackNormalWorkOrder(List<NormalWorkOrderTaskActionVO> backVOList);

    boolean finishNormalWorkOrder(NormalWorkOrderTaskActionVO finishVO);

    boolean basicSignNormalWorkOrder(NormalWorkOrderTaskActionVO signVO);

    boolean basicBackNormalWorkOrder(NormalWorkOrderTaskActionVO backVO);

    boolean basicFinishNormalWorkOrder(NormalWorkOrderTaskActionVO finishVO);
}


