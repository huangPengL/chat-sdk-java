package com.hpl.chat.chatglm.common;

/**
 * @Author: huangpenglong
 * @Date: 2024/1/16 23:14
 */
public class Constants {

    @Deprecated
    public static final String URI_V3_COMPLETIONS = "api/paas/v3/model-api/{model}/sse-invoke";
    @Deprecated
    public static final String URI_V3_COMPLETIONS_SYNC = "api/paas/v3/model-api/{model}/invoke";

    public static final String URI_V4_COMPLETIONS = "api/paas/v4/chat/completions";

    public static final String DEFAULT_HOST = "https://open.bigmodel.cn/";
    public static final String SIGNAL_STOP = "stop";
    public static final String STREAM_SIGNAL_DONE = "[DONE]";

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
