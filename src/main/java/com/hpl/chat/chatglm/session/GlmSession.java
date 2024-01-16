package com.hpl.chat.chatglm.session;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hpl.chat.chatglm.domain.chat.ChatCompletionRequest;
import com.hpl.chat.chatglm.domain.chat.ChatCompletionSyncResponse;
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
     * @throws JsonProcessingException
     */
    EventSource completions(ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException;


    /**
     * 异步获取完整的流式回复
     * @param chatCompletionRequest
     * @return
     * @throws InterruptedException
     */
    CompletableFuture<String> completions(ChatCompletionRequest chatCompletionRequest) throws InterruptedException;

    /**
     * 普通输出
     * @param chatCompletionRequest
     * @return
     * @throws IOException
     */
    ChatCompletionSyncResponse completionsSync(ChatCompletionRequest chatCompletionRequest) throws IOException;
}
