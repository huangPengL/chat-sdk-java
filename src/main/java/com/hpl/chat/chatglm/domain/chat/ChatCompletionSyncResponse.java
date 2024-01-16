package com.hpl.chat.chatglm.domain.chat;

import lombok.Data;

import java.util.List;

/**
 * @Author: huangpenglong
 * @Date: 2024/1/16 23:20
 */
@Data
public class ChatCompletionSyncResponse {

    private Integer code;
    private String msg;
    private Boolean success;
    private ChatGLMData data;

    @Data
    public static class ChatGLMData {
        private List<Choice> choices;
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

    @Data
    public static class Choice {
        private String role;
        private String content;
    }

}
