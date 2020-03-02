package cn.rdtimes.impl.mq.kafka;

/**
 * @description: kafka发送器配置信息
 * @author: BZ
 */

public class BKafkaSenderConfiguration extends BKafkaConfiguration {
    //大部分属性可以使用默认值
    //在确认一个请求发送完成之前需要收到的反馈信息的数量
    //[all,-1,0,1]
    private String acks = "1";
    //用来缓冲等待被发送到服务器的记录的总字节数，单位字节
    private long bufferMemory = 33554432;
    //生成数据时可使用的压缩类型。默认值是none(即不压缩)
    // [none, gzip, snappy, lz4]
    private String compressionType = "none";
    //若设置大于0的值，则客户端会将发送失败的记录重新发送，尽管这些记录有可能是暂时性的错误
    private int retries = 3;
    //当将多个记录被发送到同一个分区时， Producer 将尝试将记录组合到更少的请求中，单位：字节
    private int batchSize = 16384;
    //发出请求时传递给服务器的 ID 字符串。这样做的目的是为了在服务端的请求日志中能够通过
    // 逻辑应用名称来跟踪请求的来源，而不是只能通过IP和端口号跟进。
    private String clientId = "BZ-KafkaProuder000000";
    //该配置控制 KafkaProducer.send()和KafkaProducer.partitionsFor() 允许被阻塞的时长，单位：毫秒
    private long maxBlock = 60000;
    //请求的最大字节数,单位：字节
    private int maxRequestSize = 1048576;
    //客户端等待请求响应的最大时长，单位：毫秒
    private int requestTimeout = 30000;
    //自定义分区类名称
    private String partitionerClass;

    //指定消息到哪个分区，-1位未指定,按默认的分区策略分区
    private int partition = -1;
    //关键字的序列化类
    protected String keySerializer = "org.apache.kafka.common.serialization.StringSerializer";
    //值的序列化类
    protected String valueSerializer = "cn.rdtimes.impl.mq.kafka.BObjectSerializer";

    public int getPartition() {
        return partition;
    }

    public void setPartition(int partition) {
        this.partition = partition;
    }

    public String getAcks() {
        return acks;
    }

    public void setAcks(String acks) {
        this.acks = acks;
    }

    public long getBufferMemory() {
        return bufferMemory;
    }

    public void setBufferMemory(long bufferMemory) {
        this.bufferMemory = bufferMemory;
    }

    public String getCompressionType() {
        return compressionType;
    }

    public void setCompressionType(String compressionType) {
        this.compressionType = compressionType;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public long getMaxBlock() {
        return maxBlock;
    }

    public void setMaxBlock(long maxBlock) {
        this.maxBlock = maxBlock;
    }

    public int getMaxRequestSize() {
        return maxRequestSize;
    }

    public void setMaxRequestSize(int maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public String getPartitionerClass() {
        return partitionerClass;
    }

    public void setPartitionerClass(String partitionerClass) {
        this.partitionerClass = partitionerClass;
    }

    public String getKeySerializer() {
        return keySerializer;
    }

    public void setKeySerializer(String keySerializer) {
        this.keySerializer = keySerializer;
    }

    public String getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(String valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    @Override
    protected void convertProperties() {
        properties.setProperty("key.serializer", keySerializer);
        properties.setProperty("value.serializer", valueSerializer);
        properties.put("acks", acks);
        properties.put("buffer.memory", bufferMemory);
        properties.put("compression.type", compressionType);
        properties.put("retries", retries);
        properties.put("batch.size", batchSize);
        properties.put("client.id", clientId);
        properties.put("max.block.ms", maxBlock);
        properties.put("max.request.size", maxRequestSize);
        properties.put("request.timeout.ms", requestTimeout);
        if (partitionerClass != null && !partitionerClass.equals("")) {
            properties.put("partitioner.class", partitionerClass);
        }
    }

}
