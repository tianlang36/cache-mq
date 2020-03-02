package cn.rdtimes.impl.mq.kafka;

import cn.rdtimes.mq.intf.IMQFilter;
import cn.rdtimes.mq.intf.IMQReceiver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description: kafka接收器的配置信息
 * @author: BZ
 */

public class BKafkaReceiverConfiguration extends BKafkaConfiguration {
    private IMQReceiver.IProcessNotify processNotify;

    //以下是Consumer使用,大部分可以使用默认值
    //是否能自动提交
    private boolean autoCommit = false;
    //自动提交间隔，单位：毫秒
    private int autoCommitInterval = 1000;
    //偏移量自动重设策略,[latest, earliest, none]
    private String autoOffsetReset = "earliest";
    //会话超时时间
    private int sessionTimeout = 30000;
    //指定消费者从服务器获取记录的最小字节数,单位：字节
    private int fetchMin = 1;
    //指定消费者从服务器获取记录的最大字节数,单位：字节
    private int fetchMax = 52428800;
    //用于控制单次调用 call() 方法能够返回的记录数量
    private int maxPollRecords = 100;
    // 逻辑应用名称来跟踪请求的来源，而不是只能通过IP和端口号跟进。
    private String clientId = "BZ-KafkaCustomer000000";

    //消费者组名称
    private String groupId = "BZ-KakfaConsumerGroupId";
    //关键字的反序列化类
    protected String keyDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";
    //值的反序列化类
    protected String valueDeserializer = "cn.rdtimes.impl.mq.kafka.BObjectDeserializer";

    //以下是内部使用
    //订阅主题,多个主题可以用逗号分隔
    private String topicNames;
    //poll超时时间,单位：毫秒
    private long pollTimeout = 5000;

    public IMQReceiver.IProcessNotify getProcessNotify() {
        return processNotify;
    }

    public void setProcessNotify(IMQReceiver.IProcessNotify processNotify) {
        this.processNotify = processNotify;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public int getAutoCommitInterval() {
        return autoCommitInterval;
    }

    public void setAutoCommitInterval(int autoCommitInterval) {
        this.autoCommitInterval = autoCommitInterval;
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public void setAutoOffsetReset(String autoOffsetReset) {
        this.autoOffsetReset = autoOffsetReset;
    }

    public String getTopicNames() {
        return topicNames;
    }

    public void setTopicNames(String topicNames) {
        this.topicNames = topicNames;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public List<String> getTopicNameList() {
        String[] str = topicNames.split(",");
        return Arrays.asList(str);
    }

    public long getPollTimeout() {
        return pollTimeout;
    }

    public void setPollTimeout(long pollTimeout) {
        this.pollTimeout = pollTimeout;
    }

    public int getFetchMin() {
        return fetchMin;
    }

    public void setFetchMin(int fetchMin) {
        this.fetchMin = fetchMin;
    }

    public int getFetchMax() {
        return fetchMax;
    }

    public void setFetchMax(int fetchMax) {
        this.fetchMax = fetchMax;
    }

    public int getMaxPollRecords() {
        return maxPollRecords;
    }

    public void setMaxPollRecords(int maxPollRecords) {
        this.maxPollRecords = maxPollRecords;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    protected void convertProperties() {
        properties.setProperty("key.deserializer", keyDeserializer);
        properties.setProperty("value.deserializer", valueDeserializer);
        properties.put("group.id", groupId);
        properties.put("enable.auto.commit", autoCommit);
        properties.put("auto.commit.interval.ms", autoCommitInterval);
        properties.put("session.timeout.ms", sessionTimeout);
        properties.put("auto.offset.reset", autoOffsetReset);
        properties.put("fetch.min.bytes", fetchMin);
        properties.put("fetch.max.bytes", fetchMax);
        properties.put("max.poll.records", maxPollRecords);
        properties.put("receive.buffer.bytes", -1);
        properties.put("client.id", clientId);
    }

}
