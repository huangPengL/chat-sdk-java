package com.hpl.chat.chatglm.domain.chat;

import lombok.Data;

/**
 * @Author: huangpenglong
 * @Date: 2024/1/16 23:20
 */

@Data
public class ChatCompletionResponse {

    private String data;
    private String meta;

    @Data
    public static class Meta {
        private String task_status;
        private Usage usage;
        private String task_id;
        private String request_id;
    }

    @Data
    public static class Usage {
        private int completion_tokens;
        private int prompt_tokens;
        private int total_tokens;
    }

}
