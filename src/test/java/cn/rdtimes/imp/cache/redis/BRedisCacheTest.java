package cn.rdtimes.imp.cache.redis;

import cn.rdtimes.cache.BCacheFactory;
import cn.rdtimes.cache.intf.ICacheAdvance;
import cn.rdtimes.cache.intf.ICacheBase;
import cn.rdtimes.impl.cache.BCacheHelper;
import cn.rdtimes.impl.cache.redis.BRedisCacheConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description: mem缓存测试
 * @author: BZ
 */

public class BRedisCacheTest {
    private long start, end;

    @Before
    public void setup() {
        BRedisCacheConfiguration configuration = new BRedisCacheConfiguration();
        configuration.setIpAndPortAndPwd("localhost:6379");
        configuration.setMinIdle(1);
        BCacheHelper.createRedisCache(configuration, BCacheHelper.RedisConnectionType.SINGLE);
        start = System.currentTimeMillis();
    }

    @Test
    public void test() throws Exception {
        ICacheBase redisCache = BCacheHelper.getRedisCache();

        System.out.println("name:" + redisCache.getName() + "|BZ-RedisCache");
        System.out.println("hasAdvance:" + redisCache.hasAdvance() + "|true");
        redisCache.set("1", "1", 1);    //ok
        System.out.println("get:" + redisCache.get("1") + "|1");
        redisCache.set("2", "2");   //ok
        System.out.println("get:" + redisCache.get("2") + "|2");
        System.out.println("del:" + redisCache.del("2") + "|1");
        System.out.println("get:" + redisCache.get("2") + "|null");
        System.out.println("ttl:" + redisCache.ttl("1") + "|1");
        System.out.println("incr:" + redisCache.incr("3", 1) + "|1");
        System.out.println("incr:" + redisCache.incr("3", 1) + "|2");
        System.out.println("incr:" + redisCache.incr("3", 1) + "|3");
        System.out.println("decr:" + redisCache.decr("4", 0) + "|0");
        System.out.println("incr:" + redisCache.incr("4", 5) + "|5");
        System.out.println("decr:" + redisCache.decr("4", 2) + "|3");
        System.out.println("decr:" + redisCache.decr("3", 3) + "|0");
        System.out.println("decr:" + redisCache.decr("3", 3) + "|-3");
        System.out.println("get:" + redisCache.get("4") + "|3");
        System.out.println("del:" + redisCache.del("3", "4") + "|2");
        System.out.println("exist:" + redisCache.exists("1") + "|true");
        System.out.println("exist:" + redisCache.exists("3", "4") + "|0");
        redisCache.set("2", "2");
        System.out.println("expire:" + redisCache.expire("2", 2) + "|true");
        System.out.println("get:" + redisCache.get("2") + "|2");

        List<Object> list = redisCache.get("1", "2", "3", "4"); //1,2
        for (Object obj : list) {
            System.out.println("list:" + obj);
        }

        Thread.sleep(3000);

        System.out.println("get:" + redisCache.get("1") + "|null");
        System.out.println("get:" + redisCache.get("2") + "|null");
        redisCache.del("5");
        System.out.println("get:" + redisCache.get("5") + "|null");
        redisCache.set("5", "5");
        System.out.println("get:" + redisCache.get("5") + "|5");
        redisCache.set("5", "6");
        System.out.println("get:" + redisCache.get("5") + "|6");
    }

    @Test
    public void testAdvH() {
        ICacheAdvance redisCache = BCacheHelper.getRedisCache();
        redisCache.del("1","2","3","4","5","bz");
        redisCache.hset("bz", "1", "1");
        Map<String, String> map = new HashMap<String, String>(2);
        map.put("1", "2");
        map.put("2", "3");
        redisCache.hset("bz", map);
        System.out.println("hget:" + redisCache.hget("bz", "1") + "|2");
        System.out.println("hget:" + redisCache.hget("bz", "3") + "|null");
        System.out.println("hget:" + redisCache.hget("bz2", "1") + "|null");
        map = redisCache.hgetAll("bz");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println("hgetall:" + entry.getKey() + "," + entry.getValue());
        }
        List<String> list = redisCache.hget("bz", "1", "2", "3");
        for (String s : list) {
            System.out.println("hgets:" + s);
        }
        Set<String> set = redisCache.hkeys("bz");
        for (String s : set) {
            System.out.println("hkeys:" + s);
        }
        System.out.println("hlen:" + redisCache.hlen("bz") + "|2");
        System.out.println("hexist:" + redisCache.hexists("bz", "1") + "|true");
        System.out.println("hexist:" + redisCache.hexists("bz", "34") + "|false");
        list = redisCache.hvals("bz");
        for (String s : list) {
            System.out.println("hvals:" + s);
        }
        System.out.println("hdel:" + redisCache.hdel("bz", "1", "2", "3") + "|2");
        System.out.println("hlen:" + redisCache.hlen("bz") + "|0");
    }

    @Test
    public void testAdvL() {
        ICacheAdvance redisCache = BCacheHelper.getRedisCache();
        redisCache.del("bz");
        System.out.println("lpush:" + redisCache.lpush("bz", "1", "2") + "|2");
        redisCache.lset("bz", 1, "11");
        System.out.println("llen:" + redisCache.llen("bz") + "|2");
        System.out.println("linsert:" + redisCache.linsert("bz", true, "11", "01") + "|3");
        List<String> list = redisCache.lrange("bz", 0, 4);
        for (String s : list) { //2,01,11
            System.out.println("lrange:" + s);
        }
        System.out.println("rpush:" + redisCache.rpush("bz", "3"));
        System.out.println("rpop:" + redisCache.rpop("bz") + "|3");
        System.out.println("lpop:" + redisCache.lpop("bz") + "|2");
        System.out.println("lrem:" + redisCache.lrem("bz", 1, "11"));
        System.out.println("ltrim:" + redisCache.ltrim("bz", 0, 0));
        System.out.println("rpop:" + redisCache.rpop("bz") + "|01");
        System.out.println("llen:" + redisCache.llen("bz") + "|0");
    }

    @Test
    public void testAdvS() {
        ICacheAdvance redisCache = BCacheHelper.getRedisCache();
        redisCache.del("bz");
        redisCache.del("bz1");
        System.out.println("sadd:" + redisCache.sadd("bz", "1") + "|1");
        System.out.println("sadd:" + redisCache.sadd("bz", "1", "2", "3") + "|2");
        System.out.println("scard:" + redisCache.scard("bz") + "|3");
        System.out.println("sismember:" + redisCache.sismember("bz", "1") + "|true");
        System.out.println("sismember:" + redisCache.sismember("bz", "122") + "|false");
        System.out.println("sadd:" + redisCache.sadd("bz1", "1", "2", "333", "444") + "|4");
        Set<String> set;
        try {
            set = redisCache.sdiff("bz", "bz1");
            for (String s : set) {
                System.out.println("sdiff: " + s);
            }
            set = redisCache.sinter("bz", "bz1");
            for (String s : set) {
                System.out.println("sinter: " + s);
            }
            set = redisCache.sunion("bz", "bz1");
            for (String s : set) {
                System.out.println("sunion: " + s);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        set = redisCache.smembers("bz");
        for (String s : set) {
            System.out.println("smembers: " + s);
        }
        System.out.println("srem:" + redisCache.srem("bz", "1"));
        System.out.println("scard:" + redisCache.scard("bz") + "|2");
        System.out.println("srem:" + redisCache.srem("bz1", "1"));
        System.out.println("scard:" + redisCache.scard("bz1") + "|3");
    }

    @Test
    public void testAdvSS() {
        ICacheAdvance redisCache = BCacheHelper.getRedisCache();
        redisCache.del("bz");
        System.out.println("zadd:" + redisCache.zadd("bz", 1, "1") + "|1");
        Map<String, Double> scoreMembers = new HashMap<String, Double>(2);
        scoreMembers.put("2", 1D);
        scoreMembers.put("3", 2D);
        System.out.println("zadd:" + redisCache.zadd("bz", scoreMembers) + "|2");
        System.out.println("zcard:" + redisCache.zcard("bz") + "|3");
        System.out.println("zcount:" + redisCache.zcount("bz", "1", "2") + "|3");
        System.out.println("zincrby:" + redisCache.zincrby("bz", 2, "1") + "|3");
        Set<String> set = redisCache.zrange("bz", 0, 10);
        for (String s : set) {
            System.out.println("zrange: " + s);
        }
        set = redisCache.zrevrange("bz", 0, 10);
        for (String s : set) {
            System.out.println("zrevrange: " + s);
        }
        System.out.println("zrank:" + redisCache.zrank("bz", "2") + "|0");
        System.out.println("zrevrank:" + redisCache.zrevrank("bz", "1") + "|0");
        System.out.println("zscore:" + redisCache.zscore("bz", "1") + "|3");
        System.out.println("zremrangeByScore:" + redisCache.zremrangeByScore("bz", 1, 1) + "|1");
        System.out.println("zrem:" + redisCache.zrem("bz", "1") + "|1");
        System.out.println("zrem:" + redisCache.zrem("bz", "2", "3") + "|1");
        System.out.println("zcard:" + redisCache.zcard("bz") + "|0");
    }

    @Test
    public void testAdvG() {
        ICacheAdvance redisCache = BCacheHelper.getRedisCache();
        redisCache.del("bz");
        System.out.println("geoadd:" + redisCache.geoadd("bz", 10.11, 11.23, "1") + "|1");
        System.out.println("geoadd:" + redisCache.geoadd("bz", 12.11, 14.23, "2") + "|1");
        System.out.println("geoadd:" + redisCache.geoadd("bz", 103.11, 10.23, "3") + "|1");
        System.out.println("zcard:" + redisCache.zcard("bz") + "|3");
        System.out.println("geodist:" + redisCache.geodist("bz", "1", "2"));
        List<ICacheAdvance.BGeoCoordinate> list = redisCache.geopos("bz", "2", "3", "4");
        for (ICacheAdvance.BGeoCoordinate gc : list) {
            if (gc == null) {
                System.out.println("l:null,h:null");
                continue;
            }
            System.out.println("l:" + gc.getLatitude() + ",h:" + gc.getLongitude());
        }
        System.out.println("georem:" + redisCache.georem("bz", "1") + "|1");
        System.out.println("georem:" + redisCache.georem("bz", "1", "2") + "|1");
        System.out.println("zcard:" + redisCache.zcard("bz") + "|1");
    }

    @After
    public void close() throws Exception {
        System.out.println("factory count:" + BCacheFactory.getCacheMap().size());
        BCacheFactory.shutdown();
        System.out.println("close");
        end = System.currentTimeMillis();
        System.out.println("time:" + (end - start));
    }

}
