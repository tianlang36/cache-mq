package cn.rdtimes.impl.mq;

import cn.rdtimes.impl.mq.active.BActiveConfiguration;
import cn.rdtimes.impl.mq.active.BActiveReceiver;
import cn.rdtimes.impl.mq.active.BActiveSender;
import cn.rdtimes.impl.mq.kafka.BKafkaConfiguration;
import cn.rdtimes.impl.mq.kafka.BKafkaReceiver;
import cn.rdtimes.impl.mq.kafka.BKafkaSender;
import cn.rdtimes.impl.mq.rabbit.BRabbitConfiguration;
import cn.rdtimes.impl.mq.rabbit.BRabbitReceiver;
import cn.rdtimes.impl.mq.rabbit.BRabbitSender;
import cn.rdtimes.mq.BMQFactory;
import cn.rdtimes.mq.intf.IMQReceiver;
import cn.rdtimes.mq.intf.IMQSender;


/**
 * @description: mq帮助类，使用此类创建和获取mq接口
 * @author: BZ
 */

public final class BMQHelper {
    public final static String KAFKA_SENDER_NAME = "BZ-KafkaSender";
    public final static String RABBIT_SENDER_NAME = "BZ-RabbitSender";
    public final static String ACTIVE_SENDER_NAME = "BZ-ActiveSender";
    public final static String KAFKA_RECEIVER_NAME = "BZ-KafkaReceiver";
    public final static String RABBIT_RECEIVER_NAME = "BZ-RabbitReceiver";
    public final static String ACTIVE_RECEIVER_NAME = "BZ-ActiveReceiver";

    public static IMQSender getKafkaSender() {
        return BMQFactory.getMQSender(KAFKA_SENDER_NAME);
    }

    public static IMQSender getRabbitSender() {
        return BMQFactory.getMQSender(RABBIT_SENDER_NAME);
    }

    public static IMQSender getActiveSender() {
        return BMQFactory.getMQSender(ACTIVE_SENDER_NAME);
    }

    public static IMQReceiver getKafkaReceiver() {
        return BMQFactory.getMQReceiver(KAFKA_RECEIVER_NAME);
    }

    public static IMQReceiver getRabbitReceiver() {
        return BMQFactory.getMQReceiver(RABBIT_RECEIVER_NAME);
    }

    public static IMQReceiver getActiveReceiver() {
        return BMQFactory.getMQReceiver(ACTIVE_RECEIVER_NAME);
    }

    public static IMQReceiver getKafkaReceiver(String id) {
        return BMQFactory.getMQReceiver(id);
    }

    public static IMQReceiver getRabbitReceiver(String id) {
        return BMQFactory.getMQReceiver(id);
    }

    public static IMQReceiver getActiveReceiver(String id) {
        return BMQFactory.getMQReceiver(id);
    }

    public static IMQSender createKafkaSender(BKafkaConfiguration configuration) {
        return new BKafkaSender(configuration);
    }

    public static IMQReceiver createKafkaReceiver(BKafkaConfiguration configuration) {
        return new BKafkaReceiver(configuration);
    }

    public static IMQSender createActiveSender(BActiveConfiguration configuration) {
        return new BActiveSender(configuration);
    }

    public static IMQReceiver createActiveReceiver(BActiveConfiguration configuration) {
        return new BActiveReceiver(configuration);
    }

    public static IMQSender createRabbitSender(BRabbitConfiguration configuration) {
        return new BRabbitSender(configuration);
    }

    public static IMQReceiver createRabbitReceiver(BRabbitConfiguration configuration) {
        return new BRabbitReceiver(configuration);
    }

}
