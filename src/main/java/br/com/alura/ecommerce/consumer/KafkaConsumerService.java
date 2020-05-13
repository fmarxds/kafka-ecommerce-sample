package br.com.alura.ecommerce.consumer;

import br.com.alura.ecommerce.serializer.JsonDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class KafkaConsumerService<T> implements Closeable {

    private final KafkaConsumer<String, T> consumer;
    private final ConsumerParseFunction<T> consumerParseFunction;

    public KafkaConsumerService(String groupId, String topic, ConsumerParseFunction<T> consumerParseFunction, Class<T> type, Properties customProperties) {
        this(groupId, type, consumerParseFunction, customProperties);
        consumer.subscribe(Collections.singleton(topic));
    }

    public KafkaConsumerService(String groupId, Pattern topicPattern, ConsumerParseFunction<T> consumerParseFunction, Class<T> type, Properties customProperties) {
        this(groupId, type, consumerParseFunction, customProperties);
        consumer.subscribe(topicPattern);
    }

    private KafkaConsumerService(String groupId, Class<T> type, ConsumerParseFunction<T> consumerParseFunction, Properties customProperties) {
        this.consumer = new KafkaConsumer<>(properties(groupId, type, customProperties));
        this.consumerParseFunction = consumerParseFunction;
    }

    public void run() throws InterruptedException {
        while (true) {
            ConsumerRecords<String, T> records = consumer.poll(Duration.ofMillis(100));
            records.forEach(consumerParseFunction::parse);
            TimeUnit.SECONDS.sleep(1);
        }
    }

    private Properties properties(String groupId, Class<T> type, Properties customProperties) {
        Properties properties = new Properties();

        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "0.0.0.0:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        properties.setProperty(JsonDeserializer.TYPE_CONFIG, type.getName());

        if (customProperties != null) {
            properties.putAll(customProperties);
        }

        return properties;
    }

    @Override
    public void close() {
        this.consumer.close();
    }
}
