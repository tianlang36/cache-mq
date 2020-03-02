package cn.rdtimes.cache.intf;


import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description: 缓存高级接口，有的缓存产品可能没有这样的高级接口
 * 参考了redis的api
 * @author: BZ
 */

public interface ICacheAdvance extends ICacheBase {

    /////////////////////hashes/////////////////////

    /**
     * 返回 key 指定的哈希集中该字段所关联的值
     *
     * @param key
     * @param field
     * @return
     */
    String hget(String key, String field);

    /**
     * 返回 key 指定的哈希集中指定字段的值
     *
     * @param key
     * @param fields
     * @return 如果对应的field的值不存在返回空值
     */
    List<String> hget(String key, String... fields);

    /**
     * 返回 key 指定的哈希集中所有的字段和值
     *
     * @param key
     * @return
     */
    Map<String, String> hgetAll(String key);

    /**
     * 返回 key 指定的哈希集中所有字段的名字
     *
     * @param key
     * @return
     */
    Set<String> hkeys(String key);

    /**
     * 设置 key 指定的哈希集中指定字段的值
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    ICacheAdvance hset(String key, String field, String value);

    /**
     * 设置key指定的哈希集中多个字段的值，如果提供原生态实现请使用它
     *
     * @param key
     * @param hash
     * @return
     */
    default ICacheAdvance hset(String key, Map<String, String> hash) {
        for (Map.Entry<String, String> entry : hash.entrySet()) {
            hset(key, entry.getKey(), entry.getValue());
        }
        return this;
    }

    /**
     * 从 key 指定的哈希集中移除指定的域
     *
     * @param key
     * @param fields
     * @return 返回从哈希集中成功移除的域的数量，不包括不存在的那些域
     */
    Long hdel(String key, String... fields);

    /**
     * 返回hash里面field是否存在
     *
     * @param key
     * @param field
     * @return true-存在
     */
    Boolean hexists(String key, String field);

    /**
     * 返回 key 指定的哈希集包含的字段的数量
     *
     * @param key
     * @return
     */
    Long hlen(String key);

    /**
     * 返回 key 指定的哈希集中所有字段的值
     *
     * @param key
     * @return
     */
    List<String> hvals(String key);


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
    Long linsert(String key, boolean isBefore, String pivot, String value);

    /**
     * 返回存储在 key 里的list的长度
     *
     * @param key
     * @return
     */
    Long llen(String key);

    /**
     * 移除并且返回 key 对应的 list 的第一个元素
     *
     * @param key
     * @return 当 key 不存在时返回空值
     */
    String lpop(String key);

    /**
     * 将所有指定的值插入到存于 key 的列表的头部
     *
     * @param key
     * @param strings
     * @return push后的长度
     */
    Long lpush(String key, String... strings);

    /**
     * 返回存储在 key 的列表里指定范围内的元素
     *
     * @param key
     * @param start 开始位置
     * @param end   结束位置
     * @return 指定范围里的列表元素
     */
    List<String> lrange(String key, long start, long end);

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
    Long lrem(String key, long count, String value);

    /**
     * 设置 index 位置的list元素的值为 value
     *
     * @param key
     * @param index 索引位置
     * @param value
     */
    ICacheAdvance lset(String key, long index, String value);

    /**
     * 修剪(trim)一个已存在的 list，这样 list 就会只包含指定范围的指定元素
     *
     * @param key
     * @param start 开始位置
     * @param end   结束位置
     */
    ICacheAdvance ltrim(String key, long start, long end);

    /**
     * 移除并返回存于 key 的 list 的最后一个元素
     *
     * @param key
     * @return key 不存在的时候返回空值
     */
    String rpop(String key);

    /**
     * 将所有指定的值插入到存于 key 的列表的尾部
     *
     * @param key
     * @param strings
     * @return push后的长度
     */
    Long rpush(String key, String... strings);

    /////////////////////set/////////////////////

    Long sadd(String key, String member);

    /**
     * 添加一个或多个指定的member元素到集合的 key中.指定的一个或者多个元素member
     * 如果已经在集合key中存在则忽略.如果集合key 不存在，则新建集合key,并添加member元素到集合key中
     *
     * @param key
     * @param members
     * @return 返回新成功添加到集合里元素的数量，不包括已经存在于集合中的元素
     */
    Long sadd(String key, String... members);

    /**
     * 集合的基数(元素的数量),如果key不存在,则返回 0
     *
     * @param key
     * @return
     */
    Long scard(String key);

    /**
     * 返回一个集合与给定集合的差集的元素
     *
     * @param keys
     * @return 结果集
     */
    Set<String> sdiff(String... keys);

    /**
     * 返回一个集合与给定集合的交集的元素
     *
     * @param keys
     * @return 结果集
     */
    Set<String> sinter(String... keys);

    /**
     * 返回成员 member 是否是存储的集合 key的成员
     *
     * @param key
     * @param member
     * @return true-存在
     */
    Boolean sismember(String key, String member);

    /**
     * 返回key集合所有的元素
     *
     * @param key
     * @return
     */
    Set<String> smembers(String key);

    Long srem(String key, String member);

    /**
     * 在key集合中移除指定的元素
     *
     * @param key
     * @param members
     * @return 从集合中移除元素的个数，不包括不存在的成员
     */
    Long srem(String key, String... members);

    /**
     * 返回一个集合与给定集合的并集的元素
     *
     * @param keys
     * @return 结果集
     */
    Set<String> sunion(String... keys);

    /////////////////////sortedSet/////////////////////

    /**
     * 将所有指定成员添加到键为key有序集合（sorted set）里面
     *
     * @param key
     * @param score
     * @param member
     * @return 添加到有序集合的成员数量，不包括已经存在更新分数的成员
     */
    Long zadd(String key, double score, String member);

    /**
     * 如果原始提供方法请使用它
     *
     * @param key
     * @param scoreMembers
     * @return 添加到有序集合的成员数量，不包括已经存在更新分数的成员
     */
    default Long zadd(String key, Map<String, Double> scoreMembers) {
        long count = 0;
        for (Map.Entry<String, Double> entry : scoreMembers.entrySet()) {
            count += zadd(key, entry.getValue(), entry.getKey());
        }
        return count;
    }

    /**
     * 返回key的有序集元素个数
     *
     * @param key
     * @return key不存在返回0
     */
    Long zcard(String key);

    /**
     * 返回有序集key中，score值在min和max之间(默认包括score值等于min或max)的成员
     *
     * @param key
     * @param min
     * @param max
     * @return 指定分数范围的元素个数
     */
    Long zcount(String key, String min, String max);

    /**
     * 为有序集key的成员member的score值加上增量increment
     *
     * @param key
     * @param score  分数
     * @param member
     * @return
     */
    Double zincrby(String key, double score, String member);

    /**
     * 返回存储在有序集合key中的指定范围的元素
     *
     * @param key
     * @param start 开始位置
     * @param end   结束位置
     * @return 给定范围内的元素列表
     */
    Set<String> zrange(String key, long start, long end);

    /**
     * 返回有序集key中成员member的排名
     *
     * @param key
     * @param member
     * @return member的排名
     */
    Long zrank(String key, String member);

    /**
     * 返回的是从有序集合中删除的成员个数，不包括不存在的成员
     *
     * @param key
     * @param members
     * @return
     */
    Long zrem(String key, String... members);

    Long zrem(String key, String member);

    /**
     * 移除有序集key中，所有score值介于min和max之间(包括等于min或max)的成员
     *
     * @param key
     * @param start 开始位置
     * @param end   结束位置
     * @return 删除的元素的个数
     */
    Long zremrangeByScore(String key, double start, double end);

    /**
     * 返回有序集key中，指定区间内的成员。其中成员的位置按score值递减(从大到小)来排列。
     *
     * @param key
     * @param start 开始位置
     * @param end   结束位置
     * @return 指定范围的元素列表
     */
    Set<String> zrevrange(String key, long start, long end);

    /**
     * 返回有序集key中成员member的排名，其中有序集成员按score值从大到小排列
     *
     * @param key
     * @param member
     * @return member的排名
     */
    Long zrevrank(String key, String member);

    /**
     * 返回有序集key中，成员member的score值
     *
     * @param key
     * @param member
     * @return
     */
    Double zscore(String key, String member);

    /////////////////////Geo/////////////////////
    ////删除操作使用sortedset中的////

    /**
     * @description: 地理位置对象
     */

    class BGeoCoordinate {
        private double longitude;
        private double latitude;

        public BGeoCoordinate(double longitude, double latitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public double getLatitude() {
            return latitude;
        }

    }
    /**
     * 将指定的地理空间位置（纬度、经度、名称）添加到指定的key中
     *
     * @param key
     * @param longitude 经度
     * @param latitude  纬度
     * @param member    名称
     * @return 返回添加的数目
     */
    Long geoadd(String key, double longitude, double latitude, String member);

    /**
     * 返回两个给定位置之间的距离，默认使用米作为单位
     *
     * @param key
     * @param member1
     * @param member2
     * @return
     */
    Double geodist(String key, String member1, String member2);

    /**
     * 从key里返回所有给定位置元素的位置（经度和纬度）
     *
     * @param key
     * @param members
     * @return 当给定的位置元素不存在时， 对应的数组项为空值
     */
    List<BGeoCoordinate> geopos(String key, String... members);

    default Long georem(String key, String member) {
        return zrem(key, member);
    }

    /**
     * 删除key中的所有成员，不包括不存在的成员
     *
     * @param key
     * @param members
     * @return 返回删除的数量
     */
    default Long georem(String key, String... members) {
        return zrem(key, members);
    }

}
