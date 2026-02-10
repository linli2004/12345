package top.tangyh.lamp.sdk.request;

import lombok.Data;

@Data
public class SaveBaseEmployeeRequest {

    private Integer positionStatus;
    private String name;
    private Integer activeStatus;
}
