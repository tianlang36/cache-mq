package cn.rdtimes.impl.mq.kafka;

import cn.rdtimes.impl.mq.util.BSerializerUtil;
import org.apache.kafka.common.serialization.Serializer;

import java.io.IOException;

/**
 * @description: bean序列化器
 * @author: BZ
 * @create: 2020/2/13
 */

public class BObjectSerializer<T> implements Serializer<T> {

    @Override
    public byte[] serialize(String topic, T data) {
        if (data == null) return null;
        try {
            return BSerializerUtil.serialize(data);
        } catch (IOException e) {
            return null;
        }
    }

}
