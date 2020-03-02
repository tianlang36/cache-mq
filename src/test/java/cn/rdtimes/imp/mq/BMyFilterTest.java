package cn.rdtimes.imp.mq;

import cn.rdtimes.mq.BMQException;
import cn.rdtimes.mq.intf.IMQFilter;
import cn.rdtimes.mq.intf.IMQMessage;

/**
 * @description:
 * @author: BZ
 */

public class BMyFilterTest implements IMQFilter {
    @Override
    public <T extends IMQMessage> void doSenderFilter(T t) throws BMQException {
        ((BObjectMessageTest)t).setName("filter");
        System.out.println("sender filter:" + t);
    }

    @Override
    public <T extends IMQMessage> void doReceiverFilter(T t) throws BMQException {
        System.out.println("receiver filter:" + t);
        ((BObjectMessageTest)t).setName("receiver");
    }

}
