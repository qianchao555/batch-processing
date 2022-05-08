package juc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/4/18 20:50
 * @version:1.0
 */
public class Test {

    @Autowired
    Environment env;

    public static void main(String[] args) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse("2022-05-03 14:09:34", df);
//        LocalDateTime localDateTime=LocalDateTime.now();

        System.out.println(localDateTime);

        System.out.println(localDateTime.plusNanos(754927277000L));
    }

    public Map<String, Object> xxxConsumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("spring.kafka.bootstrap-servers"));
        props.put(ConsumerConfig.ENABLEAUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, env.getProperty("spring.kafka.max.poll.interval.ms", "300000"));
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, env.getProperty("spring.kafka.request.timeout.ms", "150000"));
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, env.getProperty(
                "spring.kafka.auto.commit.interval.ms",
                "10000"));
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, env.getProperty("spring.kafka.session.timeout.ms", "150000"));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, env.getProperty("spring.kafka.max.poll.records", "1000"));//每一批数量
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, env.getProperty("spring.kafka.auto.offset.reset", "earliest"));
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, env.getProperty("spring.fetch.min.bytes", "10000000"));//10M
        KafkaSaalUtils.saalConfig(props, env);
        return props;
    }

/**
 *batchFactory.
 *@return KafkaListenerContainerFactory
 *
 **/
    @Bean
    public KafkaListenerContainerFactory<?>highPerformanceBatchFactory() {
        ConcurrentKafkalistenerContainerFactory<String, String > factory = new ConcurrentKafkalistenerContainerFactory<>();
        factory.setConsumerFactory(highPerformanceConsumerFactory());
        factory.setConcurrency(Integer.parseInt(env.getProperty("spring.kafka.template.concurrency","1")));
        factory.setBatchListener(true);//设置为批量消费，每个批次数量在Kafka配置参数中设置

        //设置提交偏移量的方式
        ConsumerConfig.MAX_POLL_RECORDS_CONFI6factory.getContainerProperties().setAckMode(AckMode.MANUAL);//MANUAL_IMMEDIATE);

        return factory;

    }


