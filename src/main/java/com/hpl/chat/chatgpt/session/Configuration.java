package com.hpl.chat.chatgpt.session;

import com.hpl.chat.chatglm.common.Constants;
import com.hpl.chat.chatgpt.IOpenAiApi;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;
import org.jetbrains.annotations.NotNull;

/**
 * @Author: huangpenglong
 * @Date: 2024/1/14 22:15
 */
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Configuration {

    private IOpenAiApi openAiApi;

    private OkHttpClient okHttpClient;

    @NotNull
    private String apiKey;

    @Builder.Default
    private String apiHost = Constants.DEFAULT_HOST;

    /**
     * 字段废弃，不在使用
     */
    @Getter
    @Deprecated
    private String authToken;

    public EventSource.Factory createRequestFactory() {
        return EventSources.createFactory(okHttpClient);
    }

}
