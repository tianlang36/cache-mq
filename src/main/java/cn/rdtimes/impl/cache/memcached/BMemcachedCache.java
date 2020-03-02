package cn.rdtimes.impl.cache.memcached;

import cn.rdtimes.cache.BCacheFactory;
import cn.rdtimes.cache.intf.BAbstractCacheBase;
import cn.rdtimes.cache.intf.ICacheBase;
import cn.rdtimes.impl.cache.BCacheHelper;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @description: memcached缓存实现
 * @author: BZ
 */

public class BMemcachedCache extends BAbstractCacheBase {
    private final MemcachedClient client;

    public BMemcachedCache(InetSocketAddress... addresses) {
        super();
        try {
            client = new MemcachedClient(addresses);
            client.waitForQueues(30, TimeUnit.SECONDS);
        } catch (IOException ioe) {
            BCacheFactory.unregistryCacheBase(getName());
            throw new IllegalStateException(ioe);
        }
    }

    @Override
    public String getName() {
        return BCacheHelper.MEM_NAME;
    }

    public boolean hasAdvance() {
        return false;
    }

    @Override
    public void close() {
        client.shutdown();
    }

    @Override
    public ICacheBase set(String key, Object value, int ttl)  {
        client.set(key, ttl, value);
        return this;
    }

    @Override
    public long ttl(String key) {
        throw new UnsupportedOperationException("not implement");
    }

    @Override
    public long del(String key) {
        try {
            return (client.delete(key).get() ? 1 : 0);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public long del(String... keys) {
        int ret = 0;
        for (String key : keys) {
            ret += del(key);
        }
        return ret;
    }

    @Override
    public long incr(String key, long count) {
        return client.incr(key, count, count);
    }

    @Override
    public long decr(String key, long count) {
        return client.decr(key, count, 0L);
    }

    @Override
    public boolean expire(String key, int seconds) {
        Object value = get(key);
        if (value != null) {
            set(key, value, seconds);
            return true;
        }
        return false;
    }

    @Override
    public Object get(String key) {
        return client.get(key);
    }

    @Override
    public boolean exists(String key) {
        return get(key) != null;
    }

}
