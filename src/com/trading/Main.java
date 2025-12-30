package com.trading;

import com.trading.engine.MatchingEngine;
import com.trading.model.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        MatchingEngine engine = new MatchingEngine();

        System.out.println("=== ORDER BOOK TRADING SIMULATOR ===\n");

        // ------------------------------------------------------------
        // TEST CASE 1: Add LIMIT orders that do NOT immediately match
        // ------------------------------------------------------------
        System.out.println("TEST 1: Adding resting limit orders");

        engine.process(new Order(
                1, // orderId
                Side.SELL, // side
                OrderType.LIMIT, // type
                10.0, // price
                100, // quantity
                System.nanoTime() // timestamp
        ));

        engine.process(new Order(
                2,
                Side.BUY,
                OrderType.LIMIT,
                9.5,
                50,
                System.nanoTime()
        ));

        System.out.println("-> No trades expected (prices do not cross)\n");

        // ------------------------------------------------------------
        // TEST CASE 2: MARKET order that partially fills
        // ------------------------------------------------------------
        System.out.println("TEST 2: BUY MARKET order partially fills");

        List<Trade> trades1 = engine.process(new Order(
                3,
                Side.BUY,
                OrderType.MARKET,
                0.0, // ignored for MARKET
                80,
                System.nanoTime()
        ));

        printTrades(trades1);

        System.out.println("-> Expected: BUY 80 @ 10.0 (SELL order partially filled)\n");

        // ------------------------------------------------------------
        // TEST CASE 3: Another MARKET order consumes remaining liquidity
        // ------------------------------------------------------------
        System.out.println("TEST 3: BUY MARKET order consumes remaining SELL liquidity");

        List<Trade> trades2 = engine.process(new Order(
                4,
                Side.BUY,
                OrderType.MARKET,
                0.0,
                50,
                System.nanoTime()
        ));

        printTrades(trades2);

        System.out.println("-> Expected: BUY 20 @ 10.0 (SELL order fully filled)");
        System.out.println("-> Remaining 30 shares cannot be filled (book empty)\n");

        // ------------------------------------------------------------
        // TEST CASE 4: LIMIT order crosses the book immediately
        // ------------------------------------------------------------
        System.out.println("TEST 4: LIMIT order that crosses and partially rests");

        engine.process(new Order(
                5,
                Side.SELL,
                OrderType.LIMIT,
                11.0,
                100,
                System.nanoTime()
        ));

        List<Trade> trades3 = engine.process(new Order(
                6,
                Side.BUY,
                OrderType.LIMIT,
                12.0, // crosses the ask
                40,
                System.nanoTime()
        ));

        printTrades(trades3);

        System.out.println("-> Expected: BUY 40 @ 11.0");
        System.out.println("-> Remaining SELL quantity rests in book\n");

        System.out.println("=== END OF SIMULATION ===");
    }

    private static void printTrades(List<Trade> trades) {
        if (trades.isEmpty()) {
            System.out.println("No trades executed.");
            return;
        }

        for (Trade t : trades) {
            System.out.println(
                    "Trade executed: "
                    + t.getQuantity()
                    + " @ "
                    + t.getPrice()
                    + " (BUY order "
                    + t.getBuyOrderId()
                    + ", SELL order "
                    + t.getSellOrderId()
                    + ")"
            );
        }
    }
}
