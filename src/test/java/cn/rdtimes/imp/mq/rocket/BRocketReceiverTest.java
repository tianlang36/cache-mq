package cn.rdtimes.imp.mq.rocket;

import cn.rdtimes.impl.mq.rocket.BMQHelper;
import cn.rdtimes.impl.mq.rocket.BRocketReceiverConfiguration;
import cn.rdtimes.mq.BMQFactory;
import cn.rdtimes.mq.intf.IMQMessage;
import cn.rdtimes.mq.intf.IMQReceiver;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;


/**
 * @description:
 * @author: BZ
 * @create: 2020/2/13
 */

public class BRocketReceiverTest {

    @Test
    public void test() throws InterruptedException {
        BRocketReceiverConfiguration configuration = new BRocketReceiverConfiguration();
        configuration.setIpAndPort("127.0.0.1:9876");
        configuration.setTopic("bztest");
        configuration.setGroupId("00000000");
        configuration.setThreadCount(1);
        configuration.addFilter(new BRocketFilterTest());
        configuration.setProcessNotify(new ProcessNotify());
        configuration.setMessageClassName("cn.rdtimes.imp.mq.rocket.BMessage");

        BMQHelper.createReceiver(configuration);

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
            BMessage message = (BMessage)value;
            System.out.println("receive:" + message);
        }
    }

}
