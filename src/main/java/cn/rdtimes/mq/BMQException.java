package cn.rdtimes.mq;

/**
 * @description: 消息类基类异常
 * @author: BZ
 */

public class BMQException extends Exception {

    public BMQException() {
        super();
    }

    public BMQException(String message) {
        super(message);
    }

    public BMQException(String message, Throwable cause) {
        super(message, cause);
    }

    public BMQException(Throwable cause) {
        super(cause);
    }

    protected BMQException(String message, Throwable cause,
                           boolean enableSuppression,
                           boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
