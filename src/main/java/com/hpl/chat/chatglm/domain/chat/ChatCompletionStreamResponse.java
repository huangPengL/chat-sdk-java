package com.hpl.chat.chatglm.domain.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: huangpenglong
 * @Date: 2024/1/16 23:20
 */

@Data
public class ChatCompletionStreamResponse {

    /**
     * ID
     */
    private String id;

    /**
     * 模型
     */
    private String model;
    /**
     * 对话
     */
    private List<ChatChoiceStream> choices;
    /**
     * 创建
     */
    private long created;
    /**
     * 耗材
     */
    private Usage usage;

    @JsonProperty("request_id")
    private String requestId;

}
