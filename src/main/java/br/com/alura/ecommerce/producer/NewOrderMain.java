package br.com.alura.ecommerce.producer;

import br.com.alura.ecommerce.payload.Order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static final String TOPIC_ECOMMERCE_NEW_ORDER = "ECOMMERCE_NEW_ORDER";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (KafkaProducerService<Order> service = new KafkaProducerService<>()) {
            for (int i = 0; i < 10; i++) {
                var key = UUID.randomUUID().toString();
                var value = new Order("1", "Felipe", BigDecimal.valueOf(Math.random() * 5000 + 1).setScale(2, RoundingMode.HALF_UP));
                service.send(TOPIC_ECOMMERCE_NEW_ORDER, key, value);
            }
        }
    }

}
