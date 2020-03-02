package cn.rdtimes.imp.mq.rabbit;

import cn.rdtimes.imp.mq.BMyFilterTest;
import cn.rdtimes.imp.mq.BObjectMessageTest;
import cn.rdtimes.impl.mq.BMQHelper;
import cn.rdtimes.impl.mq.rabbit.BRabbitReceiverConfiguration;
import cn.rdtimes.mq.BMQFactory;
import cn.rdtimes.mq.intf.IMQMessage;
import cn.rdtimes.mq.intf.IMQReceiver;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;


/**
 * @description: mem缓存测试
 * @author: BZ
 */

public class BRabbitReceiverTest {

    @Test
    public void test() throws InterruptedException {
        BRabbitReceiverConfiguration configuration = new BRabbitReceiverConfiguration();
        configuration.setIpAndPort("127.0.0.1:5672");
        configuration.setQueueName("bztest");
        configuration.setThreadCount(1);
        configuration.setUserName("bz");
        configuration.setPassword("123456");
        configuration.addFilter(new BMyFilterTest());
        configuration.setProcessNotify(new ProcessNotify());

        BMQHelper.createRabbitReceiver(configuration);

        CountDownLatch latch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                BMQFactory.shutdownMQReceivers();
                try {
                    Thread.sleep(3000);
                }catch (Exception e){}
                latch.countDown();
            }
        }));
        latch.await();
    }

    class ProcessNotify implements IMQReceiver.IProcessNotify {
        @Override
        public <T extends IMQMessage> void doProcess(T value) {
            BObjectMessageTest message = (BObjectMessageTest)value;
            System.out.println("receive:" + message);
        }
    }

}
