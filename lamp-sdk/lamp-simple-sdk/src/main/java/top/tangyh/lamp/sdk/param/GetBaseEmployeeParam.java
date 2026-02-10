package top.tangyh.lamp.sdk.param;

import top.tangyh.lamp.sdk.request.GetBaseEmployeeRequest;
import top.tangyh.lamp.sdk.response.GetBaseEmployeeResponse;
import top.tangyh.lamp.sdkcore.param.BaseParam;

public class GetBaseEmployeeParam extends BaseParam<GetBaseEmployeeRequest, GetBaseEmployeeResponse> {
    @Override
    protected String method() {
        return "openapi.employee.get";
    }

}
