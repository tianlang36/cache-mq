package cn.rdtimes.impl.mq.rabbit;

import cn.rdtimes.impl.mq.util.BSerializerUtil;
import cn.rdtimes.mq.BMQException;
import cn.rdtimes.mq.BMQFactory;
import cn.rdtimes.mq.intf.BAbstractMQSender;
import cn.rdtimes.mq.intf.IMQFilter;
import cn.rdtimes.mq.intf.IMQMessage;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.List;

/**
 * @description: rabbit发送器
 * @author: BZ
 * @create: 2020/2/13
 */

public class BRabbitSender extends BAbstractMQSender {
    private final BRabbitSenderConfiguration configuration;
    private ConnectionFactory connectionFactory;
    private Connection connection;

    public BRabbitSender(BRabbitConfiguration configuration) {
        super();
        this.configuration = (BRabbitSenderConfiguration) configuration;
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
        if (BSerializerUtil.stringIsEmpty(configuration.getExchangeName()) ||
                BSerializerUtil.stringIsEmpty(configuration.getUserName()) ||
                BSerializerUtil.stringIsEmpty(configuration.getIpAndPort())) {
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
    }

    private Channel getChannel(String topic, String key) throws IOException {
        //Channel是非线程安全的
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(configuration.getExchangeName(), "direct", configuration.isEnablePersistent());
        //创建队列
        channel.queueDeclare(topic, configuration.isEnablePersistent(),
                false, false, null);
        channel.queueBind(topic, configuration.getExchangeName(), key);
        return channel;
    }

    @Override
    public String getName() {
        return BMQHelper.RABBIT_SENDER_NAME;
    }

    /**
     * 发送消息
     *
     * @param topic 相当于队列名称或主题名称
     * @param key   相关于routingkey值
     * @param value 消息
     * @param <T>
     * @throws BMQException
     */
    @Override
    protected <T extends IMQMessage> void processMessage(String topic, String key, T value) throws BMQException {
        if (topic == null || topic.equals("")) {
            throw new BMQException("topic or queue name is null");
        }
        if (key == null) {
            key = "";
        }

        Channel channel = null;
        try {
            channel = getChannel(topic, key);
            // 异步发送消息，不用等待去接收消费者的回复消息
            channel.basicPublish(configuration.getExchangeName(), key,
                    MessageProperties.PERSISTENT_TEXT_PLAIN, BSerializerUtil.serialize(value));
        } catch (Exception e) {
            throw new BMQException(e);
        } finally {
            if (channel != null) {
                try {
                    channel.close();
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
