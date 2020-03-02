package cn.rdtimes.impl.cache.redis;

import cn.rdtimes.cache.BCacheFactory;
import cn.rdtimes.cache.intf.BAbstractCacheBase;
import cn.rdtimes.cache.intf.ICacheAdvance;
import cn.rdtimes.cache.intf.ICacheBase;
import cn.rdtimes.impl.cache.BCacheHelper;
import redis.clients.jedis.*;
import redis.clients.jedis.params.SetParams;

import java.util.*;

/**
 * @description: redis集群实现
 * @author: BZ
 */

public class BRedisClusterCache extends BAbstractCacheBase implements ICacheAdvance {
    private final BRedisCacheConfiguration configuration;
    private JedisCluster jedisCluster;

    public BRedisClusterCache(BRedisCacheConfiguration configuration) {
        super();
        try {
            if (configuration == null || configuration.getIpAndPortAndPwd() == null) {
                throw new NullPointerException("configuration is null");
            }
            this.configuration = configuration;
            createConnectPool();
        } catch (Exception e) {
            BCacheFactory.unregistryCacheBase(getName());
            throw new IllegalStateException(e);
        }
    }

    private void createConnectPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(configuration.getMaxActive());
        poolConfig.setMaxIdle(configuration.getMaxIdle());
        poolConfig.setMaxWaitMillis(configuration.getMaxWaitMillis());
        poolConfig.setTestOnBorrow(configuration.isTestOnBorrow());
        poolConfig.setTestOnReturn(configuration.isTestOnReturn());
        poolConfig.setBlockWhenExhausted(configuration.isBlockWhenExhausted());
        poolConfig.setMinIdle(configuration.getMinIdle());
        poolConfig.setTimeBetweenEvictionRunsMillis(configuration.getTimeBetweenEvictionRunsMillis());
        poolConfig.setNumTestsPerEvictionRun(configuration.getNumTestsPerEvictionRun());
        poolConfig.setTestWhileIdle(configuration.isTestWhileIdle());
        poolConfig.setMinEvictableIdleTimeMillis(configuration.getMinEvictableIdleTimeMills());

        Set jedisClusterNode = new HashSet();
        String[] hosts = configuration.splitIpAndPortAndPwd();
        for (String hostport : hosts) {
            String[] ipport = configuration.alongIpAndPortAndPwd(hostport);
            String ip = ipport[0];
            int port = Integer.parseInt(ipport[1]);
            jedisClusterNode.add(new HostAndPort(ip, port));
        }

        jedisCluster = new JedisCluster(jedisClusterNode, 10000, 5000, poolConfig);
    }

    public String getName() {
        return BCacheHelper.REDIS_NAME;
    }


    /**
     * 释放资源
     */
    public void close() {
        if (jedisCluster != null) {
            jedisCluster.close();
        }
    }

    /**
     * 是否实现了高级接口
     *
     * @return true-有
     */
    public boolean hasAdvance() {
        return true;
    }

    /**
     * 设置key对应的值
     *
     * @param key
     * @param value
     * @param ttl   过期时间，0-不过期
     * @return 如果设置失败抛出异常
     */
    public ICacheBase set(String key, Object value, int ttl) {
        if (ttl > 0) {
            jedisCluster.set(key, String.valueOf(value), new SetParams().ex(ttl));
        } else {
            jedisCluster.set(key, String.valueOf(value));
        }
        return this;
    }

    public ICacheBase set(String key, Object value) {
        return set(key, value, 0);
    }

    /**
     * 获取key的有效秒数
     *
     * @param key
     * @return 返回有效秒数
     */
    public long ttl(String key) {
        return jedisCluster.ttl(key);
    }

    /**
     * 删除key对应的值
     *
     * @param key
     * @return 删除的数量
     */
    public long del(String key) {
        return jedisCluster.del(key);
    }

    /**
     * 删除key集合对应的值
     *
     * @param keys key集合
     * @return 成功删除key的数量
     */
    public long del(String... keys) {
        return jedisCluster.del(keys);
    }

    /**
     * 增加指定的数值
     *
     * @param key
     * @param count 数值
     * @return 增加后的数值
     */
    public long incr(String key, long count) {
        return jedisCluster.incrBy(key, count);
    }

    /**
     * 减少指定的数值
     *
     * @param key
     * @param count 数值
     * @return 减少后的数值
     */
    public long decr(String key, long count) {
        return jedisCluster.decrBy(key, count);
    }

    /**
     * 设置key的过期时间，单位秒
     *
     * @param key
     * @param seconds
     * @return true-设置成功
     */
    public boolean expire(String key, int seconds) {
        return jedisCluster.expire(key, seconds) == 0 ? false : true;
    }

    /**
     * 获取key对应的值
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return jedisCluster.get(key);
    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @return true-存在
     */
    public boolean exists(String key) {
        return jedisCluster.exists(key);
    }

    /**
     * 判断指定key集合是否存在
     *
     * @param keys
     * @return 存在的数量
     */
    public long exists(String... keys) {
        return jedisCluster.exists(keys);
    }

    /**
     * 返回 key 指定的哈希集中该字段所关联的值
     *
     * @param key
     * @param field
     * @return
     */
    public String hget(String key, String field) {
        return jedisCluster.hget(key, field);
    }

    /**
     * 返回 key 指定的哈希集中指定字段的值
     *
     * @param key
     * @param fields
     * @return 如果对应的field的值不存在返回空值
     */
    public List<String> hget(String key, String... fields) {
        return jedisCluster.hmget(key, fields);
    }

    /**
     * 返回 key 指定的哈希集中所有的字段和值
     *
     * @param key
     * @return
     */
    public Map<String, String> hgetAll(String key) {
        return jedisCluster.hgetAll(key);
    }

    /**
     * 返回 key 指定的哈希集中所有字段的名字
     *
     * @param key
     * @return
     */
    public Set<String> hkeys(String key) {
        return jedisCluster.hkeys(key);
    }

    /**
     * 设置 key 指定的哈希集中指定字段的值
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public ICacheAdvance hset(String key, String field, String value) {
        jedisCluster.hset(key, field, value);
        return this;
    }

    /**
     * 设置key指定的哈希集中多个字段的值，如果提供原生态实现请使用它
     *
     * @param key
     * @param hash
     * @return
     */
    public ICacheAdvance hset(String key, Map<String, String> hash) {
        jedisCluster.hset(key, hash);
        return this;
    }

    /**
     * 从 key 指定的哈希集中移除指定的域
     *
     * @param key
     * @param fields
     * @return 返回从哈希集中成功移除的域的数量，不包括不存在的那些域
     */
    public Long hdel(String key, String... fields) {
        return jedisCluster.hdel(key, fields);
    }

    /**
     * 返回hash里面field是否存在
     *
     * @param key
     * @param field
     * @return true-存在
     */
    public Boolean hexists(String key, String field) {
        return jedisCluster.hexists(key, field);
    }

    /**
     * 返回 key 指定的哈希集包含的字段的数量
     *
     * @param key
     * @return
     */
    public Long hlen(String key) {
        return jedisCluster.hlen(key);
    }

    /**
     * 返回 key 指定的哈希集中所有字段的值
     *
     * @param key
     * @return
     */
    public List<String> hvals(String key) {
        return jedisCluster.hvals(key);
    }


    /////////////////////list/////////////////////

    /**
     * 插入指定项的前面或后面
     *
     * @param key
     * @param isBefore true-插入前面，否则后面
     * @param pivot    基准值
     * @param value
     * @return 插入操作后的list长度，或者当 pivot 值找不到的时候返回 -1
     */
    public Long linsert(String key, boolean isBefore, String pivot, String value) {
        return jedisCluster.linsert(key, isBefore ? ListPosition.BEFORE : ListPosition.AFTER, pivot, value);
    }

    /**
     * 返回存储在 key 里的list的长度
     *
     * @param key
     * @return
     */
    public Long llen(String key) {
        return jedisCluster.llen(key);
    }

    /**
     * 移除并且返回 key 对应的 list 的第一个元素
     *
     * @param key
     * @return 当 key 不存在时返回空值
     */
    public String lpop(String key) {
        return jedisCluster.lpop(key);
    }

    /**
     * 将所有指定的值插入到存于 key 的列表的头部
     *
     * @param key
     * @param strings
     * @return push后的长度
     */
    public Long lpush(String key, String... strings) {
        return jedisCluster.lpush(key, strings);
    }

    /**
     * 返回存储在 key 的列表里指定范围内的元素
     *
     * @param key
     * @param start 开始位置
     * @param end   结束位置
     * @return 指定范围里的列表元素
     */
    public List<String> lrange(String key, long start, long end) {
        return jedisCluster.lrange(key, start, end);
    }

    /**
     * 从存于 key 的列表里移除前 count 次出现的值为 value 的元素
     * count > 0: 从头往尾移除值为 value 的元素
     * count < 0: 从尾往头移除值为 value 的元素
     * count = 0: 移除所有值为 value 的元素
     *
     * @param key
     * @param count 数量
     * @param value 值
     * @return 被移除的元素个数
     */
    public Long lrem(String key, long count, String value) {
        return jedisCluster.lrem(key, count, value);
    }

    /**
     * 设置 index 位置的list元素的值为 value
     *
     * @param key
     * @param index 索引位置
     * @param value
     */
    public ICacheAdvance lset(String key, long index, String value) {
        jedisCluster.lset(key, index, value);
        return this;
    }

    /**
     * 修剪(trim)一个已存在的 list，这样 list 就会只包含指定范围的指定元素
     *
     * @param key
     * @param start 开始位置
     * @param end   结束位置
     */
    public ICacheAdvance ltrim(String key, long start, long end) {
        jedisCluster.ltrim(key, start, end);
        return this;
    }

    /**
     * 移除并返回存于 key 的 list 的最后一个元素
     *
     * @param key
     * @return key 不存在的时候返回空值
     */
    public String rpop(String key) {
        return jedisCluster.rpop(key);
    }

    /**
     * 将所有指定的值插入到存于 key 的列表的尾部
     *
     * @param key
     * @param strings
     * @return push后的长度
     */
    public Long rpush(String key, String... strings) {
        return jedisCluster.rpush(key, strings);
    }

    /////////////////////set/////////////////////

    public Long sadd(String key, String member) {
        return jedisCluster.sadd(key, member);
    }

    /**
     * 添加一个或多个指定的member元素到集合的 key中.指定的一个或者多个元素member
     * 如果已经在集合key中存在则忽略.如果集合key 不存在，则新建集合key,并添加member元素到集合key中
     *
     * @param key
     * @param members
     * @return 返回新成功添加到集合里元素的数量，不包括已经存在于集合中的元素
     */
    public Long sadd(String key, String... members) {
        return jedisCluster.sadd(key, members);
    }

    /**
     * 集合的基数(元素的数量),如果key不存在,则返回 0
     *
     * @param key
     * @return
     */
    public Long scard(String key) {
        return jedisCluster.scard(key);
    }

    /**
     * 返回一个集合与给定集合的差集的元素
     *
     * @param keys
     * @return 结果集
     */
    public Set<String> sdiff(String... keys) {
        return jedisCluster.sdiff(keys);
    }

    /**
     * 返回一个集合与给定集合的交集的元素
     *
     * @param keys
     * @return 结果集
     */
    public Set<String> sinter(String... keys) {
        return jedisCluster.sinter(keys);
    }

    /**
     * 返回成员 member 是否是存储的集合 key的成员
     *
     * @param key
     * @param member
     * @return true-存在
     */
    public Boolean sismember(String key, String member) {
        return jedisCluster.sismember(key, member);
    }

    /**
     * 返回key集合所有的元素
     *
     * @param key
     * @return
     */
    public Set<String> smembers(String key) {
        return jedisCluster.smembers(key);
    }

    public Long srem(String key, String member) {
        return jedisCluster.srem(key, member);
    }

    /**
     * 在key集合中移除指定的元素
     *
     * @param key
     * @param members
     * @return 在key集合中移除指定的元素
     */
    public Long srem(String key, String... members) {
        return jedisCluster.srem(key, members);
    }

    /**
     * 返回一个集合与给定集合的并集的元素
     *
     * @param keys
     * @return 结果集
     */
    public Set<String> sunion(String... keys) {
        return jedisCluster.sunion(keys);
    }

    /////////////////////sortedSet/////////////////////

    /**
     * 将所有指定成员添加到键为key有序集合（sorted set）里面
     *
     * @param key
     * @param score
     * @param member
     * @return 添加到有序集合的成员数量，不包括已经存在更新分数的成员
     */
    public Long zadd(String key, double score, String member) {
        return jedisCluster.zadd(key, score, member);
    }

    /**
     * 如果原始提供方法请使用它
     *
     * @param key
     * @param scoreMembers
     * @return 添加到有序集合的成员数量，不包括已经存在更新分数的成员
     */
    public Long zadd(String key, Map<String, Double> scoreMembers) {
        return jedisCluster.zadd(key, scoreMembers);
    }

    /**
     * 返回key的有序集元素个数
     *
     * @param key
     * @return key不存在返回0
     */
    public Long zcard(String key) {
        return jedisCluster.zcard(key);
    }

    /**
     * 返回有序集key中，score值在min和max之间(默认包括score值等于min或max)的成员
     *
     * @param key
     * @param min
     * @param max
     * @return 指定分数范围的元素个数
     */
    public Long zcount(String key, String min, String max) {
        return jedisCluster.zcount(key, min, max);
    }

    /**
     * 为有序集key的成员member的score值加上增量increment
     *
     * @param key
     * @param score  分数
     * @param member
     * @return
     */
    public Double zincrby(String key, double score, String member) {
        return jedisCluster.zincrby(key, score, member);
    }

    /**
     * 返回存储在有序集合key中的指定范围的元素
     *
     * @param key
     * @param start 开始位置
     * @param end   结束位置
     * @return 给定范围内的元素列表
     */
    public Set<String> zrange(String key, long start, long end) {
        return jedisCluster.zrange(key, start, end);
    }

    /**
     * 返回有序集key中成员member的排名
     *
     * @param key
     * @param member
     * @return member的排名
     */
    public Long zrank(String key, String member) {
        return jedisCluster.zrank(key, member);
    }

    /**
     * 返回的是从有序集合中删除的成员个数，不包括不存在的成员
     *
     * @param key
     * @param members
     * @return
     */
    public Long zrem(String key, String... members) {
        return jedisCluster.zrem(key, members);
    }

    public Long zrem(String key, String member) {
        return jedisCluster.zrem(key, member);
    }

    /**
     * 移除有序集key中，所有score值介于min和max之间(包括等于min或max)的成员
     *
     * @param key
     * @param start 开始位置
     * @param end   结束位置
     * @return 删除的元素的个数
     */
    public Long zremrangeByScore(String key, double start, double end) {
        return jedisCluster.zremrangeByScore(key, start, end);
    }

    /**
     * 返回有序集key中，指定区间内的成员。其中成员的位置按score值递减(从大到小)来排列。
     *
     * @param key
     * @param start 开始位置
     * @param end   结束位置
     * @return 指定范围的元素列表
     */
    public Set<String> zrevrange(String key, long start, long end) {
        return jedisCluster.zrevrange(key, start, end);
    }

    /**
     * 返回有序集key中成员member的排名，其中有序集成员按score值从大到小排列
     *
     * @param key
     * @param member
     * @return
     */
    public Long zrevrank(String key, String member) {
        return jedisCluster.zrevrank(key, member);
    }

    /**
     * 返回有序集key中，成员member的score值
     *
     * @param key
     * @param member
     * @return
     */
    public Double zscore(String key, String member) {
        return jedisCluster.zscore(key, member);
    }

    /////////////////////Geo/////////////////////

    /**
     * 将指定的地理空间位置（纬度、经度、名称）添加到指定的key中
     *
     * @param key
     * @param longitude 经度
     * @param latitude  纬度
     * @param member    名称
     * @return 返回添加的数目
     */
    public Long geoadd(String key, double longitude, double latitude, String member) {
        return jedisCluster.geoadd(key, longitude, latitude, member);
    }

    /**
     * 返回两个给定位置之间的距离，默认使用米作为单位
     *
     * @param key
     * @param member1
     * @param member2
     * @return
     */
    public Double geodist(String key, String member1, String member2) {
        return jedisCluster.geodist(key, member1, member2);
    }

    /**
     * 从key里返回所有给定位置元素的位置（经度和纬度）
     *
     * @param key
     * @param members
     * @return 当给定的位置元素不存在时， 对应的数组项为空值
     */
    public List<BGeoCoordinate> geopos(String key, String... members) {
        List<GeoCoordinate> list = jedisCluster.geopos(key, members);
        if (list == null) return Collections.EMPTY_LIST;

        List<BGeoCoordinate> li = new ArrayList<BGeoCoordinate>(list.size());
        for (GeoCoordinate go : list) {
            if (go == null) {
                li.add(null);
            } else {
                BGeoCoordinate bo = new BGeoCoordinate(go.getLongitude(), go.getLatitude());
                li.add(bo);
            }
        }
        return li;
    }

}
