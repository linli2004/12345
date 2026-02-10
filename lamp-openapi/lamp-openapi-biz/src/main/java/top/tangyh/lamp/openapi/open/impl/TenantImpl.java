package top.tangyh.lamp.openapi.open.impl;

import com.gitee.sop.support.context.OpenContext;
import com.gitee.sop.support.dto.CommonFileData;
import com.gitee.sop.support.dto.FileData;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Size;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.dubbo.config.annotation.DubboService;
import top.tangyh.basic.context.ContextUtil;
import top.tangyh.basic.utils.ArgumentAssert;
import top.tangyh.lamp.base.entity.user.BaseEmployee;
import top.tangyh.lamp.base.service.user.BaseEmployeeService;
import top.tangyh.lamp.openapi.open.TenantApi;
import top.tangyh.lamp.openapi.open.req.ProductSaveRequest;
import top.tangyh.lamp.openapi.open.resp.ProductResponse;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 开放接口实现
 *
 * @author 六如
 */
@DubboService
@Slf4j
public class TenantImpl implements TenantApi {

    @Resource
    private BaseEmployeeService baseEmployeeService;

    @Override
    public BaseEmployee getEmployeeById(@NotNull(message = "id必填") Integer id,
                                        @Size(max = 20, min = 5) String name,
                                        OpenContext context) {
        log.info("appId={}, tenantId={}", context.getAppId(), context.getTenantId());
        ArgumentAssert.notNull(context.getTenantId(), "请传递租户ID");

        ContextUtil.setTenantId(context.getTenantId());
        BaseEmployee baseEmployee = baseEmployeeService.getById(1452186486492364800L);
        log.info("baseEmployee={}", baseEmployee);
        return baseEmployee;
    }


    @Override
    public ProductResponse upload(ProductSaveRequest storySaveDTO, FileData file) {
        log.info("getName:{}", file.getName());
        log.info("getOriginalFilename:{}", file.getOriginalFilename());
        checkFile(List.of(file));

        ProductResponse storyResponse = new ProductResponse();
        storyResponse.setId(1);
        storyResponse.setName(file.getOriginalFilename());
        return storyResponse;
    }


    @Override
    public ProductResponse upload2(ProductSaveRequest storySaveDTO, FileData idCardFront, FileData idCardBack) {
        log.info("upload:{}", storySaveDTO);
        checkFile(Arrays.asList(idCardFront, idCardBack));

        ProductResponse storyResponse = new ProductResponse();
        storyResponse.setId(1);
        storyResponse.setName(storySaveDTO.getProductName());
        return storyResponse;
    }

    @Override
    public ProductResponse upload3(ProductSaveRequest storySaveDTO, List<FileData> files) {
        List<String> list = new ArrayList<>();
        list.add("upload:" + storySaveDTO);
        checkFile(files);

        ProductResponse storyResponse = new ProductResponse();
        storyResponse.setId(1);
        storyResponse.setName(storySaveDTO.getProductName());
        return storyResponse;
    }

    @Override
    @SneakyThrows
    public FileData download(Integer id) {
        CommonFileData fileData = new CommonFileData();
        String str = "abc,你好~!@#\n";

        fileData.setOriginalFilename("smart-doc.json");
        fileData.setData(IOUtils.toByteArray(IOUtils.toInputStream(str, StandardCharsets.UTF_8)));


        return fileData;
    }

    private void checkFile(List<FileData> fileDataList) {
        for (FileData file : fileDataList) {
            ArgumentAssert.notNull(file.getName());
            ArgumentAssert.notNull(file.getOriginalFilename());
            ArgumentAssert.notNull(file.getBytes());
            ArgumentAssert.isTrue(!file.isEmpty());
        }
    }

}
