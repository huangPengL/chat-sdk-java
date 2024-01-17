package com.hpl.chat.chatglm.session;

import com.hpl.chat.chatglm.domain.chat.ChatCompletionRequest;
import com.hpl.chat.chatglm.domain.chat.ChatCompletionResponse;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * @Author: huangpenglong
 * @Date: 2024/1/16 23:23
 */
public interface GlmSession {
    /**
     * sse输送流式回复
     * @param chatCompletionRequest
     * @param eventSourceListener
     * @return
     */
    EventSource chatCompletionsStream(ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener);


    /**
     * 异步获取完整的流式回复
     * @param chatCompletionRequest
     * @return
     * @throws InterruptedException
     */
    CompletableFuture<String> chatCompletionsFuture(ChatCompletionRequest chatCompletionRequest) throws InterruptedException;

    /**
     * 普通输出
     * @param chatCompletionRequest
     * @return
     * @throws IOException
     */
    ChatCompletionResponse chatCompletions(ChatCompletionRequest chatCompletionRequest) throws IOException;
}
