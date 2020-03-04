package cn.rdtimes.impl.mq.rabbit;

import com.rabbitmq.client.*;
import com.rabbitmq.utility.Utility;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @description: 消费队列
 * @author: BZ
 */
class BQueueingConsumer extends DefaultConsumer {
    private final BlockingQueue<Delivery> _queue;

    private volatile ShutdownSignalException _shutdown;
    private volatile ConsumerCancelledException _cancelled;

    private static final Delivery POISON = new Delivery(null, null, null);

    public BQueueingConsumer(Channel ch) {
        this(ch, new LinkedBlockingQueue<Delivery>());
    }

    public BQueueingConsumer(Channel ch, BlockingQueue<Delivery> q) {
        super(ch);
        this._queue = q;
    }

    @Override
    public void handleShutdownSignal(String consumerTag,
                                     ShutdownSignalException sig) {
        _shutdown = sig;
        _queue.add(POISON);
    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {
        _cancelled = new ConsumerCancelledException();
        _queue.add(POISON);
    }

    @Override
    public void handleDelivery(String consumerTag,
                               Envelope envelope,
                               AMQP.BasicProperties properties,
                               byte[] body)
            throws IOException {
        checkShutdown();
        this._queue.add(new Delivery(envelope, properties, body));
    }

    public static class Delivery {
        private final Envelope _envelope;
        private final AMQP.BasicProperties _properties;
        private final byte[] _body;

        public Delivery(Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
            _envelope = envelope;
            _properties = properties;
            _body = body;
        }

        public Envelope getEnvelope() {
            return _envelope;
        }

        public BasicProperties getProperties() {
            return _properties;
        }

        public byte[] getBody() {
            return _body;
        }
    }

    private void checkShutdown() {
        if (_shutdown != null)
            throw Utility.fixStackTrace(_shutdown);
    }

    private Delivery handle(Delivery delivery) {
        if (delivery == POISON ||
                delivery == null && (_shutdown != null || _cancelled != null)) {
            if (delivery == POISON) {
                _queue.add(POISON);
                if (_shutdown == null && _cancelled == null) {
                    throw new IllegalStateException(
                            "POISON in queue, but null _shutdown and null _cancelled. " +
                                    "This should never happen, please report as a BUG");
                }
            }
            if (null != _shutdown)
                throw Utility.fixStackTrace(_shutdown);
            if (null != _cancelled)
                throw Utility.fixStackTrace(_cancelled);
        }
        return delivery;
    }

    public Delivery nextDelivery()
            throws InterruptedException, ShutdownSignalException, ConsumerCancelledException {
        return handle(_queue.take());
    }

    public Delivery nextDelivery(long timeout)
            throws InterruptedException, ShutdownSignalException, ConsumerCancelledException {
        return handle(_queue.poll(timeout, TimeUnit.MILLISECONDS));
    }

}

