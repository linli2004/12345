package top.tangyh.lamp.sop.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.tangyh.basic.base.R;
import top.tangyh.basic.constant.Constants;
import top.tangyh.lamp.sop.dto.NotifyRequest;

/**
 *
 * @author tangyh
 * @since 2025/12/18 00:06
 */
@FeignClient(name = "${" + Constants.PROJECT_PREFIX + ".feign.sop-admin-server:lamp-sop-admin-server}")
public interface NotifyApi {

    /**
     * 回调
     *
     * @param request 参数
     * @return 返回结果 回调ID
     */
    @PostMapping(value = "/notify/exec")
    R<Long> notify(@RequestBody NotifyRequest request);

}
