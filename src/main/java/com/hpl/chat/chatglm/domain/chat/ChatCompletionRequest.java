package com.hpl.chat.chatglm.domain.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: huangpenglong
 * @Date: 2024/1/16 23:18
 */
@Data
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatCompletionRequest {

    /**
     * 模型
     */
    @Builder.Default
    @JsonProperty("model")
    private String model = Model.CHATGLM_TURBO.getCode();

    /**
     * 请求ID
     */
    @JsonProperty("request_id")
    @Builder.Default
    private String requestId = String.format("hpl-%d", System.currentTimeMillis());
    /**
     * 控制温度【随机性】
     */
    @Builder.Default
    private float temperature = 0.8f;
    /**
     * 多样性控制；
     */
    @JsonProperty("top_p")
    @Builder.Default
    private float topP = 0.7f;
    /**
     * 输入给模型的会话信息
     * 用户输入的内容；role=user
     * 挟带历史的内容；role=assistant
     */
    private List<Message> messages;
    /**
     * 智普AI sse 固定参数 incremental = true 【增量返回】
     */
    @Builder.Default
    private boolean stream = true;

    /**
     * sseformat, 用于兼容解决sse增量模式okhttpsse截取data:后面空格问题, [data: hello]。只在增量模式下使用sseFormat。
     */
    @Builder.Default
    private String sseFormat = "data";


    @Override
    public String toString() {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("model", model);
        paramsMap.put("request_id", requestId);
        paramsMap.put("messages", messages);
        paramsMap.put("stream", stream);
        paramsMap.put("temperature", temperature);
        paramsMap.put("top_p", topP);
        paramsMap.put("sseFormat", sseFormat);
        try {
            return new ObjectMapper().writeValueAsString(paramsMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
