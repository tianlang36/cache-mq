package cn.rdtimes.mq.intf;

/**
 * mq发送和接收消息基类接口
 *
 * @author BZ
 */
public interface IMQBase {

    /**
     * 获取名称，要唯一
     * @return
     */
    String getName();

    /**
     * 添加过滤器
     * @param filter
     */
    void addFilter(IMQFilter filter);

    /**
     * 删除过滤器
     * @param filter
     */
    void removeFilter(IMQFilter filter);

    /**
     * 关闭
     */
    void close();

}
