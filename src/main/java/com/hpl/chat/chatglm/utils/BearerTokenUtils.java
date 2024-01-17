package com.hpl.chat.chatglm.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: huangpenglong
 * @Date: 2024/1/17 13:13
 */
@Slf4j
public class BearerTokenUtils {

    /**
     * 缓存过期时间，30分钟
      */
    private static final long EXPIRE_MILLIS = 30 * 60 * 1000L;

    /**
     * 缓存服务
     */
    public static final Cache<String, String> CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(EXPIRE_MILLIS - (60 * 1000L), TimeUnit.MILLISECONDS)
            .build();

    private BearerTokenUtils(){}

    /**
     * 对 ApiKey 进行签名
     *
     * @param apiKey    登录创建 ApiKey <a href="https://open.bigmodel.cn/usercenter/apikeys">apikeys</a>
     * @param apiSecret apiKey的后半部分 828902ec516c45307619708d3e780ae1.w5eKiLvhnLP8MtIf 取 w5eKiLvhnLP8MtIf 使用
     * @return Token
     */
    public static String getToken(String apiKey, String apiSecret) {
        // 缓存Token
        String token = CACHE.getIfPresent(apiKey);
        if (null != token){
            return token;
        }
        // 创建Token
        Map<String, Object> payload = ImmutableMap.of(
                "api_key", apiKey,
                "exp", System.currentTimeMillis() + EXPIRE_MILLIS,
                "timestamp", Calendar.getInstance().getTimeInMillis()
        );

        Map<String, Object> headerClaims = ImmutableMap.of(
                "alg", "HS256",
                "sign_type", "SIGN"
        );

        token = JWT.create()
                .withPayload(payload)
                .withHeader(headerClaims)
                .sign(Algorithm.HMAC256(apiSecret.getBytes(StandardCharsets.UTF_8)));
        CACHE.put(apiKey, token);
        return token;
    }

}
