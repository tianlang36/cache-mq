package cn.rdtimes.mq.intf;

import cn.rdtimes.mq.BMQException;
import cn.rdtimes.mq.BMQFactory;

/**
 * @description: 发送消息基类，其他实现要继承此类
 * @author: BZ
 */

public abstract class BAbstractMQSender extends BAbstractMQBase implements IMQSender {

    protected BAbstractMQSender() {
        BMQFactory.registryMQSender(getName(), this);
    }

    public String getName() {
        return "BAbstractMQSender";
    }

    @Override
    public <T extends IMQMessage> void sendMessage(String topic, String key, T message) throws BMQException {
        if (filterList.size() == 0) {
            processMessage(topic, key, message);
        } else {
            for (IMQFilter filter : filterList) {
                filter.doSenderFilter(message);
            }

            processMessage(topic, key, message);
        }
    }

    /**
     * 具体发送的实现
     */
    protected abstract <T extends IMQMessage> void processMessage(String topic, String key,
                                                                  T message) throws BMQException;

}
