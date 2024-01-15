package com.hpl.chat.chatgpt.session;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hpl.chat.chatgpt.domain.chat.ChatCompletionRequest;
import com.hpl.chat.chatgpt.domain.chat.ChatCompletionResponse;
import com.hpl.chat.chatgpt.domain.qa.QACompletionRequest;
import com.hpl.chat.chatgpt.domain.qa.QACompletionResponse;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.util.concurrent.CompletableFuture;

/**
 * OpenAi接口
 * @Author: huangpenglong
 * @Date: 2024/1/14 22:04
 */
public interface OpenAiSession {
    /**
     * 文本问答
     * @param question
     * @return
     */
    QACompletionResponse completions(String question);

    /**
     * 文本问答
     * @param qaCompletionRequest
     * @return
     */
    QACompletionResponse completions(QACompletionRequest qaCompletionRequest);

    /**
     * 文本问答 流式
     * @param qaCompletionRequest
     * @param eventSourceListener
     * @return
     * @throws JsonProcessingException
     */
    EventSource completionsStream(QACompletionRequest qaCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException;

    /**
     * 问答模型 ChatGPT流式问答
     * @param apiHostByUser
     * @param apiKeyByUser
     * @param chatCompletionRequest
     * @param eventSourceListener
     * @return
     * @throws JsonProcessingException
     */
    public EventSource chatCompletionsStream(String apiHostByUser, String apiKeyByUser, ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException;

    /**
     * 问答模型 ChatGPT流式问答
     * @param chatCompletionRequest
     * @param eventSourceListener
     * @return
     * @throws JsonProcessingException
     */
    EventSource chatCompletionsStream(ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException;

    /**
     * 问答模型 ChatGPT 非流式问答
     * @param chatCompletionRequest
     * @return
     * @throws InterruptedException
     * @throws JsonProcessingException
     */
    CompletableFuture<String> chatCompletionsFuture(ChatCompletionRequest chatCompletionRequest) throws InterruptedException, JsonProcessingException;

    /**
     * 问答模型 ChatGPT 非流式问答
     * @param chatCompletionRequest
     * @return
     */
    ChatCompletionResponse chatCompletions(ChatCompletionRequest chatCompletionRequest);
}
