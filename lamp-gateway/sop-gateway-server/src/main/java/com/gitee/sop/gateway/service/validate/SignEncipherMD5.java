package com.gitee.sop.gateway.service.validate;

import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author 六如
 */
public class SignEncipherMD5 implements SignEncipher {
    @Override
    public byte[] encrypt(String input, String secret) {
        String source = secret + input + secret;
        return DigestUtils.md5(source.getBytes(StandardCharsets.UTF_8));
    }
}
