package com.gitee.sop.gateway.util;

import com.gitee.sop.gateway.request.ApiRequest;
import com.gitee.sop.gateway.request.ApiRequestContext;
import com.gitee.sop.gateway.request.RequestFormatEnum;
import com.gitee.sop.support.dto.FileData;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author 六如
 */
public class ResponseUtil {

    public static void writerFile(FileData fileData, HttpServletResponse response) throws IOException {
        InputStream inputStream = fileData.getInputStream();
        String contentType = fileData.getContentType();
        if (contentType != null) {
            response.setContentType(contentType);
        }
        response.setHeader("Content-disposition", "attachment; filename=" + fileData.getOriginalFilename());
        response.setHeader("Content-Length", String.valueOf(fileData.getSize()));
        IOUtils.copy(inputStream, response.getOutputStream());
    }

    public static void writerText(ApiRequestContext apiRequestContext, Object apiResponse, HttpServletResponse response) throws IOException {
        ApiRequest apiRequest = apiRequestContext.getApiRequest();
        String charset = apiRequest.getCharset();
        response.setCharacterEncoding(charset);
        String format = apiRequest.getFormat();
        if (RequestFormatEnum.of(format) == RequestFormatEnum.XML) {
            response.setContentType(MediaType.APPLICATION_XML_VALUE);
            String xml = XmlUtil.toXml(apiResponse);
            response.getWriter().write(xml);
        } else {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            String json = JsonUtil.toJSONString(apiResponse);
            response.getWriter().write(json);
        }
    }


}
