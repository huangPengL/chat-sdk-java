package com.hpl.chat.chatglm.domain.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: huangpenglong
 * @Date: 2024/1/17 13:46
 */
@Getter
@AllArgsConstructor
public enum EventType {

    /**
     *
     */
    ADD("add", "增量"),
    FINISH("finish", "结束"),
    ERROR("error", "错误"),
    INTERRUPTED("interrupted", "中断"),

    ;
    private final String code;
    private final String info;

}
