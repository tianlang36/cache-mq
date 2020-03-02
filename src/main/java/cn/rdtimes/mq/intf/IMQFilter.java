package cn.rdtimes.mq.intf;

import cn.rdtimes.mq.BMQException;

/**
 * mq发送和接收使用的过滤器
 *
 * @author BZ
 */
public interface IMQFilter {

    /**
     * 对接收到的消息进一步处理
     *
     * @param message
     * @param <T>
     */
    <T extends IMQMessage> void doSenderFilter(T message) throws BMQException;

    /**
     * 对接收到的消息进一步处理
     *
     * @param message
     * @param <T>
     */
    <T extends IMQMessage> void doReceiverFilter(T message) throws BMQException;

}
