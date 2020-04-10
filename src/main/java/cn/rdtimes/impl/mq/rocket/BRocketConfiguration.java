package cn.rdtimes.impl.mq.rocket;

import cn.rdtimes.mq.intf.IMQFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: rocket配置信息
 * @author: BZ
 * @create: 2020/2/13
 */

public class BRocketConfiguration {
    private List<IMQFilter> filters = new ArrayList<>(1);
    //用于mq设置,多个使用逗号分隔ip:port,ip:port
    private String ipAndPort;
    private String groupId;

    public String getIpAndPort() {
        return ipAndPort;
    }

    public void setIpAndPort(String ipAndPort) {
        this.ipAndPort = ipAndPort;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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
