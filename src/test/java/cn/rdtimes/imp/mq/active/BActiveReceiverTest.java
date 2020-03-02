package cn.rdtimes.imp.mq.active;

import cn.rdtimes.imp.mq.BMyFilterTest;
import cn.rdtimes.imp.mq.BObjectMessageTest;
import cn.rdtimes.impl.mq.BMQHelper;
import cn.rdtimes.impl.mq.active.BActiveReceiverConfiguration;
import cn.rdtimes.mq.BMQFactory;
import cn.rdtimes.mq.intf.IMQMessage;
import cn.rdtimes.mq.intf.IMQReceiver;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;


/**
 * @description: mem缓存测试
 * @author: BZ
 */

public class BActiveReceiverTest {

    @Test
    public void test() throws InterruptedException {
        BActiveReceiverConfiguration configuration = new BActiveReceiverConfiguration();
        configuration.setUrl("tcp://127.0.0.1:61616");
        configuration.setQueueName("bztest");
        configuration.setThreadCount(1);
//        configuration.setUserName("bz");
//        configuration.setPassword("123456");
        configuration.addFilter(new BActiveFilterTest());
        configuration.setProcessNotify(new ProcessNotify());
        configuration.setMessageClassName("cn.rdtimes.imp.mq.active.BMessage");

        BMQHelper.createActiveReceiver(configuration);

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
