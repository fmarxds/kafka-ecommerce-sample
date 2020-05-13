package br.com.alura.ecommerce.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;
import java.util.regex.Pattern;

public class LogService {

    public static final Pattern PATTERN_ECOMMERCE = Pattern.compile("ECOMMERCE.*");

    public static void main(String[] args) throws InterruptedException {
        try (KafkaConsumerService<String> service = new KafkaConsumerService<>(
                LogService.class.getSimpleName(),
                PATTERN_ECOMMERCE,
                LogService.consumerParseFunction(),
                String.class,
                customProperties())) {

            service.run();

        }
    }

    private static ConsumerParseFunction<String> consumerParseFunction() {
        return record -> System.out.println("LOG: " + record.toString());
    }

    private static Properties customProperties() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return properties;
    }

}
