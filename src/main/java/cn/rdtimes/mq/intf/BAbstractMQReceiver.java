package cn.rdtimes.mq.intf;

import cn.rdtimes.mq.BMQException;
import cn.rdtimes.mq.BMQFactory;

import java.util.List;
import java.util.concurrent.*;

/**
 * @description: 接收消息基类，其他实现要继承此类
 * @author: BZ
 */

public abstract class BAbstractMQReceiver extends BAbstractMQBase implements IMQReceiver {
    //消息处理线程池大小，大于1时启动线程池
    protected int threadCount;
    //线程池中队列的大小
    protected int queueSize;
    protected IProcessNotify processNotify;
    protected String name;

    private ExecutorService executorService;
    private volatile boolean isStopped;
    private MQReceiverThread receiverThread;
    private volatile boolean isStarted;

    protected BAbstractMQReceiver(int threadCount, int queueSize) {
        this("BAbstractMQReceiver", threadCount, queueSize);
    }

    protected BAbstractMQReceiver(String name, int threadCount, int queueSize) {
        this.name = name;
        this.threadCount = (threadCount <= 0 ? 1 : threadCount);
        this.queueSize = (queueSize <= 0 ? 2000 : queueSize);

        createThreadPool();
        BMQFactory.registryMQReceiver(getName(), this);
    }

    private void createThreadPool() {
        if (this.threadCount > 1) {
            executorService = new ThreadPoolExecutor((threadCount / 2), threadCount, 60,
                    TimeUnit.SECONDS, new LinkedBlockingQueue<>(queueSize));
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public IProcessNotify getProcessNotify() {
        return processNotify;
    }

    public void start() {
        if (isStarted) return;

        isStarted = true;
        isStopped = false;
        receiverThread = new MQReceiverThread(getName());
        receiverThread.start();
    }

    //关闭接收者，不在处理接收服务
    public void shutdown() {
        if (isStopped) return;

        isStopped = true;
        try {
            if (receiverThread != null) {
                receiverThread.shutdown();
            }
            receiverThread = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (executorService != null) {
            executorService.shutdown();
            try {
                executorService.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        isStarted = false;
        BMQFactory.unregistryMQReceiver(this);
    }

    public final boolean isStarted() {
        return isStarted;
    }

    public final boolean isStopped() {
        return isStopped;
    }

    class MQReceiverThread extends Thread {
        int breakCount = 128;

        MQReceiverThread(String id) {
            super("MQReceiverThread-" + id);
            System.out.println(this.getName() + " is started.");
        }

        public void run() {
            int i = 0;
            List<IMQMessage> messages;

            while (!isStopped) {
                try {
                    //这里应该是阻塞的接收消息
                    messages = receiveMessages();
                } catch (Exception e) {
                    ++i;
                    messages = null;
                    e.printStackTrace();
                }

                //1.判断消息是否为空
                if (messages == null || messages.size() == 0) {
                    if (isStopped) break;

                    if (i > breakCount) {
                        System.out.println("receive message is null, then more than 128 loops");
                        break;
                    }
                } else {
                    i = 0;
                    if (executorService != null) { //使用线程池去完成任务
                        List<IMQMessage> msg = messages;
                        executorService.submit(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    onMessage(msg);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else { //本地线程完成任务
                        try {
                            onMessage(messages);
                        } catch (BMQException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } //end while

            System.out.println(this.getName() + " is over.");
        }

        private void onMessage(List<IMQMessage> messages) throws BMQException {
            if (messages == null || messages.size() == 0) {
                System.out.println("onMessage() messages is null");
                return;
            }

            for (IMQFilter filter : filterList) {
                for (IMQMessage message : messages) {
                    filter.doReceiverFilter(message);
                }
            }

            if (processNotify != null) {
                for (IMQMessage message : messages) {
                    processNotify.doProcess(message);
                }
            } else {
                System.out.println("processNotify interface is null");
            }
        }

        public void shutdown() {
            close();
        }

    }

}
