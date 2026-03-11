package top.tangyh.lamp;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public interface Constant {
    /**
     * 工单子任务是否有效:1-有效;0-无效
     */
    String TASK_VALID = "1";
    String TASK_INVALID = "0";

    /**
     * 工单子任务显示等级:0-优先显示;1-不优先
     */
    String TASK_LEVEL_0 = "0";
    String TASK_LEVEL_1 = "1";

    /**
     * 工单子任务审批事项:1-结案;2-退回
     */
    String AUDIT_TYPE_FINISH = "1";
    String AUDIT_TYPE_BACK = "2";

    /**
     * 结案条件:0-单主办;1-任意结案;2-全部结案
     */
    String SETTLE_CONDITION_ALL = "2";

    /**
     * 角色编码:0-镇级领导;1-镇级网格专员;2-基层部门分管领导;3-基层部门负责人;4-基层部门专员
     */
    String ROLE_CODE_TOWN_LEADER = "0";
    String ROLE_CODE_TOWN_SPECIALIST = "1";
    String ROLE_CODE_DEPT_LEADER = "2";
    String ROLE_CODE_DEPT_DIRECTOR = "3";
    String ROLE_CODE_DEPT_SPECIALIST = "4";

    /**
     * 业务节点编码：1、镇级未签收 2、镇级已签收 3.1、镇级初次交办 3.2、镇级结案 3.3、镇级退回
     * 4、基层签收 5.2、基层结案 5.3、基层退回
     * 11.1、基层结案负责人审批通过 11.2、基层结案负责人审批拒绝 12.1、基层结案领导审批通过 12.2、基层结案领导审批拒绝
     * 14.1、基层退回负责人审批通过 14.2、基层退回负责人审批拒绝 15.1、基层退回领导审批通过 15.2、基层退回领导审批拒绝
     * 13.1 镇级处理基层结案 13.2 镇级处理基层交办 13.3 镇级处理基层退回
     */
    String NODE_CODE_TOWN_NOT_SIGN = "1";
    String NODE_CODE_TOWN_SIGN = "2";
    String NODE_CODE_TOWN_FIRST_PROCESSING = "3.1";
    String NODE_CODE_TOWN_FINAL = "3.2";
    String NODE_CODE_TOWN_BACK = "3.3";
    String NODE_CODE_BASIC_SIGN = "4";
    String NODE_CODE_BASIC_FINAL = "5.2";
    String NODE_CODE_BASIC_BACK = "5.3";
    String NODE_CODE_BASIC_FINAL_DIRECTOR_APPROVE = "11.1";
    String NODE_CODE_BASIC_FINAL_DIRECTOR_REJECT = "11.2";
    String NODE_CODE_BASIC_FINAL_LEADER_APPROVE = "12.1";
    String NODE_CODE_BASIC_FINAL_LEADER_REJECT = "12.2 ";
    String NODE_CODE_BASIC_BACK_DIRECTOR_APPROVE = "14.1";
    String NODE_CODE_BASIC_BACK_DIRECTOR_REJECT = "14.2";
    String NODE_CODE_BASIC_BACK_LEADER_APPROVE = "15.1";
    String NODE_CODE_BASIC_BACK_LEADER_REJECT = "15.2 ";
    String NODE_CODE_TOWN_BASIC_FINAL = "13.1";
    String NODE_CODE_TOWN_AGAIN_PROCESSING = "13.2";
    String NODE_CODE_TOWN_BASIC_BACK = "13.3";

    Map<String, String> PROCESS_TYPE_MAP = ImmutableMap.<String, String>builder()
            .put(NODE_CODE_TOWN_NOT_SIGN, "导入")
            .put(NODE_CODE_TOWN_SIGN, "签收")
            .put(NODE_CODE_TOWN_FIRST_PROCESSING, "交办")
            .put(NODE_CODE_TOWN_FINAL, "办结")
            .put(NODE_CODE_TOWN_BACK, "退回")
            .put(NODE_CODE_BASIC_SIGN, "签收")
            .put(NODE_CODE_BASIC_FINAL, "办结")
            .put(NODE_CODE_BASIC_BACK, "退回")
            .put(NODE_CODE_BASIC_FINAL_DIRECTOR_APPROVE, "审批")
            .put(NODE_CODE_BASIC_FINAL_DIRECTOR_REJECT, "审批")
            .put(NODE_CODE_BASIC_FINAL_LEADER_APPROVE, "审批")
            .put(NODE_CODE_BASIC_FINAL_LEADER_REJECT, "审批")
            .put(NODE_CODE_BASIC_BACK_DIRECTOR_APPROVE, "审批")
            .put(NODE_CODE_BASIC_BACK_DIRECTOR_REJECT, "审批")
            .put(NODE_CODE_BASIC_BACK_LEADER_APPROVE, "审批")
            .put(NODE_CODE_BASIC_BACK_LEADER_REJECT, "审批")
            .put(NODE_CODE_TOWN_BASIC_FINAL, "办结")
            .put(NODE_CODE_TOWN_AGAIN_PROCESSING, "交办")
            .put(NODE_CODE_TOWN_BASIC_BACK, "退回")
            .build();

    Map<String, String> SUB_WORK_ORDER_TYPE_MAP = ImmutableMap.<String, String>builder()
            .put(NODE_CODE_TOWN_FIRST_PROCESSING, "待签收(基层部门12345专员)")
            .put(NODE_CODE_BASIC_SIGN, "处办中(基层部门12345专员)")
            .put(NODE_CODE_BASIC_FINAL, "结案待审(基层部门负责人)")
            .put(NODE_CODE_BASIC_BACK, "下级已退回(基层部门负责人)")
            .put(NODE_CODE_BASIC_FINAL_DIRECTOR_APPROVE, "结案待审(基层部门分管领导)")
            .put(NODE_CODE_BASIC_FINAL_DIRECTOR_REJECT, "处办中(基层部门12345专员)")
            .put(NODE_CODE_BASIC_FINAL_LEADER_APPROVE, "结案已审通过(基层部门分管领导)")
            .put(NODE_CODE_BASIC_FINAL_LEADER_REJECT, "处办中(基层部门12345专员)")
            .put(NODE_CODE_BASIC_BACK_DIRECTOR_APPROVE, "下级已退回(基层部门分管领导)")
            .put(NODE_CODE_BASIC_BACK_DIRECTOR_REJECT, "处办中(基层部门12345专员)")
            .put(NODE_CODE_BASIC_BACK_LEADER_APPROVE, "退回已审通过(基层部门分管领导)")
            .put(NODE_CODE_BASIC_BACK_LEADER_REJECT, "处办中(基层部门12345专员)")
            .put(NODE_CODE_TOWN_BASIC_FINAL, "已办结")
            .put(NODE_CODE_TOWN_BASIC_BACK, "已退回")
            .put(NODE_CODE_TOWN_AGAIN_PROCESSING, "待签收(基层部门12345专员)")
            .build();

}
