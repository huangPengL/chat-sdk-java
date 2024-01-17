package com.hpl.chat.chatgpt.interceptor;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * 自定义拦截器,设置apiKey
 * @Author: huangpenglong
 * @Date: 2024/1/14 22:28
 */
public class OpenAiInterceptor implements Interceptor {

    /**
     *
     */
    private final String defaultApiKey;

    public OpenAiInterceptor(String apiKey) {
        this.defaultApiKey = apiKey;
    }

    @Override
    public @NotNull Response intercept(Chain chain) throws IOException {

        Request originalRequest = chain.request();

        String apiKeyBefore = originalRequest.header("apiKey");
        String apiKey = apiKeyBefore == null ? this.defaultApiKey :apiKeyBefore;

        // 构建新的 Request
        Request request = originalRequest.newBuilder()
                .url(originalRequest.url())
                .header(Header.AUTHORIZATION.getValue(), "Bearer " + apiKey)
                .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                .method(originalRequest.method(), originalRequest.body())
                .build();

        return chain.proceed(request);
    }
}
