package com.hpl.chat.chatgpt.domain.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hpl.chat.chatgpt.domain.other.Usage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: huangpenglong
 * @Date: 2024/1/14 21:17
 */
@Data
public class ChatCompletionResponse implements Serializable {

    /**
     * ID
     */
    private String id;
    /**
     * 对象
     */
    private String object;
    /**
     * 模型
     */
    private String model;
    /**
     * 对话
     */
    private List<ChatChoice> choices;
    /**
     * 创建
     */
    private long created;
    /**
     * 耗材
     */
    private Usage usage;
    /**
     * 该指纹代表模型运行时使用的后端配置。
     * <a href="https://platform.openai.com/docs/api-reference/chat">...</a>
     */
    @JsonProperty("system_fingerprint")
    private String systemFingerprint;
}
