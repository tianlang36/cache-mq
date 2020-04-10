package cn.rdtimes.impl.mq.rabbit;

import cn.rdtimes.impl.mq.util.BSerializerUtil;
import cn.rdtimes.mq.BMQException;
import cn.rdtimes.mq.BMQFactory;
import cn.rdtimes.mq.intf.BAbstractMQReceiver;
import cn.rdtimes.mq.intf.IMQFilter;
import cn.rdtimes.mq.intf.IMQMessage;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: rabbit接收器
 * @author: BZ
 * @create: 2020/2/13
 */

public class BRabbitReceiver extends BAbstractMQReceiver {
    private BRabbitReceiverConfiguration configuration;
    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Channel channel;  //这个是非线程安全，这里是独立的线程使用
    private BQueueingConsumer queueingConsumer;

    public BRabbitReceiver(BRabbitConfiguration configuration) {
        super(((BRabbitReceiverConfiguration)configuration).getName() == null ? BMQHelper.RABBIT_RECEIVER_NAME :
                        ((BRabbitReceiverConfiguration)configuration).getName(),
                ((BRabbitReceiverConfiguration)configuration).getThreadCount(),
                ((BRabbitReceiverConfiguration)configuration).getQueueSize());
        try {
            this.configuration = (BRabbitReceiverConfiguration) configuration;
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
            if (BSerializerUtil.stringIsEmpty(configuration.getUserName()) ||
                    BSerializerUtil.stringIsEmpty(configuration.getIpAndPort()) ||
                    BSerializerUtil.stringIsEmpty(configuration.getQueueName())) {
                throw new NullPointerException("param is null");
            }

            connectionFactory = new ConnectionFactory();
            connectionFactory.setUsername(configuration.getUserName());
            connectionFactory.setPassword(configuration.getPassword());
            connectionFactory.setVirtualHost(configuration.getVhost());

            String[] ipAndPort = configuration.getIpAndPort().split(",");
            Address[] addresses = new Address[ipAndPort.length];
            int i = 0;
            for (String str : ipAndPort) {
                String[] ipp = str.split(":");
                addresses[i] = new Address(ipp[0], Integer.parseInt(ipp[1]));
                ++i;
            }
            connection = connectionFactory.newConnection(addresses);

            //Channel是非线程安全的
            channel = connection.createChannel();
            //创建队列
            channel.queueDeclare(configuration.getQueueName(), configuration.isEnablePersistent(),
                    false, false, null);
            channel.basicQos(1);

            queueingConsumer = new BQueueingConsumer(channel);
            channel.basicConsume(configuration.getQueueName(), !configuration.isNoAck(), queueingConsumer);

            super.start();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public <T extends IMQMessage> List<T> receiveMessages() throws BMQException {
        try {
            BQueueingConsumer.Delivery nextDelivery = queueingConsumer.nextDelivery();
            if (nextDelivery == null) {
                return null;
            } else {
                System.out.println("exchange: " + nextDelivery.getEnvelope().getExchange());
                System.out.println("routingKey: " + nextDelivery.getEnvelope().getRoutingKey());
                System.out.println("deliveryTag: " + nextDelivery.getEnvelope().getDeliveryTag());

                List<T> list = new ArrayList<T>(1);
                T value = BSerializerUtil.deserialize(nextDelivery.getBody());
                System.out.println("message: " + value);
                list.add(value);

                if (configuration.isNoAck()) {
                    channel.basicAck(nextDelivery.getEnvelope().getDeliveryTag(), false);
                }

                return list;
            }
        } catch (ShutdownSignalException se) {
            return null;
        } catch (ConsumerCancelledException ce) {
            return null;
        } catch (Exception e) {
            throw new BMQException(e);
        }
    }

    @Override
    public void close() {
        if (channel != null) {
            try {
                channel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
