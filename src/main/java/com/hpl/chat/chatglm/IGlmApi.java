package com.hpl.chat.chatglm;

import com.hpl.chat.chatglm.common.Constants;

import com.hpl.chat.chatglm.domain.chat.ChatCompletionRequest;

import com.hpl.chat.chatglm.domain.chat.ChatCompletionResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @Author: huangpenglong
 * @Date: 2024/1/16 23:13
 */
public interface IGlmApi {


    /**
     * glm问答
     * @param chatCompletionRequest
     * @return
     */
    @POST(Constants.URI_V4_COMPLETIONS)
    Single<ChatCompletionResponse> completions(@Body ChatCompletionRequest chatCompletionRequest);
}
