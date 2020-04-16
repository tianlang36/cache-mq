package cn.rdtimes.impl.cache.redis;

/**
 * @description: redis缓存配置信息
 * @author: BZ
 * @create: 2020/2/13
 */

public class BRedisCacheConfiguration {
    //地址、端口和密码一组，每组使用逗号分隔，组内使用冒号分隔
    // ip:port:pwd,ip:port:pwd
    private String ipAndPortAndPwd;
    //连接池最大连接数
    private int maxActive = 10;
    //连接池中最大空闲的连接数
    private int maxIdle = 5;
    //连接池中最小空闲的连接数
    private int minIdle = 2;
    //空闲连接的检测周期(单位为毫秒)，默认-1 表示不做检测
    private int timeBetweenEvictionRunsMillis = 60000;
    //做空闲连接检测时，每次的采样数
    private int numTestsPerEvictionRun = 3;
    //当连接池用尽后，调用者是否要等待，此参数和maxWaitMillis对应的，只有当此参数为true时，maxWaitMills才会生效
    private boolean blockWhenExhausted = true;
    //当连接池用尽后，调用者的最大等待时间(单位为毫秒)，默认值为-1 表示永不超时
    private int maxWaitMillis = 10000;
    //向连接池借用连接时是否做连接有效性检测(ping),无效连接会被移除，每次借出多执行一次ping命令，默认false
    private boolean testOnBorrow = false;
    //向连接池归还连接时是否做连接有效性检测(ping),无效连接会被移除，每次借出多执行一次ping命令，默认false
    private boolean testOnReturn = false;
    //向连接池借用连接时是否做连接空闲检测，空闲超时的连接会被移除，默认false
    private boolean testWhileIdle = false;
    //连接的最小空闲时间，达到此值后空闲连接将被移除
    private int minEvictableIdleTimeMills = 30000;
    //客户端超时时间单位是毫秒
    private int timeout = 3000;

    public String getIpAndPortAndPwd() {
        return ipAndPortAndPwd;
    }

    public void setIpAndPortAndPwd(String ipAndPortAndPwd) {
        this.ipAndPortAndPwd = ipAndPortAndPwd;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public int getNumTestsPerEvictionRun() {
        return numTestsPerEvictionRun;
    }

    public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }

    public boolean isBlockWhenExhausted() {
        return blockWhenExhausted;
    }

    public void setBlockWhenExhausted(boolean blockWhenExhausted) {
        this.blockWhenExhausted = blockWhenExhausted;
    }

    public int getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(int maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public int getMinEvictableIdleTimeMills() {
        return minEvictableIdleTimeMills;
    }

    public void setMinEvictableIdleTimeMills(int minEvictableIdleTimeMills) {
        this.minEvictableIdleTimeMills = minEvictableIdleTimeMills;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public  String[] splitIpAndPortAndPwd() {
        return ipAndPortAndPwd.split(",");
    }

    public String[] alongIpAndPortAndPwd(String ipAndPortAndPwd) {
        return ipAndPortAndPwd.split(":");
    }

}
