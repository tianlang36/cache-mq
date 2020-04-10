package cn.rdtimes.impl.mq.rocket;

import cn.rdtimes.mq.BMQException;
import cn.rdtimes.mq.intf.IMQMessage;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;


/**
 * @description: rocket消息对象,子类要实现抽象方法
 * @author: BZ
 * @create: 2020/2/13
 */

public abstract class BRocketMessage implements IMQMessage {

    public abstract Message getMessage(String topic, String tag) throws BMQException;

    public abstract void setMessage(List<MessageExt> msgList);

}
