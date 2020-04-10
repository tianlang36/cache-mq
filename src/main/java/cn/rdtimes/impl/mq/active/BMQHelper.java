package cn.rdtimes.impl.mq.active;

import cn.rdtimes.mq.BMQFactory;
import cn.rdtimes.mq.intf.IMQReceiver;
import cn.rdtimes.mq.intf.IMQSender;


/**
 * @description: mq帮助类
 * @author: BZ
 * @create: 2020/2/13
 */

public final class BMQHelper {
    public final static String ACTIVE_SENDER_NAME = "BZ-ActiveSender";
    public final static String ACTIVE_RECEIVER_NAME = "BZ-ActiveReceiver";

    public static IMQSender getSender() {
        return BMQFactory.getMQSender(ACTIVE_SENDER_NAME);
    }

    public static IMQReceiver getReceiver() {
        return BMQFactory.getMQReceiver(ACTIVE_RECEIVER_NAME);
    }

    public static IMQReceiver getReceiver(String id) {
        return BMQFactory.getMQReceiver(id);
    }

    public static IMQSender createSender(BActiveConfiguration configuration) {
        return new BActiveSender(configuration);
    }

    public static IMQReceiver createReceiver(BActiveConfiguration configuration) {
        return new BActiveReceiver(configuration);
    }

}
