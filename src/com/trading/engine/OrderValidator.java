package com.trading.engine;

import com.trading.model.Order;
import com.trading.model.OrderType;

public class OrderValidator {

    public static void validate(Order order) {
        if (order.getQuantity() <= 0)
            throw new IllegalArgumentException("Quantity must be positive");

        if (order.getType() == OrderType.LIMIT && order.getPrice() <= 0)
            throw new IllegalArgumentException("Limit price must be positive");
    }
}
