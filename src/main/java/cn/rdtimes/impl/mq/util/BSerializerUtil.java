package cn.rdtimes.impl.mq.util;

import java.io.*;

/**
 * 序列化对象工具
 */
public final class BSerializerUtil {

    /**
     * 将对象序列化成字节数组
     * @param obj
     * @return
     * @throws IOException
     */
    public static <T> byte[] serialize(T obj) throws IOException {
        if (obj == null) {
            return null;
        }

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(obj);
        return b.toByteArray();
    }

    /**
     * 反序列化字节数组到对象
     * @param bytes
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> T deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return (T)o.readObject();
    }

    public static boolean stringIsEmpty(String str) {
        if (str == null || str.length() == 0) return true;
        return false;
    }

    private BSerializerUtil() {
        //ignore
    }

}
