package com.hpl.chat.chatgpt.session;

/**
 * @Author: huangpenglong
 * @Date: 2024/1/14 22:12
 */
public interface OpenAiSessionFactory {

    /**
     * 创建session
     * @return
     */
    OpenAiSession openSession();
}
