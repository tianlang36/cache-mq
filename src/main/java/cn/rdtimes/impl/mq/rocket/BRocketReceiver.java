package cn.rdtimes.impl.mq.rocket;

import cn.rdtimes.mq.BMQException;
import cn.rdtimes.mq.BMQFactory;
import cn.rdtimes.mq.intf.BAbstractMQReceiver;
import cn.rdtimes.mq.intf.IMQFilter;
import cn.rdtimes.mq.intf.IMQMessage;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.lang.IllegalStateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @description: rocket接收器
 * @author: BZ
 * @create: 2020/2/13
 */

public class BRocketReceiver extends BAbstractMQReceiver {
    //接收队列
    private final BlockingQueue<List<MessageExt>> receiveQueue;
    private BRocketReceiverConfiguration configuration;
    private DefaultMQPushConsumer consumer;

    public BRocketReceiver(BRocketConfiguration configuration) {
        super(((BRocketReceiverConfiguration)configuration).getName() == null ? BMQHelper.ROCKET_RECEIVER_NAME :
                        ((BRocketReceiverConfiguration)configuration).getName(),
                ((BRocketReceiverConfiguration)configuration).getThreadCount(),
                ((BRocketReceiverConfiguration)configuration).getQueueSize());
        try {
            this.configuration = (BRocketReceiverConfiguration) configuration;
            this.processNotify = this.configuration.getProcessNotify();
            if (this.processNotify == null) {
                throw new NullPointerException("processNotify is null");
            }
            receiveQueue = new LinkedBlockingQueue();

            List<IMQFilter> filters = configuration.getFilters();
            for (IMQFilter filter : filters) {
                addFilter(filter);
            }

            start();
        } catch (Exception e) {
            BMQFactory.unregistryMQReceiver(this);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void start() {
        try {
            consumer = new DefaultMQPushConsumer(configuration.getGroupId());
            consumer.setNamesrvAddr(configuration.getIpAndPort());
            consumer.setConsumeFromWhere(configuration.getConsumeFromWhere());
            consumer.setConsumeThreadMax(configuration.getConsumeThreadMax());
            consumer.setConsumeThreadMin(configuration.getConsumeThreadMin());
            consumer.setMessageModel(configuration.getMessageModel());

            consumer.subscribe(configuration.getTopic(), configuration.getTags());
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list,
                                                                ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                    if (isStopped()) throw new IllegalStateException("it is already shutdown");

//                    System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), list);
                    receiveQueue.add(list);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            consumer.start();

            super.start();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public <T extends IMQMessage> List<T> receiveMessages() throws BMQException {
        try {
            List<MessageExt> list = receiveQueue.take();
            if (list != null && list != Collections.EMPTY_LIST) {
                BRocketMessage rocketMessage = getMessageInstance();
                List<T> retList = new ArrayList<T>(1);
                rocketMessage.setMessage(list);
                retList.add((T) rocketMessage);
                return retList;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new BMQException(e);
        }
    }

    private BRocketMessage getMessageInstance() throws Exception {
        return (BRocketMessage) Class.forName(configuration.getMessageClassName()).newInstance();
    }

    @Override
    public void close() {
        if (consumer != null) {
            consumer.shutdown();
        }
        //结束等待
        receiveQueue.add(Collections.EMPTY_LIST);
    }

}
