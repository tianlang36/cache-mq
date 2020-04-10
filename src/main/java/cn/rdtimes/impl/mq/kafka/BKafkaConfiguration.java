package cn.rdtimes.impl.mq.kafka;

import cn.rdtimes.mq.intf.IMQFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @description: kafka抽象配置信息
 * @author: BZ
 * @create: 2020/2/13
 */

public abstract class BKafkaConfiguration {
    private List<IMQFilter> filters = new ArrayList<>(1);
    //服务器地址，可以多个地址用逗号分隔(ip:pord,ip:port)
    private String bootstrapServers;
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
