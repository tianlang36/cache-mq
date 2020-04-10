package cn.rdtimes.impl.mq.active;

import cn.rdtimes.mq.intf.IMQFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: active配置信息
 * @author: BZ
 * @create: 2020/2/13
 */

public class BActiveConfiguration {
    private List<IMQFilter> filters = new ArrayList<>(1);
    //tcp://ip:port
    private String url;
    private String userName;
    private String password;
    //是否需要持久化存储消息
    private boolean enablePersistent = true;
    private String clientId = "BZ-Active000000";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public boolean isEnablePersistent() {
        return enablePersistent;
    }

    public void setEnablePersistent(boolean enablePersistent) {
        this.enablePersistent = enablePersistent;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
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
