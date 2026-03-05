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
     * 角色编码:1-镇级网格专员;2-基层部门分管领导;3-基层部门负责人;4-基层部门专员
     */
    String ROLE_CODE_TOWN_SPECIALIST = "1";
    String ROLE_CODE_DEPT_LEADER = "2";
    String ROLE_CODE_DEPT_MANAGER = "3";
    String ROLE_CODE_DEPT_SPECIALIST = "4";

    /**
     * 业务节点编码：1、镇级未签收 2、镇级已签收 3.1、镇级初次交办 3.2、镇级结案 3.3、镇级退回
     * 4、基层签收 5.2、基层结案 5.3、基层退回
     */
    String NODE_CODE_TOWN_NOT_SIGN = "1";
    String NODE_CODE_TOWN_SIGN = "2";
    String NODE_CODE_TOWN_FIRST_PROCESSING = "3.1";
    String NODE_CODE_TOWN_FINAL = "3.2";
    String NODE_CODE_TOWN_BACK = "3.3";
    String NODE_CODE_BASIC_SIGN = "4";
    String NODE_CODE_BASIC_FINAL = "5.2";
    String NODE_CODE_BASIC_BACK = "5.3";

    Map<String, String> PROCESS_TYPE_MAP = ImmutableMap.<String, String>builder()
            .put(NODE_CODE_TOWN_NOT_SIGN, "导入")
            .put(NODE_CODE_TOWN_SIGN, "签收")
            .put(NODE_CODE_TOWN_FIRST_PROCESSING, "交办")
            .put(NODE_CODE_TOWN_FINAL, "办结")
            .put(NODE_CODE_TOWN_BACK, "退回")
            .put(NODE_CODE_BASIC_SIGN, "签收")
            .put(NODE_CODE_BASIC_FINAL, "办结")
            .put(NODE_CODE_BASIC_BACK, "退回")
            .build();

}
