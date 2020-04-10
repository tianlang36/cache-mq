package cn.rdtimes.imp.mq.rocket;

import cn.rdtimes.impl.mq.rocket.BMQHelper;
import cn.rdtimes.impl.mq.rocket.BRocketSenderConfiguration;
import cn.rdtimes.mq.BMQFactory;
import cn.rdtimes.mq.intf.IMQSender;
import org.junit.Test;


/**
 * @description:
 * @author: BZ
 * @create: 2020/2/13
 */

public class BRocketSenderTest {

    @Test
    public void test() {
        BRocketSenderConfiguration configuration = new BRocketSenderConfiguration();
        configuration.setIpAndPort("127.0.0.1:9876");
        configuration.setGroupId("0000000");
        configuration.addFilter(new BRocketFilterTest());
        IMQSender sender = BMQHelper.createSender(configuration);

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
