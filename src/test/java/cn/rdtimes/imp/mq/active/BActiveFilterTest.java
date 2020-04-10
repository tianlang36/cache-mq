package cn.rdtimes.imp.mq.active;

import cn.rdtimes.mq.BMQException;
import cn.rdtimes.mq.intf.IMQFilter;
import cn.rdtimes.mq.intf.IMQMessage;

/**
 * @description:
 * @author: BZ
 * @create: 2020/2/13
 */

public class BActiveFilterTest implements IMQFilter {
    @Override
    public <T extends IMQMessage> void doSenderFilter(T t) throws BMQException {
        ((BMessage)t).setContent(((BMessage)t).getContent() + " filter");
        System.out.println("sender filter:" + t.toString());
    }

    @Override
    public <T extends IMQMessage> void doReceiverFilter(T t) throws BMQException {
        System.out.println("receiver filter:" + t);
    }

}
