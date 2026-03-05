package top.tangyh.lamp.base.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class NormalWorkOrderExcel {
    /**
     * 工单编号
     */
    @ExcelProperty("工单编号")
    private String orderNo;
    /**
     * 工单标题
     */
    @ExcelProperty("工单标题")
    private String orderTitle;
    /**
     * 诉求内容
     */
    @ExcelProperty("诉求内容")
    private String orderContent;
    /**
     * 诉求类型
     */
    @ExcelProperty("诉求类型")
    private String appealType;
    /**
     * 区交办时间
     */
    @ExcelProperty("区交办时间")
    private String regionAssignTime;
    /**
     * 区交办办结期限
     */
    @ExcelProperty("区交办办结期限")
    private String regionDeadline;
    /**
     * 紧急程度
     */
    @ExcelProperty("紧急程度")
    private String urgency;
    /**
     * 诉求归口
     */
    @ExcelProperty("诉求归口")
    private String sourceDeptName;
    /**
     * 诉求渠道
     */
    @ExcelProperty("诉求渠道")
    private String channel;
    /**
     * 求助人员
     */
    @ExcelProperty("求助人员")
    private String helperName;
    /**
     * 联系电话
     */
    @ExcelProperty("求助号码")
    private String contactPhone;
    /**
     * 性别
     */
    @ExcelProperty("性别")
    private String gender;
    /**
     * 事件发生地
     */
    @ExcelProperty("事件发生地")
    private String incidentLocation;
    /**
     * 诉求详细地址
     */
    @ExcelProperty("诉求详细地址")
    private String address;
    /**
     * 是否疑难;[0-否 1-是]
     */
    @ExcelProperty("是否疑难")
    private String isDifficult;
}
