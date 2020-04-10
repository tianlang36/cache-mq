package cn.rdtimes.imp.mq.kafka;

import cn.rdtimes.imp.mq.BMyFilterTest;
import cn.rdtimes.imp.mq.BObjectMessageTest;
import cn.rdtimes.impl.mq.kafka.BMQHelper;
import cn.rdtimes.impl.mq.kafka.BKafkaSenderConfiguration;
import cn.rdtimes.mq.BMQFactory;
import cn.rdtimes.mq.intf.IMQSender;
import org.junit.Test;


/**
 * @description:
 * @author: BZ
 * @create: 2020/2/13
 */

public class BKafkaSenderTest {

    @Test
    public void test() {
        BKafkaSenderConfiguration configuration = new BKafkaSenderConfiguration();
        configuration.setBootstrapServers("localhost:9092");
        configuration.addFilter(new BMyFilterTest());
        IMQSender sender = BMQHelper.createSender(configuration);

        int i = 1;
        try {
            while (i <= 10) {
                sender.sendMessage("test", i + "", createMessage(i + "", "aaa" + i, i));
                i++;

                Thread.sleep(1000);
            }
        }catch (Exception e) {
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
