package cn.rdtimes.impl.mq.active;

import cn.rdtimes.impl.mq.BMQHelper;
import cn.rdtimes.mq.BMQException;
import cn.rdtimes.mq.BMQFactory;
import cn.rdtimes.mq.intf.BAbstractMQSender;
import cn.rdtimes.mq.intf.IMQFilter;
import cn.rdtimes.mq.intf.IMQMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.List;

/**
 * @description: active发送器
 * @author: BZ
 */

public class BActiveSender extends BAbstractMQSender {
    private final BActiveSenderConfiguration configuration;
    private TopicConnectionFactory connectionFactory;
    private TopicConnection connection;

    public BActiveSender(BActiveConfiguration configuration) {
        super();
        this.configuration = (BActiveSenderConfiguration) configuration;
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
        connectionFactory = new ActiveMQConnectionFactory(configuration.getUserName(), configuration.getPassword(),
                configuration.getUrl());
        connection = connectionFactory.createTopicConnection();
        connection.setClientID(configuration.getClientId());
        connection.start();
    }

    private TopicSession getChannel() throws JMSException {
        return connection.createTopicSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
    }

    @Override
    public String getName() {
        return BMQHelper.RABBIT_SENDER_NAME;
    }

    @Override
    protected <T extends IMQMessage> void processMessage(String topic, String key, T value) throws BMQException {
        if (topic == null || topic.equals("")) {
            throw new BMQException("topic or queue name is null");
        }

        TopicSession session = null;
        try {
            session = getChannel();
            // 创建主题
            Topic destination = session.createTopic(topic);
            TopicPublisher producer = session.createPublisher(destination);
            if (configuration.isEnablePersistent())
                producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            else
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            //发送消息
            producer.send(((BActiveMessage) value).getMessage(session));
            producer.close();
        } catch (Exception e) {
            throw new BMQException(e);
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

}
