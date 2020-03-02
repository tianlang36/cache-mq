package cn.rdtimes.cache;

import cn.rdtimes.cache.intf.ICacheAdvance;
import cn.rdtimes.cache.intf.ICacheBase;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description 缓存工厂，获取缓存实现
 * 1.创建ICacheBase实例，并注册到工厂中，可以注册一系列的对象
 *    BCacheFactory.registryCacheBase(new RedisCache());
 * 2.使用时通过工厂获取实例
 *    BCacheFactory.getCacheBase("id").get("key")
 *
 * @author BZ
 */

public final class BCacheFactory {
    private static final Map<String, ICacheBase> cacheMap = new ConcurrentHashMap<>(2);

    public static void registryCacheBase(ICacheBase cacheBase) {
        if (cacheBase == null) {
            throw new NullPointerException("cacheBase is null");
        }

        registryCacheBase(cacheBase.getName(), cacheBase);
    }

    public static void registryCacheBase(String id, ICacheBase cacheBase) {
        if (id == null || cacheBase == null) {
            throw new NullPointerException("id or cacheBase is null");
        }
        cacheMap.putIfAbsent(id, cacheBase);
    }

    public static void unregistryCacheBase(String id) {
        if (id == null) {
            throw new NullPointerException("id is null");
        }
        cacheMap.remove(id);
    }

    public static void unregistryGetCacheBase(ICacheBase cacheBase) {
        if (cacheBase == null) {
            throw new NullPointerException("cacheBase is null");
        }

        unregistryCacheBase(cacheBase.getName());
    }

    public static ICacheBase getCacheBase(String id) {
        return cacheMap.get(id);
    }

    public static ICacheAdvance getCacheAdvance(String id) {
        ICacheBase cacheBase = getCacheBase(id);
        if (cacheBase != null) {
            if (cacheBase.hasAdvance()) {
                return (ICacheAdvance) cacheBase;
            }
        }
        throw new UnsupportedOperationException("not implement");
    }

    public static void shutdown() {
        for (ICacheBase cacheBase : cacheMap.values()) {
            shutdown(cacheBase);
        }
    }

    public static void shutdown(ICacheBase cacheBase) {
        if (cacheBase != null) {
            try {
                cacheBase.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void shutdown(String id) {
        shutdown(getCacheBase(id));
    }

    public static Map<String, ICacheBase> getCacheMap() {
        return cacheMap;
    }

}
