package br.com.alura.ecommerce.consumer;

import br.com.alura.ecommerce.payload.Order;

public class FraudDetectorService {

    public static final String TOPIC_ECOMMERCE_NEW_ORDER = "ECOMMERCE_NEW_ORDER";

    public static void main(String[] args) throws InterruptedException {
        try (KafkaConsumerService<Order> service = new KafkaConsumerService<>(
                FraudDetectorService.class.getSimpleName(),
                TOPIC_ECOMMERCE_NEW_ORDER,
                FraudDetectorService.consumerParseFunction(),
                Order.class,
                null)) {

            service.run();

        }
    }

    private static ConsumerParseFunction<Order> consumerParseFunction() {
        return record -> {
            System.out.println("-------------------------------------------------------------------");
            System.out.println("processing new order, checking for frauds...");
            System.out.println(record.toString());
            System.out.println("fraud successfully checked!");
        };
    }

}
