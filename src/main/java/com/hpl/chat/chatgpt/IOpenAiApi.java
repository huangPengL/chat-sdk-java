package com.hpl.chat.chatgpt;

import com.hpl.chat.chatgpt.common.Constant;
import com.hpl.chat.chatgpt.domain.chat.ChatCompletionRequest;
import com.hpl.chat.chatgpt.domain.chat.ChatCompletionResponse;
import com.hpl.chat.chatgpt.domain.qa.QACompletionRequest;
import com.hpl.chat.chatgpt.domain.qa.QACompletionResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Http请求OpenAi基础接口（非流式）
 * @Author: huangpenglong
 * @Date: 2024/1/14 20:28
 */
public interface IOpenAiApi {

    /**
     * 普通问答，GPT3
     * @param qaCompletionRequest
     * @return
     */
    @POST(Constant.URI_V1_COMPLETIONS)
    Single<QACompletionResponse> completions(@Body QACompletionRequest qaCompletionRequest);


    /**
     * ChatGPT问答
     * @param chatCompletionRequest
     * @return
     */
    @POST(Constant.URI_V1_CHAT_COMPLETIONS)
    Single<ChatCompletionResponse> completions(@Body ChatCompletionRequest chatCompletionRequest);
}
