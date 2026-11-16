package com.bank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClientAccount implements Serializable {
    private String number;
    private String pin;
    private List<BankAccount> accounts;

    /**
     * Creates a client account with a number and a PIN.
     *
     * @param number the client account number
     * @param pin the PIN
     */
    public ClientAccount(String number, String pin) {
        this.number = number;
        this.pin = pin;
        accounts = new ArrayList<>();
    }
    public String toString() {
        String str = number+":"+pin + "[";
        for (BankAccount c:accounts)
            str += c + " | ";
        return str+"]";
    }
    /**
     * Adds a bank account to the client account.
     *
     * @param account the bank account
     * @return true if the addition succeeds
     */
    public boolean add(BankAccount account) {
        return this.accounts.add(account);
    }
    /**
     * Returns the client account number.
     * @return client account number
     */
    public String getNumber() {
        return number;
    }
    public List<BankAccount> getAccounts() {
        return accounts;
    }

    public String getPin() {
        return pin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientAccount)) return false;
        ClientAccount that = (ClientAccount) o;
        return Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(number);
    }

    public BankAccount getCheckingAccount() {
        for (BankAccount acc:accounts) {
            if (acc.getType()==AccountType.CHECKING)
                return acc;
        }
        return null;
    }

    public BankAccount getSavingsAccount() {
        for (BankAccount acc:accounts) {
            if (acc.getType()==AccountType.SAVINGS)
                return acc;
        }
        return null;
    }
}
