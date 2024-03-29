package com.hpl.chat.chatgpt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hpl.chat.chatgpt.common.Constants;
import com.hpl.chat.chatgpt.domain.chat.ChatChoice;
import com.hpl.chat.chatgpt.domain.chat.ChatCompletionRequest;
import com.hpl.chat.chatgpt.domain.chat.ChatCompletionResponse;
import com.hpl.chat.chatgpt.domain.chat.Message;
import com.hpl.chat.chatgpt.session.Configuration;
import com.hpl.chat.chatgpt.session.OpenAiSession;
import com.hpl.chat.chatgpt.session.defaults.DefaultOpenAiSessionFactory;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * @Author: huangpenglong
 * @Date: 2024/1/15 16:11
 */

@Slf4j
public class ApiTest {

    public static final String OPENAI_HOST = "https://api.openai.com/";
    public static final String OPENAI_APIKEY = "sess-ebMaNZHqCuKrxVMQnjgypZa3u9soT91W9LRfQ1Vt";

    private OpenAiSession openAiSession;

    /**
     * 创建配置类、工厂模式创建并开启会话
     */
    @Before
    public void testOpenAiSessionFactory(){
        Configuration configuration = Configuration.builder()
                .apiKey(OPENAI_APIKEY)
                .build();
        DefaultOpenAiSessionFactory defaultOpenAiSessionFactory = new DefaultOpenAiSessionFactory(configuration);
        this.openAiSession = defaultOpenAiSessionFactory.openSession();

    }

    /**
     * 聊天接口-流式输出
     * @throws InterruptedException
     * @throws JsonProcessingException
     */
    @Test
    public void testChatCompletionsStream() throws InterruptedException, JsonProcessingException {
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .stream(true)
                .messages(Collections.singletonList(Message.builder().role(Constants.Role.USER).content("你好").build()))
                .build();
        EventSource eventSource = this.openAiSession.chatCompletionsStream(chatCompletionRequest, new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, String id, String type, String data) {
                log.info("测试结果 id:{} type:{} data:{}", id, type, data);
            }

            @Override
            public void onFailure(EventSource eventSource, Throwable t, Response response) {
                log.error("失败 code:{} message:{}", response.code(), response.message());
            }
        });
        // 等待
        new CountDownLatch(1).await();
    }

    /**
     * 聊天接口-普通输出
     */
    @Test
    public void testChatCompletions(){
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .messages(Collections.singletonList(Message.builder().role(Constants.Role.USER).content("你好").build()))
                .build();
        ChatCompletionResponse chatCompletionResponse = this.openAiSession.chatCompletions(chatCompletionRequest);
        if(chatCompletionResponse != null){
            for(ChatChoice choice: chatCompletionResponse.getChoices()){
                log.info("测试结果：{}", choice.getMessage().getContent());
            }
        }
    }

    /**
     * 聊天接口-异步获取
     * @throws InterruptedException
     * @throws JsonProcessingException
     * @throws ExecutionException
     */
    @Test
    public void testChatCompletionsFuture() throws InterruptedException, JsonProcessingException, ExecutionException {
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .stream(true)
                .messages(Collections.singletonList(Message.builder().role(Constants.Role.USER).content("你好").build()))
                .build();

        log.info("测试结果：{}", this.openAiSession.chatCompletionsFuture(chatCompletionRequest).get());
    }

}
