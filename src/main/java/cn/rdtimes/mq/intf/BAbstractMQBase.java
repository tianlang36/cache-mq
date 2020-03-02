package cn.rdtimes.mq.intf;

import java.util.ArrayList;
import java.util.List;

/**
 * 发送和接收消息接口实现基类
 *
 * @author BZ
 */
public abstract class BAbstractMQBase implements IMQBase {
    protected final List<IMQFilter> filterList = new ArrayList<>(1);

    public String getName() {
        return "BAbstractMQInterface";
    }

    public void addFilter(IMQFilter filter) {
        if (filter == null) {
            throw new NullPointerException("filter is null");
        }

        synchronized (this) {
            if (!filterList.contains(filter)) {
                filterList.add(filter);
            }
        }
    }

    public void removeFilter(IMQFilter filter) {
        if (filter == null) {
            throw new NullPointerException("filter is null");
        }

        synchronized (this) {
            filterList.remove(filter);
        }
    }

}
