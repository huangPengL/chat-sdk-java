package com.hpl.chat.chatglm.session;

import com.hpl.chat.chatglm.IGlmApi;
import com.hpl.chat.chatglm.common.Constants;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;

/**
 * @Author: huangpenglong
 * @Date: 2024/1/16 23:25
 */

@Builder
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {

    /**
     * 智普Ai ChatGlM 请求地址
     */
    @Builder.Default
    private String apiHost = Constants.DEFAULT_HOST;

    /**
     * 智普Ai <a href="https://open.bigmodel.cn/usercenter/apikeys">...</a>
     *      - apiSecretKey = {apiKey}.{apiSecret}
     */
    private String apiSecretKey;

    public void setApiSecretKey(String apiSecretKey) {
        this.apiSecretKey = apiSecretKey;
        String[] arrStr = apiSecretKey.split("\\.");
        if (arrStr.length != 2) {
            throw new RuntimeException("invalid apiSecretKey");
        }
        this.apiKey = arrStr[0];
        this.apiSecret = arrStr[1];
    }

    private String apiKey;

    private String apiSecret;

    private IGlmApi glmApi;

    private OkHttpClient okHttpClient;

    public EventSource.Factory createRequestFactory() {
        return EventSources.createFactory(okHttpClient);
    }

    /**
     * OkHttp 配置信息
     */
    @Builder.Default
    private HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;


}
