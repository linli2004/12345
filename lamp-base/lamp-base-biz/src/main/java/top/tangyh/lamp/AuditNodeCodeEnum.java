package top.tangyh.lamp;

import lombok.Getter;

import java.util.Objects;

/**
 * 工作流节点编码定义
 * 规则：由 [业务动作] + [审批人] + [结果] 组合
 */
@Getter
public enum AuditNodeCodeEnum {

    NODE_CODE_BASIC_FINAL_DIRECTOR_APPROVE(Constant.AUDIT_TYPE_FINISH, Constant.NODE_CODE_BASIC_FINAL_DIRECTOR_APPROVE, true, Constant.ROLE_CODE_DEPT_DIRECTOR),
    NODE_CODE_BASIC_FINAL_DIRECTOR_REJECT(Constant.AUDIT_TYPE_FINISH, Constant.NODE_CODE_BASIC_FINAL_DIRECTOR_REJECT, false, Constant.ROLE_CODE_DEPT_DIRECTOR),
    NODE_CODE_BASIC_FINAL_LEADER_APPROVE(Constant.AUDIT_TYPE_FINISH, Constant.NODE_CODE_BASIC_FINAL_LEADER_APPROVE, true, Constant.ROLE_CODE_DEPT_LEADER),
    NODE_CODE_BASIC_FINAL_LEADER_REJECT(Constant.AUDIT_TYPE_FINISH, Constant.NODE_CODE_BASIC_FINAL_LEADER_REJECT, false, Constant.ROLE_CODE_DEPT_LEADER),
    NODE_CODE_BASIC_BACK_DIRECTOR_APPROVE(Constant.AUDIT_TYPE_BACK, Constant.NODE_CODE_BASIC_BACK_DIRECTOR_APPROVE, true, Constant.ROLE_CODE_DEPT_DIRECTOR),
    NODE_CODE_BASIC_BACK_DIRECTOR_REJECT(Constant.AUDIT_TYPE_BACK, Constant.NODE_CODE_BASIC_BACK_DIRECTOR_REJECT, false, Constant.ROLE_CODE_DEPT_DIRECTOR),
    NODE_CODE_BASIC_BACK_LEADER_APPROVE(Constant.AUDIT_TYPE_BACK, Constant.NODE_CODE_BASIC_BACK_LEADER_APPROVE, true, Constant.ROLE_CODE_DEPT_LEADER),
    NODE_CODE_BASIC_BACK_LEADER_REJECT(Constant.AUDIT_TYPE_BACK, Constant.NODE_CODE_BASIC_BACK_LEADER_REJECT, false, Constant.ROLE_CODE_DEPT_LEADER);

    private final String auditType;
    private final String roleCode;
    private final Boolean auditResult;
    private final String nodeCode;

    AuditNodeCodeEnum(String auditType, String nodeCode, Boolean auditResult, String roleCode) {
        this.auditType = auditType;
        this.roleCode = roleCode;
        this.auditResult = auditResult;
        this.nodeCode = nodeCode;
    }

    public static String getNodeCode(String auditType, Boolean auditResult, String roleCode) {
        for (AuditNodeCodeEnum node : values()) {
            if (Objects.equals(node.getAuditType(), auditType) && auditResult == node.getAuditResult() && Objects.equals(node.getRoleCode(), roleCode)) {
                return node.getNodeCode();
            }
        }
        return "";
    }
}
