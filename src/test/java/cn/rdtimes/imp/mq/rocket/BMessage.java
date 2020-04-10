package cn.rdtimes.imp.mq.rocket;

import cn.rdtimes.impl.mq.rocket.BRocketMessage;
import cn.rdtimes.mq.BMQException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;


/**
 * @description:
 * @author: BZ
 * @create: 2020/2/13
 */

public class BMessage extends BRocketMessage {
    private String content;
    private Message message;

    @Override
    public Message getMessage(String topic, String tag) throws BMQException {
        try {
            Message msg = new Message(topic /* Topic */,
                    tag /* Tag */,
                    content.getBytes() /* Message body */);
            return msg;
        } catch (Exception e) {
            throw new BMQException(e);
        }
    }

    private List<MessageExt> msgList;

    @Override
    public void setMessage(List<MessageExt> msgList) {
        this.msgList = msgList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String toString() {
        if (msgList == null) return "";
        StringBuilder sb = new StringBuilder(128);
        sb.append("count:" + msgList.size());
        sb.append("\r\n");
        for (MessageExt msg : msgList) {
            sb.append("id:" + msg.getMsgId() + ",body:" + new String(msg.getBody()));
            sb.append("\r\n");
        }
        return sb.toString();
    }

}
