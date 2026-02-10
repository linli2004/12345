package com.gitee.sop.gateway.service.dubbo;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.gitee.sop.gateway.common.ApiInfoDTO;
import com.gitee.sop.gateway.common.enums.StatusEnum;
import com.gitee.sop.gateway.dao.entity.ApiInfo;
import com.gitee.sop.gateway.dao.mapper.ApiInfoMapper;
import com.gitee.sop.gateway.service.manager.ApiManager;
import com.gitee.sop.gateway.util.CopyUtil;
import com.gitee.sop.support.service.ApiRegisterService;
import com.gitee.sop.support.service.dto.RegisterDTO;
import com.gitee.sop.support.service.dto.RegisterResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Collection;
import java.util.Objects;

/**
 * @author 六如
 */
@Slf4j
@DubboService
public class ApiRegisterServiceImpl implements ApiRegisterService {

    private static final int REG_SOURCE_SYS = 1;
    private final Snowflake snowflake = IdUtil.getSnowflake(1, 1);
    @Resource
    private ApiManager apiManager;
    @Resource
    private ApiInfoMapper apiInfoMapper;

    @Override
    public RegisterResult register(Collection<RegisterDTO> registerDTOS) {
        try {
            for (RegisterDTO registerDTO : registerDTOS) {
                log.info("注册开放接口, registerDTO={}", registerDTO);
                this.doReg(registerDTO);
            }
            return RegisterResult.success();
        } catch (Exception e) {
            log.error("接口注册失败", e);
            return RegisterResult.error(e.getMessage());
        }
    }

    private void doReg(RegisterDTO registerDTO) {
        ApiInfoDTO apiInfoDTO = CopyUtil.copyBean(registerDTO, ApiInfoDTO::new);
        apiInfoDTO.setStatus(StatusEnum.ENABLE.getValue());

        ApiInfo apiInfo = apiInfoMapper.getByNameVersion(apiInfoDTO.getApiName(), apiInfoDTO.getApiVersion());
        boolean isSave = false;
        if (apiInfo == null) {
            apiInfo = new ApiInfo();
            apiInfo.setId(snowflake.nextId());
            isSave = true;
        } else {
            check(apiInfo, registerDTO);
        }
        CopyUtil.copyPropertiesIgnoreNull(apiInfoDTO, apiInfo);
        apiInfo.setRegSource(REG_SOURCE_SYS);
        // 保存到数据库
        if (isSave) {
            apiInfoMapper.saveIgnoreNull(apiInfo);
        } else {
            apiInfoMapper.updateIgnoreNull(apiInfo);
        }

        apiInfoDTO.setId(apiInfo.getId());
        // 保存到缓存
        apiManager.save(apiInfoDTO);
    }

    private void check(ApiInfo apiInfo, RegisterDTO registerDTO) {
        if (!Objects.equals(apiInfo.getApplication(), registerDTO.getApplication())) {
            throw new RuntimeException("接口[" + registerDTO + "]已存在于[" + apiInfo.getApplication() + "]应用中.必须保证接口全局唯一");
        }
    }

}
