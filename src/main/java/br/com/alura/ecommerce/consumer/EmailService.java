package br.com.alura.ecommerce.consumer;

import br.com.alura.ecommerce.payload.Order;

public class EmailService {

    public static final String TOPIC_ECOMMERCE_NEW_ORDER = "ECOMMERCE_NEW_ORDER";

    public static void main(String[] args) throws InterruptedException {
        try (KafkaConsumerService<Order> service = new KafkaConsumerService<>(
                EmailService.class.getSimpleName(),
                TOPIC_ECOMMERCE_NEW_ORDER,
                EmailService.consumerParseFunction(),
                Order.class,
                null)) {

            service.run();

        }
    }

    private static ConsumerParseFunction<Order> consumerParseFunction() {
        return record -> {
            System.out.println("-------------------------------------------------------------------");
            System.out.println("processing new order, sending order email...");
            System.out.println(record.toString());
            System.out.println("email successfully sent!");
        };
    }

}
