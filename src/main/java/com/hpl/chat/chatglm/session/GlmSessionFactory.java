package com.hpl.chat.chatglm.session;

/**
 * @Author: huangpenglong
 * @Date: 2024/1/16 23:23
 */
public interface GlmSessionFactory {

    /**
     * 创建GlmSession
     * @return
     */
    GlmSession openSession();
}
