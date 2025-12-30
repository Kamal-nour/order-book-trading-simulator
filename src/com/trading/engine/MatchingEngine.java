package com.trading.engine;

import com.trading.model.*;
import java.util.ArrayList;
import java.util.List;

public class MatchingEngine {

    private final OrderBook orderBook = new OrderBook();

    public List<Trade> process(Order order) {
        OrderValidator.validate(order);
        List<Trade> trades = new ArrayList<>();

        if (order.getType() == OrderType.MARKET) {
            match(order, trades, false);
        } else {
            match(order, trades, true);
            if (!order.isFilled()) {
                orderBook.add(order);
            }
        }
        return trades;
    }

    private void match(Order incoming, List<Trade> trades, boolean priceCheck) {
        while (!incoming.isFilled()) {
            Order bestOpposite
                    = incoming.getSide() == Side.BUY
                    ? orderBook.peekBestAsk()
                    : orderBook.peekBestBid();

            if (bestOpposite == null) {
                break;
            }

            if (priceCheck) {
                boolean crossed
                        = incoming.getSide() == Side.BUY
                        ? incoming.getPrice() >= bestOpposite.getPrice()
                        : incoming.getPrice() <= bestOpposite.getPrice();
                if (!crossed) {
                    break;
                }
            }

            int qty = Math.min(incoming.getQuantity(), bestOpposite.getQuantity());
            incoming.reduceQuantity(qty);
            bestOpposite.reduceQuantity(qty);

            trades.add(new Trade(
                    incoming.getSide() == Side.BUY ? incoming.getOrderId() : bestOpposite.getOrderId(),
                    incoming.getSide() == Side.SELL ? incoming.getOrderId() : bestOpposite.getOrderId(),
                    bestOpposite.getPrice(),
                    qty,
                    System.nanoTime()
            ));

            if (bestOpposite.isFilled()) {
                if (incoming.getSide() == Side.BUY) {
                    orderBook.removeBestAsk(); 
                }else {
                    orderBook.removeBestBid();
                }
            }
        }
    }
}
