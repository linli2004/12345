package top.tangyh.lamp.openapi.open.req;

import lombok.Data;

@Data
public class SaveBaseEmployeeRequest {

    private Integer positionStatus;
    private String name;
    private Integer activeStatus;
}
