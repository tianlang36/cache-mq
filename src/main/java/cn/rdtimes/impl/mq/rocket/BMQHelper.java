package cn.rdtimes.impl.mq.rocket;

import cn.rdtimes.mq.BMQFactory;
import cn.rdtimes.mq.intf.IMQReceiver;
import cn.rdtimes.mq.intf.IMQSender;


/**
 * @description: mq帮助类
 * @author: BZ
 * @create: 2020/2/13
 */

public final class BMQHelper {
    public final static String ROCKET_SENDER_NAME = "BZ-RocketSender";
    public final static String ROCKET_RECEIVER_NAME = "BZ-RocketReceiver";

    public static IMQSender getSender() {
        return BMQFactory.getMQSender(ROCKET_SENDER_NAME);
    }

    public static IMQReceiver getReceiver() {
        return BMQFactory.getMQReceiver(ROCKET_RECEIVER_NAME);
    }

    public static IMQReceiver getReceiver(String id) {
        return BMQFactory.getMQReceiver(id);
    }

    public static IMQSender createSender(BRocketConfiguration configuration) {
        return new BRocketSender(configuration);
    }

    public static IMQReceiver createReceiver(BRocketConfiguration configuration) {
        return new BRocketReceiver(configuration);
    }

}
