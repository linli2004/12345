package com.gitee.sop.gateway.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author 六如
 */
public class XmlUtil {

    private static final XmlMapper XML_MAPPER = new XmlMapper();


    public static String toXml(Object object) throws JsonProcessingException {
        return XML_MAPPER.writeValueAsString(object);
    }

}
