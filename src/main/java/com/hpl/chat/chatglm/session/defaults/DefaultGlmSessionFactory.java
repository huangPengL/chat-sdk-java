package com.hpl.chat.chatglm.session.defaults;

import com.hpl.chat.chatglm.IGlmApi;
import com.hpl.chat.chatglm.interceptor.GlmInterceptor;
import com.hpl.chat.chatglm.session.Configuration;
import com.hpl.chat.chatglm.session.GlmSession;
import com.hpl.chat.chatglm.session.GlmSessionFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * @Author: huangpenglong
 * @Date: 2024/1/17 13:01
 */
public class DefaultGlmSessionFactory implements GlmSessionFactory {
    private final Configuration configuration;

    public DefaultGlmSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }


    @Override
    public GlmSession openSession() {
        // 1. 日志配置
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(configuration.getLevel());

        // 2. 创建 Http 客户端
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new GlmInterceptor(configuration))
                .connectTimeout(450, TimeUnit.SECONDS)
                .writeTimeout(450, TimeUnit.SECONDS)
                .readTimeout(450, TimeUnit.SECONDS)
                .build();

        // 3. 创建 GlmApi 对象
        IGlmApi glmApi = new Retrofit.Builder()
                .baseUrl(configuration.getApiHost())
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build().create(IGlmApi.class);

        // 4. 配置Configuration
        configuration.setOkHttpClient(okHttpClient);
        configuration.setGlmApi(glmApi);

        return new DefaultGlmSession(configuration);
    }
}
