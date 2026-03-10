package top.tangyh.lamp;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum NoticeNodeCodeEnum {

    NODE_CODE_TOWN_FIRST_PROCESSING(Constant.NODE_CODE_TOWN_FIRST_PROCESSING, Constant.ROLE_CODE_DEPT_SPECIALIST),
    NODE_CODE_BASIC_SIGN(Constant.NODE_CODE_BASIC_SIGN, Constant.ROLE_CODE_DEPT_SPECIALIST),
    NODE_CODE_BASIC_FINAL(Constant.NODE_CODE_BASIC_FINAL, Constant.ROLE_CODE_DEPT_DIRECTOR),
    NODE_CODE_BASIC_BACK(Constant.NODE_CODE_BASIC_BACK, Constant.ROLE_CODE_DEPT_DIRECTOR),
    NODE_CODE_BASIC_FINAL_DIRECTOR_APPROVE(Constant.NODE_CODE_BASIC_FINAL_DIRECTOR_APPROVE, Constant.ROLE_CODE_DEPT_LEADER),
    NODE_CODE_BASIC_FINAL_DIRECTOR_REJECT(Constant.NODE_CODE_BASIC_FINAL_DIRECTOR_REJECT, Constant.ROLE_CODE_DEPT_SPECIALIST),
    NODE_CODE_BASIC_FINAL_LEADER_REJECT(Constant.NODE_CODE_BASIC_FINAL_LEADER_REJECT, Constant.ROLE_CODE_DEPT_SPECIALIST),
    NODE_CODE_BASIC_BACK_DIRECTOR_APPROVE(Constant.NODE_CODE_BASIC_BACK_DIRECTOR_APPROVE, Constant.ROLE_CODE_DEPT_LEADER),
    NODE_CODE_BASIC_BACK_DIRECTOR_REJECT(Constant.NODE_CODE_BASIC_BACK_DIRECTOR_REJECT, Constant.ROLE_CODE_DEPT_SPECIALIST),
    NODE_CODE_BASIC_BACK_LEADER_REJECT(Constant.NODE_CODE_BASIC_BACK_LEADER_REJECT, Constant.ROLE_CODE_DEPT_SPECIALIST),
    NODE_CODE_TOWN_AGAIN_PROCESSING(Constant.NODE_CODE_TOWN_AGAIN_PROCESSING, Constant.ROLE_CODE_DEPT_SPECIALIST);


    private final String roleCode;
    private final String nodeCode;

    NoticeNodeCodeEnum(String nodeCode, String roleCode) {
        this.roleCode = roleCode;
        this.nodeCode = nodeCode;
    }

    public static String getRoleCode(String nodeCode) {
        for (NoticeNodeCodeEnum node : values()) {
            if (Objects.equals(node.getNodeCode(), nodeCode)) {
                return node.getRoleCode();
            }
        }
        return "";
    }
}
