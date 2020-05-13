package br.com.alura.ecommerce.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerParseFunction<T> {

    void parse(ConsumerRecord<String, T> record);

}
