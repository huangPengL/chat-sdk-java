package com.hpl.chat.chatgpt.common;

/**
 * @Author: huangpenglong
 * @Date: 2024/1/14 21:10
 */
public class Constants {
    public static final String URI_V1_COMPLETIONS = "v1/completions";
    public static final String URI_V1_CHAT_COMPLETIONS = "v1/chat/completions";

    public static final String DEFAULT_HOST = "https://api.openai.com/";

    public static final String STREAM_SIGNAL_DONE = "[DONE]";

    public static final String SIGNAL_STOP = "stop";


    public enum Role {
        /**
         *
         */
        SYSTEM("system"),
        USER("user"),
        ASSISTANT("assistant"),
        ;

        private final String code;

        Role(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

    }
}
