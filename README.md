# 
# 🚀 chat-sdk-java


## 📖 简介
集成聊天接口的SDK。目前集成了以下：
- ChatGPT
- ChatGLM


## 🔰 快速开始

```xml
<groupId>com.hpl.chat</groupId>
    <artifactId>chat-sdk-java</artifactId>
<version>1.0</version>
```

对于所有聊天模型API，该SDK提供三种类型的输出模式。
- 流式（一个字一个字蹦出来）
- 普通（等待完整结果）
- 异步（调用流式，但需要收集完整结果才能被异步获取）

详见以下代码展示。


### ChatGPT
测试类com.hpl.chat.chatgpt.ApiTest
```java
@Slf4j
public class ApiTest {

    public static final String OPENAI_HOST = "https://api.openai.com/";
    public static final String OPENAI_APIKEY = "sess-1KoEcZRoN0u0BHPQxJZbFdoSjZ6A5tLY7RQI1aTi";

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
```


### ChatGLM
测试类com.hpl.chat.chatglm.ApiTest
```java
@Slf4j
public class ApiTest {

    public static final String API_SECRET_KEY = "c3713146757596d58e65e0f7ad882ab6.7lEnzSH949MePSCJ";

    private GlmSession glmSession;

    /**
     * 创建配置类、工厂模式创建并开启会话
     */
    @Before
    public void testGlmSessionFactory() {
        Configuration configuration = Configuration.builder().build();
        configuration.setApiSecretKey(API_SECRET_KEY);

        this.glmSession = new DefaultGlmSessionFactory(configuration).openSession();
    }

    /**
     * 默认测试prompt
     * @return
     */
    private List<ChatCompletionRequest.Message> getTestPrompts() {
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
```
