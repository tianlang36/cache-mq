package cn.rdtimes.imp.mq.active;

import cn.rdtimes.impl.mq.active.BActiveMessage;
import cn.rdtimes.mq.BMQException;

import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * @description:
 * @author: BZ
 */

public class BMessage extends BActiveMessage {
    private String content;
    private Message message;

    @Override
    public Message getMessage(Session session) throws BMQException {
        try {
            return session.createTextMessage(content);
        } catch (Exception e) {
            throw new BMQException(e);
        }
    }

    @Override
    public void setMessage(Message msg) {
        message = msg;
    }

    public Message getMessage() {
        return message;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String toString() {
        try {
            String str = "content:" + content + ",message:" ;
            if (message == null) str += "";
            else str += ((TextMessage) message).getText();
            return str;
        } catch (Exception e) {
            return "";
        }
    }

}
