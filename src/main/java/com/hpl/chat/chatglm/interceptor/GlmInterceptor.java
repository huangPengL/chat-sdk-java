package com.hpl.chat.chatglm.interceptor;

import com.hpl.chat.chatglm.session.Configuration;
import com.hpl.chat.chatglm.utils.BearerTokenUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @Author: huangpenglong
 * @Date: 2024/1/17 13:05
 */
public class GlmInterceptor implements Interceptor {

    private final Configuration configuration;

    public GlmInterceptor(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public @NotNull Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request request = originalRequest.newBuilder()
                .url(originalRequest.url())
                .header("Authorization", "Bearer "
                        + BearerTokenUtils.getToken(configuration.getApiKey(), configuration.getApiSecret()))
                .build();

        return chain.proceed(request);
    }
}
