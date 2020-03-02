package cn.rdtimes.impl.cache;

import cn.rdtimes.cache.BCacheFactory;
import cn.rdtimes.cache.intf.ICacheAdvance;
import cn.rdtimes.cache.intf.ICacheBase;
import cn.rdtimes.impl.cache.memcached.BMemcachedCache;
import cn.rdtimes.impl.cache.redis.BRedisCacheConfiguration;
import cn.rdtimes.impl.cache.redis.BRedisClusterCache;
import cn.rdtimes.impl.cache.redis.BRedisShardCache;
import cn.rdtimes.impl.cache.redis.BRedisSingleCache;

import java.net.InetSocketAddress;

/**
 * @description: 缓存创建帮助类，使用此类创建和获取缓存接口
 * @author: BZ
 */

public final class BCacheHelper {
    //redis缓存部署类型
    public enum RedisConnectionType {
        SINGLE,     //单机
        SHARD,      //分片
        CLUSTER     //集群
    }

    public final static String MEM_NAME = "BZ-MemcachedCache";
    public final static String REDIS_NAME = "BZ-RedisCache";

    public static ICacheBase getMemcachedCache() {
        return BCacheFactory.getCacheBase(MEM_NAME);
    }

    public static ICacheAdvance getRedisCache() {
        return BCacheFactory.getCacheAdvance(REDIS_NAME);
    }

    public static ICacheBase createMemcachedCache(InetSocketAddress... addresses) {
        return new BMemcachedCache(addresses);
    }

    public static ICacheAdvance createRedisCache(BRedisCacheConfiguration configuration) {
        return new BRedisSingleCache(configuration);
    }

    public static ICacheAdvance createRedisCache(BRedisCacheConfiguration configuration, RedisConnectionType rct) {
        if (rct == RedisConnectionType.CLUSTER) {
            return new BRedisClusterCache(configuration);
        } else if (rct == RedisConnectionType.SHARD) {
            return new BRedisShardCache(configuration);
        } else {
            return new BRedisSingleCache(configuration);
        }
    }

}
