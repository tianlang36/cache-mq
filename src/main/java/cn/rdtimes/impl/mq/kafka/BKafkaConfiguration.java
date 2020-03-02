package cn.rdtimes.impl.mq.kafka;

import cn.rdtimes.mq.intf.IMQFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @description: kafka抽象配置信息
 * @author: BZ
 */

public abstract class BKafkaConfiguration {
    private List<IMQFilter> filters = new ArrayList<>(1);

    //服务器地址，可以多个地址用逗号分隔(ip:pord,ip:port)
    protected String bootstrapServers;

    //接收器线程池大小，与发送器无关
    private int threadCount = 1;
    //接收下线程池队列最大值，与发送器无关
    private int queueSize = 2000;
    //以不同名称可以创建多个接收器
    private String name;

    //转换成属性对象
    protected final Properties properties;

    public BKafkaConfiguration() {
        properties = new Properties();
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Properties getProperties() {
        properties.clear();
        properties.setProperty("bootstrap.servers", bootstrapServers);
        convertProperties();

        return properties;
    }

    public void addFilter(IMQFilter filter) {
        filters.add(filter);
    }

    public void removeFilter(IMQFilter filter) {
        if (filters != null) {
            filters.remove(filter);
        }
    }

    public List<IMQFilter> getFilters() {
        return filters;
    }

    //将配置转换成属性对象
    protected abstract void convertProperties();

}
