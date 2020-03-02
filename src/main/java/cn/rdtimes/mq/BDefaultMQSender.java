package cn.rdtimes.mq;

import cn.rdtimes.mq.intf.BAbstractMQBase;
import cn.rdtimes.mq.intf.IMQMessage;
import cn.rdtimes.mq.intf.IMQSender;

/**
 * 发送消息缺省实现
 *
 * @author BZ
 */
final class BDefaultMQSender extends BAbstractMQBase implements IMQSender {

    BDefaultMQSender() {
        //ignore
    }

    public String getName() {
        return "BDefaultMQSender";
    }

    @Override
    public <T extends IMQMessage> void sendMessage(String topic, String key, T message) throws BMQException {
        System.out.println("BDefaultMQSender have send a message: [topic=" + topic + ",key=" + key +
                ",message=" + (message == null ? "" : message));
    }

    public void close() {
        //ignore
    }

}
