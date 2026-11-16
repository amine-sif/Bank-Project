# Banking System

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Java%20Swing-blue?style=for-the-badge)
![Architecture](https://img.shields.io/badge/Architecture-Client--Server-green?style=for-the-badge)

A multi-threaded Java client-server banking application with a Swing graphical interface, a custom text protocol over TCP sockets, and file-based persistence.

---

## Features

* **Client-server architecture:** custom TCP socket communication (`Connection`) with dedicated listener threads for reading incoming text, and automatic disconnection of clients after 30 seconds of inactivity.
* **Swing GUI:** desktop interface (`MainFrame`, `MainPanel`) built with the MVC and Observer patterns.
* **Account management:**
  * Create a client account with a number and PIN (`NEW`).
  * Log in with account number and PIN (`CONNECT`).
  * Checking (`CheckingAccount`) and savings (`SavingsAccount`) accounts. Savings accounts charge a 2$ fee per operation when the balance is under 1000$.
* **Banking operations (server-side protocol):**
  * Deposits (`DEPOSIT`) and withdrawals (`WITHDRAW`)
  * Account-to-account transfers (`TRANSFER`)
  * Bill payments (`BILL`)
  * Transaction history (`HISTORY`), stored in a custom linked stack (`LinkedStack`)
* **Persistence:** the bank is loaded from `bank.data` when the server starts and saved when the server is stopped with `EXIT`.

> **Status:** the server implements all operations above. The GUI currently supports login, account creation and savings-account creation; the remaining operation buttons are not yet wired to the server.

---

## Protocol

Plain text commands sent by the client; the server replies with the same keyword followed by `OK` or `NO`.

| Command | Example |
|---|---|
| `NEW <number>:<pin>` | `NEW ABC123:1234` |
| `CONNECT <number>:<pin>` | `CONNECT ABC123:1234` |
| `SAVINGS` | create a savings account |
| `SELECT checking\|savings` | choose the active account |
| `DEPOSIT <amount>` / `WITHDRAW <amount>` | `DEPOSIT 100` |
| `TRANSFER <amount> <account>` | `TRANSFER 50 XYZ12A` |
| `BILL <amount> <billNumber> <description>` | `BILL 20 INV42 electricity` |
| `HISTORY`, `LIST`, `EXIT` | |

Rules: client account number = 6-8 uppercase letters/digits; PIN = 4-5 digits.

---

## Project Structure

```text
Bank-Project/
├── BankClient2/                 # Client application & Swing GUI
│   └── src/main/java/com/
│       ├── client/              # Socket client & event handlers
│       ├── commonclient/        # Networking, events & listener threads
│       ├── controller/          # Swing action & mouse listeners
│       ├── observer/            # Observer/Observable pattern
│       ├── programs/            # Entry points (MainFrame, ClientProgramLab2)
│       └── view/                # Swing panels & UI components
│
└── BankServer2/                 # Server application & banking logic
    └── src/main/java/com/
        ├── bank/                # Domain model (Bank, ClientAccount, accounts, operations)
        │   ├── adt/             # LinkedStack
        │   ├── io/              # BankIO (save/load)
        │   └── server/          # BankServer, BankConnection, inactivity thread
        ├── common/              # Networking, events & listener threads
        ├── programs/            # Entry point (ServerProgramLab2)
        └── server/              # Generic Server, event handler & command processor
```

---

## Running

Requires JDK 17+ (tested on 21).

1. Start the server: run `com.programs.ServerProgramLab2` (listens on port 8888).
2. Start the client: run `com.programs.MainFrame`, then **Start → Connect**.
3. Stop the server by typing `EXIT` in its console so the bank is saved to `bank.data`
   (in the server's working directory).