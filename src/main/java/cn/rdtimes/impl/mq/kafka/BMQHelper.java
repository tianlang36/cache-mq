package cn.rdtimes.impl.mq.kafka;

import cn.rdtimes.mq.BMQFactory;
import cn.rdtimes.mq.intf.IMQReceiver;
import cn.rdtimes.mq.intf.IMQSender;


/**
 * @description: mq帮助类
 * @author: BZ
 * @create: 2020/2/13
 */

public final class BMQHelper {
    public final static String KAFKA_SENDER_NAME = "BZ-KafkaSender";
    public final static String KAFKA_RECEIVER_NAME = "BZ-KafkaReceiver";

    public static IMQSender getSender() {
        return BMQFactory.getMQSender(KAFKA_SENDER_NAME);
    }

    public static IMQReceiver getReceiver() {
        return BMQFactory.getMQReceiver(KAFKA_RECEIVER_NAME);
    }

    public static IMQReceiver getReceiver(String id) {
        return BMQFactory.getMQReceiver(id);
    }

    public static IMQSender createSender(BKafkaConfiguration configuration) {
        return new BKafkaSender(configuration);
    }

    public static IMQReceiver createReceiver(BKafkaConfiguration configuration) {
        return new BKafkaReceiver(configuration);
    }

}
