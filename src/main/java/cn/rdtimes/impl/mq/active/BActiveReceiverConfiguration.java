package cn.rdtimes.impl.mq.active;

import cn.rdtimes.mq.intf.IMQReceiver;

/**
 * @description: active接收器配置信息
 * @author: BZ
 * @create: 2020/2/13
 */

public class BActiveReceiverConfiguration extends BActiveConfiguration {
    private IMQReceiver.IProcessNotify processNotify;
    //接收器线程池大小，与发送器无关
    private int threadCount = 1;
    //接收下线程池队列最大值，与发送器无关
    private int queueSize = 2000;
    //以不同名称可以创建多个接收器
    private String name;
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

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
