
package com.trading.model;

public class Order {

    private final long orderId;
    private final Side side;
    private final OrderType type;
    private final double price;     
    private int quantity;           
    private final long timestamp;

    public Order(long orderId,
                 Side side,
                 OrderType type,
                 double price,
                 int quantity,
                 long timestamp) {
        this.orderId = orderId;
        this.side = side;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }

    public long getOrderId() {
        return orderId;
    }

    public Side getSide() {
        return side;
    }

    public OrderType getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void reduceQuantity(int amount) {
        this.quantity -= amount;
    }

    public boolean isFilled() {
        return quantity <= 0;
    }
}
