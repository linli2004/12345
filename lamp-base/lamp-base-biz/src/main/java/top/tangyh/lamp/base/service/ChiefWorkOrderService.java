package top.tangyh.lamp.base.service;

import jakarta.servlet.http.HttpServletResponse;
import top.tangyh.basic.base.service.SuperService;
import top.tangyh.lamp.base.entity.ChiefWorkOrder;
import top.tangyh.lamp.base.vo.update.ChiefWorkOrderTaskActionVO;

import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * <p>
 * 业务接口
 * 督办工单
 * </p>
 *
 * @author lunar
 * @date 2026-03-13 16:00:00
 */
public interface ChiefWorkOrderService extends SuperService<Long, ChiefWorkOrder> {

    /**
     * 导入督办工单
     *
     * @param inputStream     excel文件流
     * @param supervisionTime 督办时间
     * @param name            名称
     */
    void importChiefWorkOrder(InputStream inputStream, LocalDateTime supervisionTime, String name, ChiefWorkOrderTaskActionVO actionVO);

    /**
     * 下载督办工单模板
     *
     * @param response 响应
     */
    void downloadTemplate(HttpServletResponse response);
    String generateBatchNo(LocalDateTime supervisionTime);
    void exportTaskZip(String batchNo, HttpServletResponse response);
}
