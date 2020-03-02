package cn.rdtimes.cache.intf;

import cn.rdtimes.cache.BCacheFactory;


/**
 * 缓存接口实现基类
 *
 * @author BZ
 */
public abstract class BAbstractCacheBase implements ICacheBase {

    protected BAbstractCacheBase() {
        BCacheFactory.registryCacheBase(this.getName(), this);
    }

    public String getName() {
        return "BAbstractCacheInterface";
    }

    @Override
    public void close() {
        //ignore
    }

    @Override
    public ICacheBase set(String key, Object value, int ttl) {
        return this;
    }

    @Override
    public long ttl(String key) {
        throw new UnsupportedOperationException("not implement");
    }

    @Override
    public long del(String key) {
        return 0;
    }

    @Override
    public long del(String... keys) {
        return 0;
    }

    @Override
    public long incr(String key, long count) {
        return 0;
    }

    @Override
    public long decr(String key, long count) {
        return 0;
    }

    @Override
    public boolean expire(String key, int seconds) {
        return false;
    }

    @Override
    public Object get(String key) {
        return null;
    }

    @Override
    public boolean exists(String key) {
        return false;
    }

}
