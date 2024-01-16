package com.hpl.chat.chatglm;

import com.hpl.chat.chatglm.common.Constants;

import com.hpl.chat.chatglm.domain.chat.ChatCompletionRequest;
import com.hpl.chat.chatglm.domain.chat.ChatCompletionResponse;
import com.hpl.chat.chatglm.domain.chat.ChatCompletionSyncResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @Author: huangpenglong
 * @Date: 2024/1/16 23:13
 */
public interface IGlmApi {

    /**
     * 异步响应（流式）
     * @param model
     * @param chatCompletionRequest
     * @return
     */
    @POST(Constants.URI_V3_COMPLETIONS)
    Single<ChatCompletionResponse> completions(@Path("model") String model, @Body ChatCompletionRequest chatCompletionRequest);


    /**
     * 同步响应
     * @param chatCompletionRequest
     * @return
     */
    @POST(Constants.URI_V3_COMPLETIONS_SYNC)
    Single<ChatCompletionSyncResponse> completions(@Body ChatCompletionRequest chatCompletionRequest);
}
