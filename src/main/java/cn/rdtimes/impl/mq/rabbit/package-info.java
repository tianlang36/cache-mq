/**
 * rabbitmq实现，这里使用direct方式发送和接收消息，以字符串方式传递消息
 * 在web管理器中要先创建用户及密码，再创建queue，再创建exchange（direct类型）
 */
package cn.rdtimes.impl.mq.rabbit;