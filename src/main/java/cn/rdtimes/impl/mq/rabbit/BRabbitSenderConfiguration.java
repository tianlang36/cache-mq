package cn.rdtimes.impl.mq.rabbit;


/**
 * @description: rabbit发送器配置信息
 * @author: BZ
 */

public class BRabbitSenderConfiguration extends BRabbitConfiguration {
    private String exchangeName = "amq-direct";

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

}
