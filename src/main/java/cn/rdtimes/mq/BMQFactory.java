package cn.rdtimes.mq;

import cn.rdtimes.mq.intf.BAbstractMQReceiver;
import cn.rdtimes.mq.intf.IMQReceiver;
import cn.rdtimes.mq.intf.IMQSender;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * mq工厂，管理发送者和接收者
 *
 * 1.创建MQSender实例，并注册到工厂中，可以注册一系列的对象
 *   BMQFactory.registryMQSender(new IMQSender());
 * 2.使用时通过工厂获取MQSender实例
 *   BMQFactory.getMQSender("xxx").sendMessage()
 * 3.创建MQReceiver实例将自动注册到此工厂中，无需在调用注册方法。
 *   接收者无需管理，自动完成任务。
 * 4.BMQFactory.shutdownMQ()方法将关闭所有接收者
 *
 * @author BZ
 */
public final class BMQFactory {
    private static final Map<String, IMQSender> senderMap = new ConcurrentHashMap<>(1);
    private static final Map<String, IMQReceiver> receiverMap = new ConcurrentHashMap<>(1);
    private static final IMQSender defaultSender = new BDefaultMQSender();

    public static void registryMQSender(IMQSender mqSender) {
        if (mqSender == null) {
            throw new NullPointerException("mqSender is null");
        }

        registryMQSender(mqSender.getName(), mqSender);
    }

    public static void registryMQSender(String id, IMQSender mqSender) {
        if (id == null || mqSender == null) {
            throw new NullPointerException("id or mqSender is null");
        }
        senderMap.putIfAbsent(id, mqSender);
    }

    public static void unregistryMQSender(String id) {
        if (id == null) {
            throw new NullPointerException("id is null");
        }
        senderMap.remove(id);
    }

    public static void unregistryMQSender(IMQSender mqSender) {
        if (mqSender == null) {
            throw new NullPointerException("mqSender is null");
        }

        unregistryMQSender(mqSender.getName());
    }

    public static IMQSender getMQSender(String id) {
        IMQSender sender = senderMap.get(id);
        if (sender == null) {
            sender = defaultSender;
        }
        return sender;
    }

    public static Map<String, IMQSender> getSenderMap() {
        return senderMap;
    }

    public static void registryMQReceiver(IMQReceiver mqReceiver) {
        if (mqReceiver == null) {
            throw new NullPointerException("mqReceiver is null");
        }

        registryMQReceiver(mqReceiver.getName(), mqReceiver);
    }

    public static void registryMQReceiver(String id, IMQReceiver mqReceiver) {
        if (id == null || mqReceiver == null) {
            throw new NullPointerException("id or mqReceiver is null");
        }

        receiverMap.putIfAbsent(id, mqReceiver);
    }

    public static void unregistryMQReceiver(IMQReceiver mqReceiver) {
        if (mqReceiver == null) {
            throw new NullPointerException("mqReceiver is null");
        }

        unregistryMQReceiver(mqReceiver.getName());
    }

    public static void unregistryMQReceiver(String id) {
        if (id == null) {
            throw new NullPointerException("id is null");
        }
        receiverMap.remove(id);
    }

    public static IMQReceiver getMQReceiver(String id) {
        return receiverMap.get(id);
    }

    public static void shutdown() {
        shutdownMQSenders();
        shutdownMQReceivers();
    }

    public static void shutdownMQReceivers() {
        for (IMQReceiver receiver : receiverMap.values()) {
            shutdownMQReceiver(receiver);
        }
    }

    public static void shutdownMQReceiver(IMQReceiver receiver) {
        if (receiver != null) {
            try {
                //这里有可能实现类不是继承此抽象类
                ((BAbstractMQReceiver) receiver).shutdown();
            } catch (Exception e) {
                receiver.close();
                unregistryMQReceiver(receiver);
            }
        }
    }

    public static void shutdownMQReceiver(String id) {
        IMQReceiver receiver = getMQReceiver(id);
        shutdownMQReceiver(receiver);
    }

    public static void shutdownMQSenders() {
        for (IMQSender sender : senderMap.values()) {
            shutdownMQSender(sender);
        }
    }

    public static void shutdownMQSender(IMQSender sender) {
        if (sender != null) {
            try {
                sender.close();
            } catch (Exception e) {
                //ignore
            }
            unregistryMQSender(sender.getName());
        }
    }

    public static void shutdownMQSender(String id) {
        IMQSender sender = getMQSender(id);
        shutdownMQSender(sender);
    }

    public static Map<String, IMQReceiver> getReceiverMap() {
        return receiverMap;
    }

}

