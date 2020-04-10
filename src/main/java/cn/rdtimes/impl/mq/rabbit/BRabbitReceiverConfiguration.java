package cn.rdtimes.impl.mq.rabbit;

import cn.rdtimes.mq.intf.IMQReceiver;

/**
 * @description: rabbit接收器配置信息
 * @author: BZ
 * @create: 2020/2/13
 */

public class BRabbitReceiverConfiguration extends BRabbitConfiguration {
    private IMQReceiver.IProcessNotify processNotify;
    //接收器线程池大小，与发送器无关
    private int threadCount = 1;
    //接收下线程池队列最大值，与发送器无关
    private int queueSize = 2000;
    //以不同名称可以创建多个接收器
    private String name;

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
