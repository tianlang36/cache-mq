package cn.rdtimes.impl.mq.kafka;

import cn.rdtimes.mq.BMQException;
import cn.rdtimes.mq.BMQFactory;
import cn.rdtimes.mq.intf.BAbstractMQSender;
import cn.rdtimes.mq.intf.IMQFilter;
import cn.rdtimes.mq.intf.IMQMessage;
import org.apache.kafka.clients.producer.*;

import java.util.List;


/**
 * @description: kafka发送器
 * @author: BZ
 * @create: 2020/2/13
 */

public class BKafkaSender extends BAbstractMQSender {
    private BKafkaSenderConfiguration configuration;
    private Producer producer;

    public BKafkaSender(BKafkaConfiguration configuration) {
        super();
        try {
            this.configuration = (BKafkaSenderConfiguration) configuration;
            List<IMQFilter> filters = configuration.getFilters();
            for (IMQFilter filter : filters) {
                addFilter(filter);
            }
            producer = new KafkaProducer(this.configuration.getProperties());
        } catch (Exception e) {
            BMQFactory.unregistryMQSender(this);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String getName() {
        return BMQHelper.KAFKA_SENDER_NAME;
    }

    @Override
    protected <T extends IMQMessage> void processMessage(String topic, String key, T value) throws BMQException {
        if (topic == null || topic.equals("") || value == null) {
            throw new BMQException("value is null");
        }

        ProducerRecord<String, T> record;
        if (configuration.getPartition() >= 0) { //指定分区
            record = new ProducerRecord<>(topic, configuration.getPartition(), key, value);
        } else {
            record = new ProducerRecord<>(topic, key, value);
        }

        //发送消息(异步发送，同步要使用get方法等待）
        producer.send(record);
    }

    public void close() {
        if (producer != null) {
            producer.close();
        }
    }

}
