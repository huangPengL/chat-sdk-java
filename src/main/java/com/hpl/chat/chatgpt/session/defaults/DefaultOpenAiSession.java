package com.hpl.chat.chatgpt.session.defaults;

import cn.hutool.http.ContentType;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpl.chat.chatgpt.IOpenAiApi;
import com.hpl.chat.chatgpt.common.Constants;
import com.hpl.chat.chatgpt.domain.chat.ChatChoice;
import com.hpl.chat.chatgpt.domain.chat.ChatCompletionRequest;
import com.hpl.chat.chatgpt.domain.chat.ChatCompletionResponse;
import com.hpl.chat.chatgpt.domain.chat.Message;
import com.hpl.chat.chatgpt.domain.qa.QACompletionRequest;
import com.hpl.chat.chatgpt.domain.qa.QACompletionResponse;
import com.hpl.chat.chatgpt.session.Configuration;
import com.hpl.chat.chatgpt.session.OpenAiSession;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @Author: huangpenglong
 * @Date: 2024/1/14 22:49
 */
public class DefaultOpenAiSession implements OpenAiSession {

    /**
     * 配置信息
     */
    private final Configuration configuration;

    /**
     * OpenAI 接口
     */
    private final IOpenAiApi openAiApi;
    /**
     * 工厂事件
     */
    private final EventSource.Factory factory;

    public DefaultOpenAiSession(Configuration configuration) {
        this.configuration = configuration;
        this.openAiApi = configuration.getOpenAiApi();
        this.factory = configuration.createRequestFactory();
    }

    @Override
    public QACompletionResponse completions(String question) {
        QACompletionRequest request = QACompletionRequest
                .builder()
                .prompt(question)
                .build();
        return this.openAiApi.completions(request).blockingGet();
    }

    @Override
    public QACompletionResponse completions(QACompletionRequest qaCompletionRequest) {
        return this.openAiApi.completions(qaCompletionRequest).blockingGet();
    }

    @Override
    public EventSource completionsStream(QACompletionRequest qaCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException{

        // 参数校验
        if(!qaCompletionRequest.isStream()){
            throw new RuntimeException("Streaming chat must require the parameter stream to be true.");
        }
        // 构建请求
        Request request = new Request.Builder()
                .url(configuration.getApiHost().concat(Constants.URI_V1_COMPLETIONS))
                .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), new ObjectMapper().writeValueAsString(qaCompletionRequest)))
                .build();

        return factory.newEventSource(request, eventSourceListener);
    }

    @Override
    public EventSource chatCompletionsStream(String apiHostByUser, String apiKeyByUser, ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException {

        // 参数校验
        if(!chatCompletionRequest.isStream()){
            throw new RuntimeException("Streaming chat must require the parameter stream to be true.");
        }

        // 设置host 和 apiKey
        String host = apiHostByUser == null ? configuration.getApiHost() : apiHostByUser;
        String apiKey = apiKeyByUser == null ? configuration.getApiKey() : apiKeyByUser;

        // 构造请求
        Request request = new Request.Builder()
                .url(host.concat(Constants.URI_V1_CHAT_COMPLETIONS))
                .addHeader("apiKey", apiKey)
                .post(RequestBody.create(
                        MediaType.parse(ContentType.JSON.getValue()),
                        new ObjectMapper().writeValueAsString(chatCompletionRequest)))
                .build();
        return factory.newEventSource(request, eventSourceListener);
    }

    @Override
    public EventSource chatCompletionsStream(ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException {
        return this.chatCompletionsStream(null, null, chatCompletionRequest, eventSourceListener);
    }

    @Override
    public CompletableFuture<String> chatCompletionsFuture(ChatCompletionRequest chatCompletionRequest) throws InterruptedException, JsonProcessingException {
        CompletableFuture<String> future = new CompletableFuture<>();

        if(!chatCompletionRequest.isStream()){
            future.completeExceptionally(new RuntimeException("Streaming chat must require the parameter stream to be true."));
            return future;
        }

        // 收集结果
        StringBuilder dataCollect = new StringBuilder();
        this.chatCompletionsStream(chatCompletionRequest, new EventSourceListener() {

            @Override
            public void onEvent(EventSource eventSource, String id, String type, String data) {
                if(Constants.STREAM_SIGNAL_DONE.equals(data)){
                    onClosed(eventSource);
                    future.complete(dataCollect.toString());
                    return;
                }

                ChatCompletionResponse chatCompletionResponse = JSON.parseObject(data, ChatCompletionResponse.class);
                List<ChatChoice> choices = chatCompletionResponse.getChoices();
                for (ChatChoice chatChoice : choices) {
                    Message delta = chatChoice.getDelta();
                    if (Constants.Role.ASSISTANT.getCode().equals(delta.getRole())){
                        continue;
                    }

                    // 应答完成
                    String finishReason = chatChoice.getFinishReason();
                    if (Constants.SIGNAL_STOP.equalsIgnoreCase(finishReason)) {
                        onClosed(eventSource);
                        return;
                    }

                    // 发送信息
                    try {
                        dataCollect.append(delta.getContent());
                    } catch (Exception e) {
                        future.completeExceptionally(new RuntimeException("Request closed before completion"));
                    }

                }
            }

            @Override
            public void onClosed(EventSource eventSource) {
                future.complete(dataCollect.toString());

            }

            @Override
            public void onFailure(EventSource eventSource, Throwable t, Response response) {
                future.completeExceptionally(new RuntimeException("Request closed before completion"));
            }
        });
        return future;
    }

    @Override
    public ChatCompletionResponse chatCompletions(ChatCompletionRequest chatCompletionRequest) {
        return this.openAiApi.completions(chatCompletionRequest).blockingGet();
    }
}
