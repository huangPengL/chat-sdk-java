package com.hpl.chat.chatglm.domain.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: huangpenglong
 * @Date: 2024/1/16 23:18
 */
@Getter
@AllArgsConstructor
public enum Model {
    /**
     *
     */

    @Deprecated
    CHATGLM_6B_SSE("chatGLM_6b_SSE", "ChatGLM-6B 测试模型"),
    @Deprecated
    CHATGLM_LITE("chatglm_lite", "轻量版模型，适用对推理速度和成本敏感的场景"),
    @Deprecated
    CHATGLM_LITE_32K("chatglm_lite_32k", "标准版模型，适用兼顾效果和成本的场景"),
    @Deprecated
    CHATGLM_STD("chatglm_std", "适用于对知识量、推理能力、创造力要求较高的场景"),
    @Deprecated
    CHATGLM_PRO("chatglm_pro", "适用于对知识量、推理能力、创造力要求较高的场景"),
    /** 智谱AI最新模型 */
    CHATGLM_TURBO("chatglm_turbo", "适用于对知识量、推理能力、创造力要求较高的场景"),
    
    GLM_3_TURBO("glm-3-turbo", "glm-3-turbo"),

    GLM_4("glm-4", "glm-4")

    ;
    private final String code;
    private final String info;
}
