package cn.rdtimes.impl.mq.kafka;

import cn.rdtimes.impl.mq.util.BSerializerUtil;
import org.apache.kafka.common.serialization.Deserializer;


/**
 * @description: bean反序列化
 * @author: BZ
 * @create: 2020/2/13
 */

public class BObjectDeserializer<T> implements Deserializer<T> {

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null) return null;
        try {
            return BSerializerUtil.deserialize(data);
        } catch (Exception e) {
            return null;
        }
    }

}
