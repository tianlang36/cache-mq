package cn.rdtimes.mq.intf;

import cn.rdtimes.mq.BMQException;

/**
 * 发送消息接口
 *
 * @author BZ
 */
public interface IMQSender extends IMQBase {

    /**
     * 发送消息
     *
     * @param topic   主题，可以为空值
     * @param key     关键字段，可以为空值
     * @param message 消息
     * @param <T>     具体实现的消息类
     * @throws BMQException
     */
    <T extends IMQMessage> void sendMessage(String topic, String key, T message) throws BMQException;

    default <T extends IMQMessage> void sendMessage(String topic, T message) throws BMQException {
        sendMessage(topic, null, message);
    }

    void close();

}
