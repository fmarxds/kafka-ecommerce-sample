package br.com.alura.ecommerce.payload;

import java.math.BigDecimal;

public class Order {

    private final String userId;
    private final String userName;
    private final BigDecimal amount;

    private Order() {
        this.userId = null;
        this.userName = null;
        this.amount = null;
    }

    public Order(String userId, String userName, BigDecimal amount) {
        this.userId = userId;
        this.userName = userName;
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", amount=" + amount +
                '}';
    }
}
