package com.hpl.chat.chatgpt.session.defaults;

import com.hpl.chat.chatgpt.IOpenAiApi;
import com.hpl.chat.chatgpt.interceptor.OpenAiInterceptor;
import com.hpl.chat.chatgpt.session.Configuration;
import com.hpl.chat.chatgpt.session.OpenAiSession;
import com.hpl.chat.chatgpt.session.OpenAiSessionFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 *
 * @Author: huangpenglong
 * @Date: 2024/1/14 22:14
 */
public class DefaultOpenAiSessionFactory implements OpenAiSessionFactory {
    /**
     * 配置信息
     */
    private final Configuration configuration;

    public DefaultOpenAiSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }


    @Override
    public OpenAiSession openSession() {
        // 1. 日志
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

        // 2. 创建 Http 客户端
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new OpenAiInterceptor(this.configuration.getApiKey()))
                .connectTimeout(450, TimeUnit.SECONDS)
                .writeTimeout(450, TimeUnit.SECONDS)
                .readTimeout(450, TimeUnit.SECONDS)
                .build();


        // 3. 创建 API 对象
        IOpenAiApi openAiApi = new Retrofit.Builder()
                .baseUrl(configuration.getApiHost())
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build().create(IOpenAiApi.class);

        // 4. 配置Configuration
        this.configuration.setOkHttpClient(client);
        this.configuration.setOpenAiApi(openAiApi);

        return new DefaultOpenAiSession(configuration);
    }
}
