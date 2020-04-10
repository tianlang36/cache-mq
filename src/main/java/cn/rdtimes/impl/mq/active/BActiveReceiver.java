package cn.rdtimes.impl.mq.active;

import cn.rdtimes.mq.BMQException;
import cn.rdtimes.mq.BMQFactory;
import cn.rdtimes.mq.intf.BAbstractMQReceiver;
import cn.rdtimes.mq.intf.IMQFilter;
import cn.rdtimes.mq.intf.IMQMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.lang.IllegalStateException;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: active接收器
 * @author: BZ
 * @create: 2020/2/13
 */

public class BActiveReceiver extends BAbstractMQReceiver {
    private BActiveReceiverConfiguration configuration;
    private TopicConnectionFactory connectionFactory;
    private TopicConnection connection;
    private TopicSession session;
    private TopicSubscriber topicSubscriber;

    public BActiveReceiver(BActiveConfiguration configuration) {
        super(((BActiveReceiverConfiguration) configuration).getName() == null ? BMQHelper.ACTIVE_RECEIVER_NAME :
                        ((BActiveReceiverConfiguration) configuration).getName(),
                ((BActiveReceiverConfiguration) configuration).getThreadCount(),
                ((BActiveReceiverConfiguration) configuration).getQueueSize());
        try {
            this.configuration = (BActiveReceiverConfiguration) configuration;
            this.processNotify = this.configuration.getProcessNotify();
            if (this.processNotify == null) {
                throw new NullPointerException("processNotify is null");
            }
            List<IMQFilter> filters = configuration.getFilters();
            for (IMQFilter filter : filters) {
                addFilter(filter);
            }

            start();
        } catch (Exception e) {
            BMQFactory.unregistryMQReceiver(this);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void start() {
        try {
            connectionFactory = new ActiveMQConnectionFactory(configuration.getUserName(), configuration.getPassword(),
                    configuration.getUrl());
            connection = connectionFactory.createTopicConnection();
            connection.setClientID(configuration.getClientId());
            connection.start();
            session = connection.createTopicSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            // 创建主题
            Topic topic = session.createTopic(configuration.getQueueName());
            if (configuration.isEnablePersistent()) {
                topicSubscriber = session.createDurableSubscriber(topic, configuration.getClientId());
            } else {
                topicSubscriber = session.createSubscriber(topic);
            }

            super.start();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public <T extends IMQMessage> List<T> receiveMessages() throws BMQException {
        try {
            Message msg = topicSubscriber.receive();
            if (msg != null) {
                System.out.println("messageId: " + msg.getJMSMessageID());
                System.out.println("correlationId: " + msg.getJMSCorrelationID());
                System.out.println("timestamp: " + msg.getJMSTimestamp());

                BActiveMessage activeMessage = getMessageInstance();
                List<T> list = new ArrayList<T>(1);
                activeMessage.setMessage(msg);
                list.add((T) activeMessage);
                return list;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new BMQException(e);
        }
    }

    private BActiveMessage getMessageInstance() throws Exception {
        return (BActiveMessage) Class.forName(configuration.getMessageClassName()).newInstance();
    }

    @Override
    public void close() {
        if (topicSubscriber != null) {
            try {
                topicSubscriber.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (session != null) {
            try {
                session.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
