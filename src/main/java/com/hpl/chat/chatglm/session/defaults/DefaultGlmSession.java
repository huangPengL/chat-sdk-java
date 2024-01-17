package com.hpl.chat.chatglm.session.defaults;

import cn.hutool.http.ContentType;
import com.alibaba.fastjson.JSON;
import com.hpl.chat.chatglm.IGlmApi;
import com.hpl.chat.chatglm.common.Constants;
import com.hpl.chat.chatglm.domain.chat.*;
import com.hpl.chat.chatglm.session.Configuration;
import com.hpl.chat.chatglm.session.GlmSession;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @Author: huangpenglong
 * @Date: 2024/1/17 13:00
 */
public class DefaultGlmSession implements GlmSession {

    private final Configuration configuration;

    private final IGlmApi glmApi;

    private final EventSource.Factory factory;

    public DefaultGlmSession(Configuration configuration) {
        this.configuration = configuration;
        this.glmApi = configuration.getGlmApi();
        this.factory = configuration.createRequestFactory();
    }

    @Override
    public EventSource chatCompletionsStream(ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener){

        if(!chatCompletionRequest.isStream()){
            throw new RuntimeException("Streaming chat must require the parameter stream to be true.");
        }

        String url = Constants.DEFAULT_HOST.concat(Constants.URI_V4_COMPLETIONS);

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(
                        MediaType.parse(ContentType.JSON.getValue()),
                        chatCompletionRequest.toString()))
                .build();
        return factory.newEventSource(request, eventSourceListener);
    }

    @Override
    public CompletableFuture<String> chatCompletionsFuture(ChatCompletionRequest chatCompletionRequest) throws InterruptedException {
        CompletableFuture<String> future = new CompletableFuture<>();

        if(!chatCompletionRequest.isStream()){
            future.completeExceptionally(new RuntimeException("Streaming chat must require the parameter stream to be true."));
            return future;
        }

        // 收集结果
        StringBuilder dataCollect = new StringBuilder();

        this.chatCompletionsStream(chatCompletionRequest, new EventSourceListener() {
            @Override
            public void onEvent(@NotNull EventSource eventSource, String id, String type, String data) {
                if(Constants.STREAM_SIGNAL_DONE.equals(data)){
                    onClosed(eventSource);
                    return;
                }

                ChatCompletionStreamResponse response = JSON.parseObject(data, ChatCompletionStreamResponse.class);
                List<ChatChoiceStream> choices = response.getChoices();
                for (ChatChoiceStream chatChoice : choices) {
                    Message delta = chatChoice.getDelta();

                    // 发送信息
                    try {
                        dataCollect.append(delta.getContent());
                    } catch (Exception e) {
                        future.completeExceptionally(new RuntimeException("Request closed before completion"));
                    }

                }
            }

            @Override
            public void onClosed(@NotNull EventSource eventSource) {
                future.complete(dataCollect.toString());

            }

            @Override
            public void onFailure(@NotNull EventSource eventSource, Throwable t, Response response) {
                future.completeExceptionally(new RuntimeException("Request closed before completion"));
            }
        });

        return future;
    }

    @Override
    public ChatCompletionResponse chatCompletions(ChatCompletionRequest chatCompletionRequest) throws IOException {
        if(chatCompletionRequest.isStream()){
            throw new RuntimeException("Normal chat must require the parameter stream to be false.");
        }

        return this.glmApi.completions(chatCompletionRequest).blockingGet();
    }
}
