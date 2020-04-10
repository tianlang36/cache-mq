package cn.rdtimes.impl.cache.redis;

import cn.rdtimes.cache.BCacheFactory;
import cn.rdtimes.cache.intf.ICacheAdvance;

/**
 * @description: 缓存创建帮助类
 * @author: BZ
 * @create: 2020/2/13
 */

public final class BCacheHelper {
    //redis缓存部署类型
    public enum RedisConnectionType {
        SINGLE,     //单机
        SHARD,      //分片
        CLUSTER     //集群
    }

    public final static String REDIS_NAME = "BZ-RedisCache";

    public static ICacheAdvance getRedisCache() {
        return BCacheFactory.getCacheAdvance(REDIS_NAME);
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
