package cn.rdtimes.impl.mq.kafka;

import cn.rdtimes.impl.mq.BMQHelper;
import cn.rdtimes.mq.BMQException;
import cn.rdtimes.mq.BMQFactory;
import cn.rdtimes.mq.intf.BAbstractMQReceiver;
import cn.rdtimes.mq.intf.IMQFilter;
import cn.rdtimes.mq.intf.IMQMessage;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * @description: 接收器
 * @author: BZ
 */

public class BKafkaReceiver extends BAbstractMQReceiver {
    private BKafkaReceiverConfiguration configuration;
    private Consumer consumer;
    private final CommitBack commitBack = new CommitBack();

    public BKafkaReceiver(BKafkaConfiguration configuration) {
        super(configuration.getName() == null ? BMQHelper.KAFKA_RECEIVER_NAME : configuration.getName(),
                configuration.getThreadCount(), configuration.getQueueSize());
        try {
            this.configuration = (BKafkaReceiverConfiguration) configuration;
            this.processNotify = this.configuration.getProcessNotify();
            if (this.processNotify == null) {
                throw new NullPointerException("processNotify is null");
            }
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
    public <T extends IMQMessage> List<T> receiveMessages() throws BMQException {
        if (!initAndExit()) return null;

        List<T> retList;
        try {
            //在该时间内 poll 会等待服务器返回数据
            ConsumerRecords<String, T> records = consumer.poll(configuration.getPollTimeout());
            if (records == null || records.isEmpty()) {
                return null;
            } else {
                retList = new ArrayList<T>(records.count());
            }

            // poll 返回一个记录列表
            // 每条记录都包含了记录所属主题的信息、记录所在分区的信息、记录在分区里的偏移量，以及记录的键值对
            for (ConsumerRecord<String, T> record : records) {
                System.out.printf("receiver a message [topic=%s, partition=%s, offset=%d, key=%s, value=%s].\r\n",
                        record.topic(), record.partition(), record.offset(),
                        record.key(), record.value());

                retList.add(record.value());
            }

            if (!configuration.isAutoCommit()) { //没指定自动提交这里要提交
                try {
                    consumer.commitAsync(commitBack);
                } catch (CommitFailedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            throw new BMQException(e);
        }

        return retList;
    }

    private boolean initAndExit() {
        if (consumer == null) {
            consumer = new KafkaConsumer(configuration.getProperties());
            consumer.subscribe(configuration.getTopicNameList());
        }

        if (isStopped()) {
            try {
                if (consumer != null) {
                    consumer.commitSync();
                    consumer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        return true;
    }

    @Override
    public void close() {
        //ignore
    }

    class CommitBack implements OffsetCommitCallback {
        @Override
        public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception e) {
            if (e != null) {
                System.out.printf("Commit failed for offsets %s,%s\r\n", map, e);
            }
        }
    }

}
