package top.tangyh.lamp.sdk.param;

import top.tangyh.lamp.sdk.request.SaveBaseEmployeeRequest;
import top.tangyh.lamp.sdk.response.GetBaseEmployeeResponse;
import top.tangyh.lamp.sdkcore.param.BaseParam;

public class SaveBaseEmployeeParam extends BaseParam<SaveBaseEmployeeRequest, GetBaseEmployeeResponse> {
    @Override
    protected String method() {
        return "openapi.employee.save";
    }

}
