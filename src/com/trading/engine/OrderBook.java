package com.trading.engine;

import com.trading.model.Order;
import com.trading.model.Side;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.NavigableMap;
import java.util.TreeMap;

public class OrderBook {

    // BUY side: highest price first
    private final NavigableMap<Double, Deque<Order>> bids
            = new TreeMap<>(Comparator.reverseOrder());

    // SELL side: lowest price first
    private final NavigableMap<Double, Deque<Order>> asks
            = new TreeMap<>();

    // Add a LIMIT order to the book
    public void add(Order order) {
        NavigableMap<Double, Deque<Order>> book
                = order.getSide() == Side.BUY ? bids : asks;

        book.computeIfAbsent(order.getPrice(), p -> new ArrayDeque<>())
                .addLast(order);
    }

    // Best bid price
    public Double getBestBidPrice() {
        return bids.isEmpty() ? null : bids.firstKey();
    }

    // Best ask price
    public Double getBestAskPrice() {
        return asks.isEmpty() ? null : asks.firstKey();
    }

    // Oldest order at best bid
    public Order peekBestBid() {
        if (bids.isEmpty()) {
            return null;
        }
        return bids.firstEntry().getValue().peekFirst();
    }

    // Oldest order at best ask
    public Order peekBestAsk() {
        if (asks.isEmpty()) {
            return null;
        }
        return asks.firstEntry().getValue().peekFirst();
    }

    // Remove filled best bid order
    public void removeBestBid() {
        removeTopOrder(bids);
    }

    // Remove filled best ask order
    public void removeBestAsk() {
        removeTopOrder(asks);
    }

    // Internal helper to remove the top order from a side
    private void removeTopOrder(NavigableMap<Double, Deque<Order>> book) {
        var entry = book.firstEntry();
        if (entry == null) {
            return;
        }

        Deque<Order> queue = entry.getValue();
        queue.removeFirst();

        if (queue.isEmpty()) {
            book.remove(entry.getKey());
        }
    }
}
