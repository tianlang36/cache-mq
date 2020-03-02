package cn.rdtimes.imp.cache.memcached;

import cn.rdtimes.cache.BCacheFactory;
import cn.rdtimes.cache.intf.ICacheBase;
import cn.rdtimes.impl.cache.BCacheHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @description: mem缓存测试
 * @author: BZ
 */

public class BMemcachedCacheTest {

    @Before
    public void setup() {
        BCacheHelper.createMemcachedCache(new InetSocketAddress("localhost", 11122));
        System.out.println("create");
    }

    @Test
    public void test() throws Exception {
        ICacheBase memcachedCache = BCacheHelper.getMemcachedCache();

        System.out.println("name:" + memcachedCache.getName());
        System.out.println("hasAdvance:" + memcachedCache.hasAdvance());
        memcachedCache.set("1", "1", 1);
        System.out.println("get:" + memcachedCache.get("1"));
        memcachedCache.set("2", "2");
        System.out.println("get:" + memcachedCache.get("2"));
        System.out.println("del:" + memcachedCache.del("2"));
        System.out.println("get:" + memcachedCache.get("2"));
        try {
            memcachedCache.ttl("1");
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("ttl is normal");
        }
        System.out.println("incr:" + memcachedCache.incr("3", 1));
        System.out.println("incr:" + memcachedCache.incr("3", 1));
        System.out.println("incr:" + memcachedCache.incr("3", 1));
        System.out.println("decr:" + memcachedCache.decr("4", 0));
        System.out.println("incr:" + memcachedCache.incr("4", 5));
        System.out.println("decr:" + memcachedCache.decr("4", 2));
        System.out.println("decr:" + memcachedCache.decr("3", 3));
        System.out.println("decr:" + memcachedCache.decr("3", 3));
        System.out.println("get:" + memcachedCache.get("4"));
        System.out.println("del:" + memcachedCache.del("3", "4"));
        System.out.println("exist:" + memcachedCache.exists("1"));
        System.out.println("exist:" + memcachedCache.exists("3", "4"));
        memcachedCache.set("2", "2");
        memcachedCache.expire("2", 2);
        System.out.println("get:" + memcachedCache.get("2"));

        List<Object> list = memcachedCache.get("1", "2", "3", "4");
        for (Object obj : list) {
            System.out.println("list:" + obj);
        }

        Thread.sleep(3000);

        System.out.println("get:" + memcachedCache.get("1"));
        System.out.println("get:" + memcachedCache.get("2"));
        System.out.println("get:" + memcachedCache.get("5"));
        memcachedCache.set("5", "5");
        System.out.println("get:" + memcachedCache.get("5"));
        memcachedCache.set("5", "6");
        System.out.println("get:" + memcachedCache.get("5"));
    }

    @After
    public void close() throws Exception {
        BCacheFactory.shutdown();
        System.out.println("close");
    }

//    public static void main(String[] args) {
//        Result result = JUnitCore.runClasses(BMemcachedCacheTest.class);
//        for (Failure failure : result.getFailures()) {
//            System.out.println(failure.toString());
//        }
//        System.out.println(result.wasSuccessful());
//    }

}
