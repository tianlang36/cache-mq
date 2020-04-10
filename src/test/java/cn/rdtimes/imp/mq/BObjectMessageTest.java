package cn.rdtimes.imp.mq;

import cn.rdtimes.mq.intf.IMQMessage;

/**
 * @description:
 * @author: BZ
 * @create: 2020/2/13
 */

public class BObjectMessageTest implements IMQMessage {
    private String id;
    private String name;
    private int age;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String toString() {
        return "id:" + id + ",name:" + name + ",age:" + age;
    }

}
