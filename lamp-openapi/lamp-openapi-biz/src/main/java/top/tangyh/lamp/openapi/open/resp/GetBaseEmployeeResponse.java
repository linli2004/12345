package top.tangyh.lamp.openapi.open.resp;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetBaseEmployeeResponse {
    private Long id;
    private String realName;
    private LocalDateTime createdTime;

    // 其他字段省略了
}
