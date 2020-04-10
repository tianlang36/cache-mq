package cn.rdtimes.impl.cache.memcached;

import cn.rdtimes.cache.BCacheFactory;
import cn.rdtimes.cache.intf.ICacheBase;

import java.net.InetSocketAddress;

/**
 * @description: 缓存创建帮助类
 * @author: BZ
 * @create: 2020/2/13
 */

public final class BCacheHelper {
    public final static String MEM_NAME = "BZ-MemcachedCache";

    public static ICacheBase getMemcachedCache() {
        return BCacheFactory.getCacheBase(MEM_NAME);
    }

    public static ICacheBase createMemcachedCache(InetSocketAddress... addresses) {
        return new BMemcachedCache(addresses);
    }

}
