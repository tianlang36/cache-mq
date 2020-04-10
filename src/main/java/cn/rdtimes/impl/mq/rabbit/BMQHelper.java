package cn.rdtimes.impl.mq.rabbit;

import cn.rdtimes.mq.BMQFactory;
import cn.rdtimes.mq.intf.IMQReceiver;
import cn.rdtimes.mq.intf.IMQSender;


/**
 * @description: mq帮助类
 * @author: BZ
 * @create: 2020/2/13
 */

public final class BMQHelper {
    public final static String RABBIT_SENDER_NAME = "BZ-RabbitSender";
    public final static String RABBIT_RECEIVER_NAME = "BZ-RabbitReceiver";

    public static IMQSender getSender() {
        return BMQFactory.getMQSender(RABBIT_SENDER_NAME);
    }

    public static IMQReceiver getReceiver() {
        return BMQFactory.getMQReceiver(RABBIT_RECEIVER_NAME);
    }

    public static IMQReceiver getReceiver(String id) {
        return BMQFactory.getMQReceiver(id);
    }

    public static IMQSender createSender(BRabbitConfiguration configuration) {
        return new BRabbitSender(configuration);
    }

    public static IMQReceiver createReceiver(BRabbitConfiguration configuration) {
        return new BRabbitReceiver(configuration);
    }

}
