package com.knowledge.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @Description:TODO
 * @ClassName KafkaConsumer
 * @Author qianchao
 * @Date 2023/4/14
 * @Version V1.0
 **/
@Component
public class KafkaConsumer {
    /**
     * @param record record
     * @KafkaListener(groupId = "testGroup", topicPartitions = {
     * @TopicPartition(topic = "topic1", partitions = {"0", "1"}),
     * @TopicPartition(topic = "topic2", partitions = "0",
     * partitionOffsets = @PartitionOffset(partition = "1", initialOffset = "100"))
     * },concurrency = "6")
     * //concurrency就是同组下的消费者个数，就是并发消费数，必须小于等于分区总数
     */
    @KafkaListener(topics = "my-replicated-topic", groupId = "qcGroup")
    public void listenQcGroup(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String value = record.value();
        System.out.println("qcGroup message: " + value);
        System.out.println("qcGroup record: " + record);
        //手动提交offset，一般是提交一个banch，幂等性防止重复消息
        // === 每条消费完确认性能不好！
        ack.acknowledge();
    }

    //配置多个消费组
//    @KafkaListener(topics = "my-replicated-topic", groupId = "qcGroup2")
//    public void listenQcGroup2(ConsumerRecord<String, String> record, Acknowledgment ack) {
//        String value = record.value();
//        System.out.println("qcGroup2 message: " + value);
//        System.out.println("qcGroup2 record: " + record);
//        //手动提交offset
//        ack.acknowledge();
//    }
}
