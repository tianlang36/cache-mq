package cn.rdtimes.impl.mq.rabbit;

import cn.rdtimes.mq.intf.IMQFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: rabbit配置信息
 * @author: BZ
 * @create: 2020/2/13
 */

public class BRabbitConfiguration {
    private List<IMQFilter> filters = new ArrayList<>(1);
    //用于mq设置,多个使用逗号分隔ip:port,ip:port
    private String ipAndPort;
    private String userName;
    private String password;
    private String vhost = "/";
    //是否需要持久化存储消息
    private boolean enablePersistent = true;

    public String getIpAndPort() {
        return ipAndPort;
    }

    public void setIpAndPort(String ipAndPort) {
        this.ipAndPort = ipAndPort;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVhost() {
        return vhost;
    }

    public void setVhost(String vhost) {
        if (vhost == null || vhost.equals("")) vhost = "/";
        this.vhost = vhost;
    }

    public boolean isEnablePersistent() {
        return enablePersistent;
    }

    public void setEnablePersistent(boolean enablePersistent) {
        this.enablePersistent = enablePersistent;
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

}
