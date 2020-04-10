package cn.rdtimes.imp.mq.rabbit;

import cn.rdtimes.imp.mq.BMyFilterTest;
import cn.rdtimes.imp.mq.BObjectMessageTest;
import cn.rdtimes.impl.mq.rabbit.BMQHelper;
import cn.rdtimes.impl.mq.rabbit.BRabbitSenderConfiguration;
import cn.rdtimes.mq.BMQFactory;
import cn.rdtimes.mq.intf.IMQSender;
import org.junit.Test;


/**
 * @description:
 * @author: BZ
 * @create: 2020/2/13
 */

public class BRabbitSenderTest {

    @Test
    public void test() {
        BRabbitSenderConfiguration configuration = new BRabbitSenderConfiguration();
        configuration.setIpAndPort("127.0.0.1:5672");
        configuration.setUserName("bz");
        configuration.setPassword("123456");
        configuration.setExchangeName("bztest-exchange");
        configuration.addFilter(new BMyFilterTest());
        IMQSender sender = BMQHelper.createSender(configuration);

        int i = 1;
        String queueName = "bztest";
        String routingKey = null;
        try {
            while (i <= 10) {
                sender.sendMessage(queueName, routingKey, createMessage(i + "", "aaa" + i, i));
                i++;

                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        BMQFactory.shutdownMQSenders();
    }

    private BObjectMessageTest createMessage(String id, String name, int age) {
        BObjectMessageTest message = new BObjectMessageTest();
        message.setId(id);
        message.setName(name);
        message.setAge(age);
        return message;
    }

}
