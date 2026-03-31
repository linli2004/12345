package top.tangyh.lamp.base.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 督办工单导入Excel对象
 *
 * @author lunar
 * @date 2026-03-13 16:00:00
 */
@Data
public class ChiefWorkOrderItemExcel {

    @ExcelProperty("标题")
    private String title;

    @ExcelProperty("转办单工单编号")
    private String workOrderNo;

    @ExcelProperty("联系号码")
    private String contactPhone;

    @ExcelProperty("诉求号码")
    private String appealPhone;
    @ExcelProperty("诉求号码")
    private String sourceDeptName;

    @ExcelProperty("诉求内容")
    private String appealContent;

    @ExcelProperty("转办单回复内容")
    private String replyContent;

    @ExcelProperty("诉求类型")
    private String appealType;

    @ExcelProperty("转办单满意度")
    private String satisfaction;

    @ExcelProperty("转办单不满意回访内容")
    private String unsatisfiedReason;

    @ExcelProperty("线上督办单编号")
    private String onlineSupervisionNo;

    @ExcelProperty("督办单回访时间")
    private String supervisionReturnTime;

    @ExcelProperty("督办单办结时间")
    private String supervisionFinishTime;

    @ExcelProperty("督办单回复内容")
    private String supervisionReplyContent;

    @ExcelProperty("督办单短信评价满意度")
    private String supervisionSmsSatisfaction;

    @ExcelProperty("督办单回访内容")
    private String supervisionReturnContent;

    @ExcelProperty("督办单研判（一级）")
    private String judgmentLevel1;

    @ExcelProperty("督办单研判（二级）")
    private String judgmentLevel2;

    @ExcelProperty(index = 18) // 根据列顺序映射，第19列
    private String processResult;

    @ExcelProperty("是否为最终办理结果")
    private String isFinal;

    @ExcelProperty("计划完成时间（非最终办理结果填写此列）")
    private String planFinishTime;

    @ExcelProperty("分类调整")
    private String categoryAdjust;

    @ExcelProperty("承办单位")
    private String undertakerUnit;

    @ExcelProperty(index = 23) // 根据列顺序映射，第24列
    private String finalResult;
}
