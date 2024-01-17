package com.hpl.chat.chatglm;

import com.google.common.collect.ImmutableList;
import com.hpl.chat.chatglm.common.Constants;
import com.hpl.chat.chatglm.domain.chat.*;
import com.hpl.chat.chatglm.session.Configuration;
import com.hpl.chat.chatglm.session.GlmSession;
import com.hpl.chat.chatglm.session.defaults.DefaultGlmSessionFactory;
import lombok.extern.slf4j.Slf4j;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * @Author: huangpenglong
 * @Date: 2024/1/17 13:49
 */

@Slf4j
public class ApiTest {

    public static final String API_SECRET_KEY = "c3713146757596d58e65e0f7ad882ab6.7lEnzSH949MePSCJ";

    private GlmSession glmSession;

    /**
     * 创建配置类、工厂模式创建并开启会话
     */
    @Before
    public void testGlmSessionFactory(){
        Configuration configuration = Configuration.builder().build();
        configuration.setApiSecretKey(API_SECRET_KEY);

        this.glmSession = new DefaultGlmSessionFactory(configuration).openSession();
    }

    /**
     * 默认测试prompt
     * @return
     */
    private List<ChatCompletionRequest.Message> getTestPrompts(){
        String systemContent = "你是一名喝醉酒的流浪汉，性格幽默，喜欢唠嗑。";
        String userContent = "你好，介绍自己";

        return ImmutableList.of(
                ChatCompletionRequest.Message.builder()
                        .role(Constants.Role.SYSTEM.getCode())
                        .content(systemContent)
                        .build(),

                ChatCompletionRequest.Message.builder()
                        .role(Constants.Role.USER.getCode())
                        .content(userContent)
                        .build()
        );
    }

    /**
     * 聊天接口-流式输出
     * @throws InterruptedException
     */
    @Test
    public void testChatCompletionsStream() throws InterruptedException {

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(Model.GLM_3_TURBO.getCode())
                .messages(getTestPrompts())
                .build();

        this.glmSession.chatCompletionsStream(request, new EventSourceListener() {
            @Override
            public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
                log.info("测试结果 id:{} type:{} data:{}", id, type, data);
            }

            @Override
            public void onClosed(@NotNull EventSource eventSource) {
                log.info("对话完成");
            }
        });

        new CountDownLatch(1).await();
    }

    /**
     * 聊天接口-异步获取
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Test
    public void testChatCompletionsFuture() throws InterruptedException, ExecutionException {
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(Model.GLM_4.getCode())
                .messages(getTestPrompts())
                .build();
        CompletableFuture<String> future = this.glmSession.chatCompletionsFuture(request);

        log.info("测试结果：{}", future.get());
    }

    /**
     * 聊天接口-普通输出
     * @throws IOException
     */
    @Test
    public void testChatCompletions() throws IOException {
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .stream(false)
                .model(Model.GLM_4.getCode())
                .messages(getTestPrompts())
                .build();

        ChatCompletionResponse response = this.glmSession.chatCompletions(request);
        log.info("测试结果：{}", response);
    }

}
