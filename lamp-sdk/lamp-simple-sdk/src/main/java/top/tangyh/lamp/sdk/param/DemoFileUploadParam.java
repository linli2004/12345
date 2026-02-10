package top.tangyh.lamp.sdk.param;


import top.tangyh.lamp.sdk.request.DemoFileUploadRequest;
import top.tangyh.lamp.sdk.response.GetProductResponse;
import top.tangyh.lamp.sdkcore.param.BaseParam;

/**
 * @author 六如
 */
public class DemoFileUploadParam extends BaseParam<DemoFileUploadRequest, GetProductResponse> {
    @Override
    protected String method() {
        return "openapi.upload.more";
    }
}
