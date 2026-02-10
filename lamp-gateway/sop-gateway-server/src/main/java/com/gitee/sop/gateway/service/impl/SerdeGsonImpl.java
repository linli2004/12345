package com.gitee.sop.gateway.service.impl;

import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.alibaba.nacos.shaded.com.google.gson.GsonBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 序列化/反序列化 gson实现
 *
 * @author 六如
 */
public class SerdeGsonImpl extends SerdeImpl {

    Gson gson;

    @Override
    public String toJson(Object object) {
        return gson.toJson(object);
    }

    @Override
    public Map<String, Object> parseJson(String json) {
        return gson.fromJson(json, LinkedHashMap.class);
    }


    @Override
    protected void doInit() {
        gson = new GsonBuilder()
                .setDateFormat(dateFormat)
                .create();
    }


}
