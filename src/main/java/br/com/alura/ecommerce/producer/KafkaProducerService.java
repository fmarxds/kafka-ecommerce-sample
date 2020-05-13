package br.com.alura.ecommerce.producer;

import br.com.alura.ecommerce.serializer.JsonSerializer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class KafkaProducerService<T> implements Closeable {

    private final KafkaProducer<String, T> producer;

    public KafkaProducerService() {
        this.producer = new KafkaProducer<>(this.properties());
    }

    public void send(String topic, String key, T value) throws ExecutionException, InterruptedException {
        Future<RecordMetadata> recordMetadataFuture = this.producer.send(new ProducerRecord<>(topic, key, value), this.producerCallback());
        recordMetadataFuture.get();
    }

    private Callback producerCallback() {
        return (metadata, exception) -> {
            if (null != exception) {
                System.out.println("error while sending to " + metadata.topic());
                exception.printStackTrace();
                return;
            }
            System.out.println("success sending to " + metadata.topic() + ":::partition " + metadata.partition() + "/ offset " + metadata.offset() + "/ timestamp " + metadata.timestamp());
        };
    }

    private Properties properties() {
        Properties properties = new Properties();

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "0.0.0.0:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        properties.setProperty(ProducerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());

        return properties;
    }

    @Override
    public void close() {
        this.producer.close();
    }
}
