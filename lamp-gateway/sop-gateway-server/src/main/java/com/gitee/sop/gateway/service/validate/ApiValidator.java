package com.gitee.sop.gateway.service.validate;

import com.gitee.sop.gateway.common.ApiInfoDTO;
import com.gitee.sop.gateway.common.enums.StatusEnum;
import com.gitee.sop.gateway.common.enums.YesOrNoEnum;
import com.gitee.sop.gateway.config.GateApiConfig;
import com.gitee.sop.gateway.exception.ApiException;
import com.gitee.sop.gateway.message.ErrorEnum;
import com.gitee.sop.gateway.request.ApiRequest;
import com.gitee.sop.gateway.request.ApiRequestContext;
import com.gitee.sop.gateway.request.RequestFormatEnum;
import com.gitee.sop.gateway.request.UploadContext;
import com.gitee.sop.gateway.service.manager.ApiManager;
import com.gitee.sop.gateway.service.manager.IpBlacklistManager;
import com.gitee.sop.gateway.service.manager.IsvApiPermissionManager;
import com.gitee.sop.gateway.service.manager.IsvManager;
import com.gitee.sop.gateway.service.manager.SecretManager;
import com.gitee.sop.gateway.service.manager.dto.IsvDTO;
import com.gitee.sop.support.enums.ApiModeEnum;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * 负责校验,校验工作都在这里
 *
 * @author 六如
 */
@Slf4j
@Service
public class ApiValidator implements Validator {

    private static final long MILLISECOND_OF_ONE_SECOND = 1000;

    /**
     * 单个文件内容最大值
     */
    @Value("${upload.one-file-max-size}")
    private DataSize oneFileMaxSize;

    /**
     * 总文件最大值
     */
    @Value("${upload.total-file-max-size}")
    private DataSize maxFileSize;

    @Resource
    private Signer signer;

    @Resource
    private GateApiConfig apiConfig;

    @Resource
    private ApiManager apiManager;

    @Resource
    private IpBlacklistManager ipBlacklistManager;

    @Resource
    private IsvApiPermissionManager isvApiPermissionManager;

    @Resource
    private IsvManager isvManager;

    @Resource
    private SecretManager secretManager;

    private DateTimeFormatter dateTimeFormatter;

    @Override
    public ValidateReturn validate(ApiRequestContext apiRequestContext) {
        ApiRequest apiRequest = apiRequestContext.getApiRequest();
        ApiInfoDTO apiInfo = apiManager.get(apiRequest.getMethod(), apiRequest.getVersion());
        // 检查接口信息
        checkApiInfo(apiRequestContext, apiInfo);
        // 校验字段完整性
        checkField(apiRequestContext);
        // 检查isv
        IsvDTO isvDTO = checkIsv(apiRequestContext);
        // 检查isv接口授权
        checkPermission(apiRequestContext, apiInfo, isvDTO);
        // 校验签名
        checkSign(apiRequestContext, isvDTO);
        // 检查是否超时
        checkTimeout(apiRequestContext);
        // 检查格式化
        checkFormat(apiRequestContext);
        // IP能否访问
        checkIP(apiRequestContext);
        // 检查上传文件
        checkUploadFile(apiRequestContext);
        // 检查token
        checkToken(apiRequestContext, apiInfo);
        return new ValidateReturn(apiInfo, isvDTO);
    }

    @Override
    public ValidateReturn validateRest(ApiRequestContext apiRequestContext) {
        ApiRequest apiRequest = apiRequestContext.getApiRequest();
        ApiInfoDTO apiInfo = apiManager.get(apiRequest.getMethod(), apiRequest.getVersion());
        // 检查接口信息
        checkApiInfo(apiRequestContext, apiInfo);

        if (!Objects.equals(apiInfo.getApiMode(), ApiModeEnum.RESTFUL.getValue())) {
            log.error("Open模式接口不允许使用Restful进行访问, apiInfo={}", apiInfo);
            throw new ApiException(ErrorEnum.ISV_INVALID_METHOD, apiRequestContext.getLocale());
        }
        // IP能否访问
        checkIP(apiRequestContext);
        // 检查上传文件
        checkUploadFile(apiRequestContext);
        // 检查token
        checkToken(apiRequestContext, apiInfo);
        return new ValidateReturn(apiInfo, null);
    }

    public void checkApiInfo(ApiRequestContext apiRequestContext, ApiInfoDTO apiInfoDTO) {
        // 检查路由是否存在
        if (apiInfoDTO == null) {
            throw new ApiException(ErrorEnum.ISV_INVALID_METHOD, apiRequestContext.getLocale());
        }
        // 检查路由是否启用
        if (StatusEnum.of(apiInfoDTO.getStatus()) != StatusEnum.ENABLE) {
            throw new ApiException(ErrorEnum.ISP_API_DISABLED, apiRequestContext.getLocale());
        }
    }

    public void checkPermission(ApiRequestContext apiRequestContext, ApiInfoDTO apiInfoDTO, IsvDTO isvDTO) {
        // 校验是否需要授权访问
        boolean needCheckPermission = BooleanUtils.toBoolean(apiInfoDTO.getIsPermission());
        if (needCheckPermission) {
            boolean hasPermission = isvApiPermissionManager.hasPermission(isvDTO.getId(), apiInfoDTO);
            if (!hasPermission) {
                throw new ApiException(ErrorEnum.ISV_ROUTE_NO_PERMISSIONS, apiRequestContext.getLocale());
            }
        }
    }

    public void checkField(ApiRequestContext apiRequestContext) {
        ApiRequest apiRequest = apiRequestContext.getApiRequest();
        if (apiRequest == null) {
            throw new ApiException(ErrorEnum.ISV_INVALID_PARAMETER, apiRequestContext.getLocale());
        }
        Locale locale = apiRequestContext.getLocale();
        if (ObjectUtils.isEmpty(apiRequest.getAppId())) {
            throw new ApiException(ErrorEnum.ISV_MISSING_APP_ID, locale);
        }
        if (ObjectUtils.isEmpty(apiRequest.getMethod())) {
            throw new ApiException(ErrorEnum.ISV_MISSING_METHOD, locale);
        }
        if (ObjectUtils.isEmpty(apiRequest.getVersion())) {
            throw new ApiException(ErrorEnum.ISV_MISSING_VERSION, locale);
        }
        if (ObjectUtils.isEmpty(apiRequest.getSignType())) {
            throw new ApiException(ErrorEnum.ISV_MISSING_SIGNATURE_CONFIG, locale);
        }
        if (ObjectUtils.isEmpty(apiRequest.getCharset())) {
            throw new ApiException(ErrorEnum.ISV_INVALID_CHARSET, locale);
        }
        if (ObjectUtils.isEmpty(apiRequest.getSign())) {
            throw new ApiException(ErrorEnum.ISV_MISSING_SIGNATURE, locale);
        }
        if (ObjectUtils.isEmpty(apiRequest.getTimestamp())) {
            throw new ApiException(ErrorEnum.ISV_MISSING_TIMESTAMP, locale);
        }
    }

    /**
     * 是否在IP黑名单中
     *
     * @param apiRequestContext 接口参数
     */
    protected void checkIP(ApiRequestContext apiRequestContext) {
        String ip = apiRequestContext.getIp();
        if (ipBlacklistManager.contains(ip)) {
            throw new ApiException(ErrorEnum.ISV_IP_FORBIDDEN, apiRequestContext.getLocale());
        }
    }


    /**
     * 校验上传文件内容
     *
     * @param apiRequestContext apiRequestContext
     */
    protected void checkUploadFile(ApiRequestContext apiRequestContext) {
        // 校验上传文件内容
        UploadContext uploadContext = apiRequestContext.getUploadContext();
        if (uploadContext != null) {
            List<MultipartFile> allFiles = uploadContext.getAllFile();
            if (ObjectUtils.isEmpty(allFiles)) {
                return;
            }

            for (MultipartFile multipartFile : allFiles) {
                checkSingleFileSize(apiRequestContext, multipartFile);
            }

            long totalSize = allFiles.stream()
                    .map(MultipartFile::getSize)
                    .mapToLong(Long::longValue)
                    .sum();

            if (totalSize > maxFileSize.toBytes()) {
                throw new ApiException(ErrorEnum.ISV_INVALID_FILE_SIZE, apiRequestContext.getLocale(), totalSize, maxFileSize);
            }
        }
    }

    /**
     * 校验单个文件大小
     *
     * @param file 文件
     */
    private void checkSingleFileSize(ApiRequestContext apiRequestContext, MultipartFile file) {
        long fileSize = file.getSize();
        long maxSize = oneFileMaxSize.toBytes();
        if (fileSize > maxSize) {
            throw new ApiException(ErrorEnum.ISV_INVALID_FILE_SIZE, apiRequestContext.getLocale(), fileSize, maxSize);
        }
    }

    protected void checkTimeout(ApiRequestContext apiRequestContext) {
        ApiRequest apiRequest = apiRequestContext.getApiRequest();
        int timeoutSeconds = apiConfig.getTimeoutSeconds();
        // 如果设置为0，表示不校验
        if (timeoutSeconds == 0) {
            return;
        }
        if (timeoutSeconds < 0) {
            throw new IllegalArgumentException("服务端timeoutSeconds设置错误");
        }
        String requestTime = apiRequest.getTimestamp();
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(requestTime, dateTimeFormatter);
            long diffMills = Duration.between(localDateTime, LocalDateTime.now()).toMillis();
            if (diffMills > timeoutSeconds * MILLISECOND_OF_ONE_SECOND) {
                throw new ApiException(ErrorEnum.ISV_INVALID_TIMESTAMP, apiRequestContext.getLocale());
            }
        } catch (DateTimeParseException e) {
            throw new ApiException(ErrorEnum.ISV_INVALID_TIMESTAMP, apiRequestContext.getLocale(), apiRequest.takeNameVersion());
        }
    }

    protected IsvDTO checkIsv(ApiRequestContext apiRequestContext) {
        ApiRequest apiRequest = apiRequestContext.getApiRequest();
        IsvDTO isv = isvManager.getIsv(apiRequest.getAppId());
        // 没有用户
        if (isv == null) {
            throw new ApiException(ErrorEnum.ISV_INVALID_APP_ID, apiRequestContext.getLocale());
        }
        // 禁止访问
        if (isv.getStatus() == null || isv.getStatus() == StatusEnum.DISABLE.getValue()) {
            throw new ApiException(ErrorEnum.ISV_ACCESS_FORBIDDEN, apiRequestContext.getLocale());
        }

        // 有效期
        if (isv.getStartExpirationTime() != null && LocalDateTime.now().isBefore(isv.getStartExpirationTime())) {
            throw new ApiException(ErrorEnum.ISV_INVALID_EXPIRATION_TIME, apiRequestContext.getLocale());
        }
        if (isv.getEndExpirationTime() != null && LocalDateTime.now().isAfter(isv.getEndExpirationTime())) {
            throw new ApiException(ErrorEnum.ISV_INVALID_EXPIRATION_TIME, apiRequestContext.getLocale());
        }
        return isv;
    }

    protected void checkSign(ApiRequestContext apiRequestContext, IsvDTO isv) {
        ApiRequest apiRequest = apiRequestContext.getApiRequest();
        String clientSign = apiRequest.getSign();
        if (ObjectUtils.isEmpty(clientSign)) {
            throw new ApiException(ErrorEnum.ISV_MISSING_SIGNATURE, apiRequestContext.getLocale(),
                    apiRequest.takeNameVersion(), apiConfig.getSignName());
        }
        // ISV上传的公钥
        String publicKey = secretManager.getIsvPublicKey(isv.getId());
        if (ObjectUtils.isEmpty(publicKey)) {
            throw new ApiException(ErrorEnum.ISV_MISSING_SIGNATURE_CONFIG, apiRequestContext.getLocale(),
                    apiRequest.takeNameVersion());
        }
        // 错误的sign
        if (!signer.checkSign(apiRequestContext, publicKey)) {
            throw new ApiException(ErrorEnum.ISV_INVALID_SIGNATURE, apiRequestContext.getLocale(),
                    apiRequest.takeNameVersion());
        }
    }


    protected void checkFormat(ApiRequestContext apiRequestContext) {
        ApiRequest apiRequest = apiRequestContext.getApiRequest();
        String format = apiRequest.getFormat();
        if (ObjectUtils.isEmpty(format)) {
            return;
        }
        if (RequestFormatEnum.of(format) == RequestFormatEnum.NONE) {
            throw new ApiException(ErrorEnum.ISV_INVALID_FORMAT, apiRequestContext.getLocale(),
                    apiRequest.takeNameVersion(), format);
        }
    }


    /**
     * 校验token
     *
     * @param apiRequestContext 参数
     */
    protected void checkToken(ApiRequestContext apiRequestContext, ApiInfoDTO apiInfoDTO) {
        Integer isNeedToken = apiInfoDTO.getIsNeedToken();
        if (YesOrNoEnum.of(isNeedToken) == YesOrNoEnum.NO) {
            return;
        }
        // 这里做校验token操作
        String appAuthToken = apiRequestContext.getApiRequest().getAppAuthToken();
        if (StringUtils.isBlank(appAuthToken)) {
            throw new ApiException(ErrorEnum.AOP_INVALID_AUTH_TOKEN, apiRequestContext.getLocale());
        }
    }

    @PostConstruct
    public void init() {
        dateTimeFormatter = DateTimeFormatter.ofPattern(apiConfig.getTimestampPattern());
    }

}
