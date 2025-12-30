package com.trading.model;

public class Trade {

    private final long buyOrderId;
    private final long sellOrderId;
    private final double price;
    private final int quantity;
    private final long timestamp;

    public Trade(long buyOrderId,
                 long sellOrderId,
                 double price,
                 int quantity,
                 long timestamp) {
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.price = price;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }

    public long getBuyOrderId() {
        return buyOrderId;
    }

    public long getSellOrderId() {
        return sellOrderId;
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
}
