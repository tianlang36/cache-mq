package cn.rdtimes.impl.mq.rocket;


/**
 * @description: rocket发送器配置信息
 * @author: BZ
 * @create: 2020/2/13
 */

public class BRocketSenderConfiguration extends BRocketConfiguration {
    //创建topic时默认的队列数量
    private int defaultTopicQueueNums = 3;
    //发送消息的超时时间,单位：毫秒
    private int sendMsgTimeout = 5000;
    //压缩消息体的阈值,单位：字节
    private int compressMsgBodyOverHowmuch = 8192;
    //同步模式下内部尝试发送消息的最大次数
    private int retryTimesWhenSendFailed = 1;
    //消息的最大长度,4MB
    private int maxMessageSize = 4194304;
    //是否在内部发送失败时重试另一个broker
    private boolean retryAnotherBrokerWhenNotStoreOK = false;

    public int getDefaultTopicQueueNums() {
        return defaultTopicQueueNums;
    }

    public void setDefaultTopicQueueNums(int defaultTopicQueueNums) {
        this.defaultTopicQueueNums = defaultTopicQueueNums;
    }

    public int getSendMsgTimeout() {
        return sendMsgTimeout;
    }

    public void setSendMsgTimeout(int sendMsgTimeout) {
        this.sendMsgTimeout = sendMsgTimeout;
    }

    public int getCompressMsgBodyOverHowmuch() {
        return compressMsgBodyOverHowmuch;
    }

    public void setCompressMsgBodyOverHowmuch(int compressMsgBodyOverHowmuch) {
        this.compressMsgBodyOverHowmuch = compressMsgBodyOverHowmuch;
    }

    public int getRetryTimesWhenSendFailed() {
        return retryTimesWhenSendFailed;
    }

    public void setRetryTimesWhenSendFailed(int retryTimesWhenSendFailed) {
        this.retryTimesWhenSendFailed = retryTimesWhenSendFailed;
    }

    public int getMaxMessageSize() {
        return maxMessageSize;
    }

    public void setMaxMessageSize(int maxMessageSize) {
        this.maxMessageSize = maxMessageSize;
    }

    public boolean isRetryAnotherBrokerWhenNotStoreOK() {
        return retryAnotherBrokerWhenNotStoreOK;
    }

    public void setRetryAnotherBrokerWhenNotStoreOK(boolean retryAnotherBrokerWhenNotStoreOK) {
        this.retryAnotherBrokerWhenNotStoreOK = retryAnotherBrokerWhenNotStoreOK;
    }

}
