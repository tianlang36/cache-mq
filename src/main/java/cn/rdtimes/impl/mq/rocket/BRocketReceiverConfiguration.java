package cn.rdtimes.impl.mq.rocket;

import cn.rdtimes.mq.intf.IMQReceiver;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * @description: rocket接收器配置信息
 * @author: BZ
 * @create: 2020/2/13
 */

public class BRocketReceiverConfiguration extends BRocketConfiguration {
    private IMQReceiver.IProcessNotify processNotify;
    //接收器线程池大小，与发送器无关
    private int threadCount = 1;
    //接收下线程池队列最大值，与发送器无关
    private int queueSize = 2000;
    //以不同名称可以创建多个接收器
    private String name;

    //主题名称
    private String topic;
    //具体消息实现类全限名称
    private String messageClassName;
    //tag组合，默认为*
    private String tags = "*";
    //消费的方式
    private MessageModel messageModel = MessageModel.CLUSTERING;
    //消费者从那个位置消费
    private ConsumeFromWhere consumeFromWhere = ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET;
    //线程池最小数目
    private int consumeThreadMin = 5;
    //线程池最大数目
    private int consumeThreadMax = 5;


    public IMQReceiver.IProcessNotify getProcessNotify() {
        return processNotify;
    }

    public void setProcessNotify(IMQReceiver.IProcessNotify processNotify) {
        this.processNotify = processNotify;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessageClassName() {
        return messageClassName;
    }

    public void setMessageClassName(String messageClassName) {
        this.messageClassName = messageClassName;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        if (tags == null || tags.equals("")) return;
        this.tags = tags;
    }

    public MessageModel getMessageModel() {
        return messageModel;
    }

    public void setMessageModel(MessageModel messageModel) {
        this.messageModel = messageModel;
    }

    public ConsumeFromWhere getConsumeFromWhere() {
        return consumeFromWhere;
    }

    public void setConsumeFromWhere(ConsumeFromWhere consumeFromWhere) {
        this.consumeFromWhere = consumeFromWhere;
    }

    public int getConsumeThreadMin() {
        return consumeThreadMin;
    }

    public void setConsumeThreadMin(int consumeThreadMin) {
        this.consumeThreadMin = consumeThreadMin;
    }

    public int getConsumeThreadMax() {
        return consumeThreadMax;
    }

    public void setConsumeThreadMax(int consumeThreadMax) {
        this.consumeThreadMax = consumeThreadMax;
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
