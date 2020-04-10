package cn.rdtimes.impl.mq.active;

import cn.rdtimes.mq.BMQException;
import cn.rdtimes.mq.intf.IMQMessage;

import javax.jms.Message;
import javax.jms.Session;

/**
 * @description: active消息对象,子类要实现抽象方法
 * @author: BZ
 * @create: 2020/2/13
 */

public abstract class BActiveMessage implements IMQMessage {

    public abstract Message getMessage(final Session session) throws BMQException;

    public abstract void setMessage(Message msg);

}
