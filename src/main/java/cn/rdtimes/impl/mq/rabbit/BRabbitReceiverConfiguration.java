package cn.rdtimes.impl.mq.rabbit;

import cn.rdtimes.mq.intf.IMQReceiver;

/**
 * @description: rabbit接收器配置信息
 * @author: BZ
 */

public class BRabbitReceiverConfiguration extends BRabbitConfiguration {
    private IMQReceiver.IProcessNotify processNotify;

    //是否要手工确认
    private boolean noAck = true;
    private String queueName;

    public IMQReceiver.IProcessNotify getProcessNotify() {
        return processNotify;
    }

    public void setProcessNotify(IMQReceiver.IProcessNotify processNotify) {
        this.processNotify = processNotify;
    }

    public boolean isNoAck() {
        return noAck;
    }

    public void setNoAck(boolean noAck) {
        this.noAck = noAck;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

}
