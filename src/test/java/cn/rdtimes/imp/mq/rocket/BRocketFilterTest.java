package cn.rdtimes.imp.mq.rocket;

import cn.rdtimes.mq.BMQException;
import cn.rdtimes.mq.intf.IMQFilter;
import cn.rdtimes.mq.intf.IMQMessage;

/**
 * @description:
 * @author: BZ
 * @create: 2020/2/13
 */

public class BRocketFilterTest implements IMQFilter {
    @Override
    public <T extends IMQMessage> void doSenderFilter(T t) throws BMQException {
        ((BMessage)t).setContent(((BMessage)t).getContent() + " filter");
        System.out.println("sender filter:" + ((BMessage) t).getContent());
    }

    @Override
    public <T extends IMQMessage> void doReceiverFilter(T t) throws BMQException {
        System.out.println("receiver filter:" + t);
    }

}
