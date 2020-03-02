package cn.rdtimes.mq.intf;

import cn.rdtimes.mq.BMQException;

import java.util.List;

/**
 * 消息接收接口
 *
 * @author BZ
 */
public interface IMQReceiver extends IMQBase {

    /**
     * 从消息队列中接收消息，一旦有消息就返回
     * 由接收消息线程来循环调用此方法，知道关闭或退出
     *
     * @return 接收到的消息列表（kafka可以返回多个消息）
     */
    <T extends IMQMessage> List<T> receiveMessages() throws BMQException;

    /**
     * 获取业务通知接口
     * 当有消息接到时将会调用此接口处理消息信息，和具体业务逻辑相关
     *
     * @return 接口实现对象
     */
    IProcessNotify getProcessNotify();

    /**
     * 关闭接收消息，将停止从队列接收消息并断开连接
     */
    void close();

    /**
     * 业务处理通知，进行业务具体业务处理
     * 注意单实例时并发处理消息的情况
     */
    interface IProcessNotify {

        /**
         * 具体业务处理逻辑
         *
         * @param message 接收到消息
         */
        <T extends IMQMessage> void doProcess(T message);

    }

}
