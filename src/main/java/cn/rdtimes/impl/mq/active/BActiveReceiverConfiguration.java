package cn.rdtimes.impl.mq.active;

import cn.rdtimes.mq.intf.IMQReceiver;

/**
 * @description: active接收器配置信息
 * @author: BZ
 */

public class BActiveReceiverConfiguration extends BActiveConfiguration {
    private IMQReceiver.IProcessNotify processNotify;
    //主题名称
    private String queueName;
    //具体消息实现类全限名称
    private String messageClassName;

    public IMQReceiver.IProcessNotify getProcessNotify() {
        return processNotify;
    }

    public void setProcessNotify(IMQReceiver.IProcessNotify processNotify) {
        this.processNotify = processNotify;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getMessageClassName() {
        return messageClassName;
    }

    public void setMessageClassName(String messageClassName) {
        this.messageClassName = messageClassName;
    }

}
