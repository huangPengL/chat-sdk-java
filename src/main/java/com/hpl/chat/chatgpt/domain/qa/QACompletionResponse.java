package com.hpl.chat.chatgpt.domain.qa;

import com.hpl.chat.chatgpt.domain.other.Choice;
import com.hpl.chat.chatgpt.domain.other.Usage;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: huangpenglong
 * @Date: 2024/1/14 20:28
 */
@Data
public class QACompletionResponse implements Serializable {

    /** ID */
    private String id;
    /** 对象 */
    private String object;
    /** 模型 */
    private String model;
    /** 对话 */
    private Choice[] choices;
    /** 创建 */
    private long created;
    /** 耗材 */
    private Usage usage;

}
