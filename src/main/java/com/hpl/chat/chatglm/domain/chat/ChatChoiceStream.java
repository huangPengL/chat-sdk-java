package com.hpl.chat.chatglm.domain.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: huangpenglong
 * @Date: 2024/1/17 15:24
 */
@Data
public class ChatChoiceStream implements Serializable {

    private long index;

    private Message delta;

    @JsonProperty("finish_reason")
    private String finishReason;
}
