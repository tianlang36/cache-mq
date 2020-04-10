package cn.rdtimes.impl.mq.rocket;

import cn.rdtimes.mq.BMQException;
import cn.rdtimes.mq.BMQFactory;
import cn.rdtimes.mq.intf.BAbstractMQSender;
import cn.rdtimes.mq.intf.IMQFilter;
import cn.rdtimes.mq.intf.IMQMessage;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;

import java.util.List;

/**
 * @description: rocket发送器
 * @author: BZ
 * @create: 2020/2/13
 */

public class BRocketSender extends BAbstractMQSender {
    private final BRocketSenderConfiguration configuration;
    //它是线程安全的
    private DefaultMQProducer producer;

    public BRocketSender(BRocketConfiguration configuration) {
        super();
        this.configuration = (BRocketSenderConfiguration) configuration;
        List<IMQFilter> filters = configuration.getFilters();
        for (IMQFilter filter : filters) {
            addFilter(filter);
        }

        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
            BMQFactory.unregistryMQSender(this);
        }
    }

    private void init() throws Exception {
        producer = new DefaultMQProducer(configuration.getGroupId());
        producer.setNamesrvAddr(configuration.getIpAndPort());
        producer.setDefaultTopicQueueNums(configuration.getDefaultTopicQueueNums());
        producer.setCompressMsgBodyOverHowmuch(configuration.getCompressMsgBodyOverHowmuch());
        producer.setSendMsgTimeout(configuration.getSendMsgTimeout());
        producer.setMaxMessageSize(configuration.getMaxMessageSize());
        producer.setRetryTimesWhenSendFailed(configuration.getRetryTimesWhenSendFailed());
        producer.setRetryAnotherBrokerWhenNotStoreOK(configuration.isRetryAnotherBrokerWhenNotStoreOK());
        producer.start();
    }

    @Override
    public String getName() {
        return BMQHelper.ROCKET_SENDER_NAME;
    }

    @Override
    protected <T extends IMQMessage> void processMessage(String topic, String key, T value) throws BMQException {
        if (topic == null || topic.equals("")) {
            throw new BMQException("topic or queue name is null");
        }

        try {
            SendResult sendResult = producer.send(((BRocketMessage) value).getMessage(topic, key));
            if (sendResult.getSendStatus() != SendStatus.SEND_OK) {
                throw new BMQException("send is error.[" + sendResult.toString() + "]");
            }
        } catch (Exception e) {
            throw new BMQException(e);
        }
    }

    public void close() {
        if (producer != null) {
            producer.shutdown();
        }
    }

}
