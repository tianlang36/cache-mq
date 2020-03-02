package cn.rdtimes.cache.intf;

import java.util.ArrayList;
import java.util.List;


/**
 * @description: 缓存基础接口
 * @author: BZ
 */

public interface ICacheBase {

    /**
     * 获取名称，要唯一
     *
     * @return
     */
    String getName();

    /**
     * 释放资源
     */
    void close();

    /**
     * 是否实现了高级接口
     * @return true-有
     */
    default boolean hasAdvance() {
        return false;
    }

    /**
     * 设置key对应的值
     *
     * @param key
     * @param value
     * @param ttl   过期时间，0-不过期
     * @return
     */
    ICacheBase set(String key, Object value, int ttl) ;

    default ICacheBase set(String key, Object value) {
        return set(key, value, 0);
    }

    /**
     * 获取key的有效秒数
     * @param key
     * @return 返回有效秒数
     */
    long ttl(String key);

    /**
     * 删除key对应的值
     *
     * @param key
     * @return 删除的数量
     */
    long del(String key);

    /**
     * 删除key集合对应的值
     *
     * @param keys key集合
     * @return 成功删除key的数量
     */
    long del(String... keys);

    /**
     * 增加指定的数值
     *
     * @param key
     * @param count 数值
     * @return 增加后的数值
     */
    long incr(String key, long count);

    /**
     * 减少指定的数值
     *
     * @param key
     * @param count 数值
     * @return 减少后的数值
     */
    long decr(String key, long count);

    /**
     * 设置key的过期时间，单位秒
     *
     * @param key
     * @param seconds
     * @return true-设置成功
     */
    boolean expire(String key, int seconds);

    /**
     * 获取key对应的值
     *
     * @param key
     * @return
     */
    Object get(String key);

    /**
     * 获取key集合对应的值
     *
     * @param keys
     * @return
     */
    default List<Object> get(String... keys) {
        List<Object> list = new ArrayList(1);
        for (String key : keys) {
            list.add(get(key));
        }
        return list;
    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @return true-存在
     */
    boolean exists(String key);

    /**
     * 判断指定key集合是否存在
     *
     * @param keys
     * @return 存在的数量
     */
    default long exists(String... keys) {
        long count = 0;
        for (String key : keys) {
            if (exists(key)) ++count;
        }
        return count;
    }

}
