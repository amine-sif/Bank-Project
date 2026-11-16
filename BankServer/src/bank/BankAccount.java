package bank;


import java.io.Serializable;

public abstract class BankAccount implements Serializable {
    private String number;
    private AccountType type;
    protected double balance;
    protected LinkedStack<Operation> history;

    public String toString() {
        String str = number+"("+type + ") Balance "+balance+"$";
        return str;
    }
    /**
     * Randomly generates a bank account number with the format CCC00C, where C is an upper case letter
     * and 0 is a digit between 0 and 9.
     * @return the generated number
     */
    public static String generateNewNumber() {
        char[] t = new char[6];
        for (int i=0;i<3;i++) {
            t[i] = (char)((int)(Math.random()*26)+'A');
        }
        for (int i=3;i<5;i++) {
            t[i] = (char)((int)(Math.random()*10)+'0');
        }
        t[5] = (char)((int)(Math.random()*26)+'A');
        return new String(t);
    }

    /**
     * Creates a bank account.
     * @param number account number
     * @param type account type
     */
    public BankAccount(String number, AccountType type) {
        this.number = number;
        this.type = type;
        this.balance = 0;
        this.history = new LinkedStack<>();
    }
    public String getNumber() {
        return number;
    }
    public AccountType getType() {
        return type;
    }
    public double getBalance() {
        return balance;
    }
    public abstract boolean credit(double amount);
    public abstract boolean debit(double amount);
    public abstract boolean payBill(String billNumber, double amount, String description);
    public abstract boolean transfer(double amount, String recipientAccountNumber);

    public String getHistory() {
        String s = "";
        LinkedStack<Operation> tempStack = new LinkedStack<>();
        Operation operation;

        while (!this.history.isEmpty()) {
            operation = this.history.pop();
            s += operation + "\n";
            tempStack.push(operation);
        }
        while (!tempStack.isEmpty()) {
            operation = tempStack.pop();
            this.history.push(operation);
        }
        return s;
    }
}
