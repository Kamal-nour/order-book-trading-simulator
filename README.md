Order Book Trading Simulator

Overview
--------
This project implements a deterministic limit-order-book trading simulator in Java.
It models the core matching logic of a financial exchange, including market and limit
orders, price–time priority, partial fills, and in-memory order book management.

The goal of this project is to demonstrate clean system design, correct market
behavior, and deterministic matching logic, rather than UI or networking concerns.


Key Features
------------
- Support for LIMIT and MARKET orders
- Price–time priority matching (price first, FIFO at same price)
- Partial fills and multi-trade execution per order
- In-memory bid / ask order book
- Deterministic behavior using logical timestamps
- Clean separation between model and engine layers


Project Structure
-----------------
src/
└── com/
    └── trading/
        ├── Main.java              (Entry point with illustrative test cases)
        │
        ├── model/                 (Domain model – immutable data)
        │   ├── Order.java
        │   ├── Trade.java
        │   ├── Side.java
        │   └── OrderType.java
        │
        └── engine/                (Core trading logic)
            ├── OrderBook.java
            ├── MatchingEngine.java
            └── OrderValidator.java


Design Principles
-----------------
- model/ contains pure domain objects with no business logic
- engine/ contains all market behavior and state mutation
- Main.java is used only for demonstration and testing


Order Types
-----------
Limit Orders:
- Specify a price and quantity
- Execute only at the given price or better
- Any unfilled quantity rests in the order book

Market Orders:
- Execute immediately against the best available prices
- Do not rest in the order book
- May be partially filled if liquidity is insufficient


Matching Logic
--------------
1. Incoming orders are validated
2. Orders are matched against the best available opposite-side orders
3. Matching follows:
   - Price priority
   - Time priority (FIFO at the same price)
4. Trades are generated for each fill
5. Fully filled orders are removed from the book
6. Remaining limit order quantity is added to the book

This behavior mirrors real exchange matching engines.


Determinism
-----------
To ensure reproducible and deterministic behavior, the simulator uses a logical
timestamp instead of system time.

This avoids nondeterminism caused by system clocks and ensures that:
- FIFO ordering is consistent
- Test outputs are identical across runs


How to Compile and Run
---------------------
From the project root:

Compile:
javac src/com/trading/model/*.java src/com/trading/engine/*.java src/com/trading/Main.java

Run:
java -cp src com.trading.Main


Example Output
--------------
Trade executed: 80 @ 10.0 (BUY order 3, SELL order 1)
Trade executed: 20 @ 10.0 (BUY order 4, SELL order 1)
Trade executed: 40 @ 11.0 (BUY order 6, SELL order 5)


Why This Project
----------------
This simulator focuses on the core of trading systems:
- Deterministic state transitions
- Correct market semantics
- Clean architecture and separation of concerns

It intentionally excludes UI, networking, and databases to keep the focus on
matching engine correctness, which is the heart of financial trading platforms.


Possible Extensions
-------------------
- Order cancellation
- Multi-instrument support
- Order book snapshots
- Deterministic replay from file
- Performance benchmarking


Author
------
Built as a systems-oriented trading simulation project for internship and interview
preparation, with emphasis on correctness, clarity, and determinism.
