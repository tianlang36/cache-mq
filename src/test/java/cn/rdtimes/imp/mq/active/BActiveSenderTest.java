package cn.rdtimes.imp.mq.active;

import cn.rdtimes.imp.mq.BMyFilterTest;
import cn.rdtimes.imp.mq.BObjectMessageTest;
import cn.rdtimes.impl.mq.BMQHelper;
import cn.rdtimes.impl.mq.active.BActiveSenderConfiguration;
import cn.rdtimes.mq.BMQFactory;
import cn.rdtimes.mq.intf.IMQSender;
import org.junit.Test;


/**
 * @description: mem缓存测试
 * @author: BZ
 */

public class BActiveSenderTest {

    @Test
    public void test() {
        BActiveSenderConfiguration configuration = new BActiveSenderConfiguration();
        configuration.setUrl("tcp://127.0.0.1:61616");
//        configuration.setUserName("bz");
//        configuration.setPassword("123456");
        configuration.setClientId("0000000");
        configuration.addFilter(new BActiveFilterTest());
        IMQSender sender = BMQHelper.createActiveSender(configuration);

        int i = 1;
        String queueName = "bztest";
        try {
            while (i <= 10) {
                sender.sendMessage(queueName, createMessage("aaa" + i));
                i++;

                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        BMQFactory.shutdownMQSenders();
    }

    private BMessage createMessage(String content) {
        BMessage message = new BMessage();
        message.setContent(content);
        return message;
    }

}
