package bank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Bank implements Serializable {
    private String name;
    private List<ClientAccount> accounts;

    public Bank(String name) {
        this.name = name;
        this.accounts = new ArrayList<>();
    }

    @Override
    public String toString() {
        String str = "-----Bank-----\n";
        for (ClientAccount c:accounts)
            str += "Client account : "+c + "\n";
        return str + "\n----------------";
    }
    public void print() {
        System.out.println(this);
    }
    /**
     * Looks for a client account from its number.
     *
     * @param clientAccountNumber the client account number
     * @return the client account if it was found. Otherwise, returns null
     */
    public ClientAccount getClientAccount(String clientAccountNumber) {
        ClientAccount acc = new ClientAccount(clientAccountNumber,"");
        int index = this.accounts.indexOf(acc);
        if (index != -1)
            return this.accounts.get(index);
        else
            return null;
    }

    /**
     * Checks that a bank account belongs to the client account.
     *
     * @param bankAccountNumber number of the bank account
     * @param clientAccountNumber number of the client account
     * @return true if the bank account belongs to the client account
     */

    public boolean belongsTo(String bankAccountNumber, String clientAccountNumber) {
    throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Deposits money into a bank account
     *
     * @param amount amount to deposit
     * @param accountNumber account number
     * @return true if the deposit was done correctly
     */
    public boolean deposit(double amount, String accountNumber) {
        BankAccount acc = getBankAccountByNumber(accountNumber);
        if (acc!=null)
            return acc.credit(amount);
        return false;
    }

    /**
     * Withdraws money from a bank account
     *
     * @param amount amount withdrawn
     * @param accountNumber account number
     * @return true if the withdrawal was done correctly
     */
    public boolean withdraw(double amount, String accountNumber) {
        BankAccount acc = getBankAccountByNumber(accountNumber);
        if (acc!=null)
            return acc.debit(amount);
        return false;
    }

    /**
     * Transfers money from one account to another in the same bank
     * @param amount amount to transfer
     * @param sourceAccountNumber number of the account the money is taken from
     * @param targetAccountNumber number of the account the money is deposited into
     * @return true if the operation went correctly
     */
    public boolean transfer(double amount, String sourceAccountNumber, String targetAccountNumber) {
        BankAccount acc1, acc2;
        if (amount<=0 || sourceAccountNumber==null || sourceAccountNumber.equals(targetAccountNumber))
            return false;
        acc1 = getBankAccountByNumber(sourceAccountNumber);
        if (acc1==null)
            return false;
        acc2 = getBankAccountByNumber(targetAccountNumber);
        if (acc2==null)
            return false;
        if (acc1.transfer(amount,targetAccountNumber))
            return acc2.transfer(-amount,sourceAccountNumber);
        return false;
    }

    /**
     * Pays a bill.
     * @param amount amount of the bill
     * @param accountNumber number of the bank account the payment is made from
     * @param billNumber number of the bill
     * @param description descriptive text of the bill
     * @return true if the payment was done correctly
     */
    public boolean payBill(double amount, String accountNumber, String billNumber, String description) {
        BankAccount acc = getBankAccountByNumber(accountNumber);
        if (acc!=null)
            return acc.payBill(billNumber,amount,description);
        return false;
    }

    /**
     * Creates a new client account with a number and a PIN and adds it to the list of accounts.
     *
     * @param clientAccountNumber number of the client account to create
     * @param pin PIN of the client account to create
     * @return true if the account was created correctly
     */
    public boolean add(String clientAccountNumber, String pin) {
        /*Rules:
            - Check that the number has between 6 and 8 characters and contains only upper case letters and digits.
              Otherwise, return false.
            - Check that the PIN has between 4 and 5 characters and contains only digits. Otherwise,
              return false.
            - Check whether there is already a client account with the number, return false.
            - Otherwise:
                . Create a client account with the number and the PIN;
                . Generate (with BankAccount.generateNewNumber()) a new bank account number that is not
                  already used;
                . Create a checking account with this number and add it to the client account;
                . Add the client account to the list of accounts and return true.
         */
        //Question 2.2 :
        int size = clientAccountNumber.length();
        char c;
        ClientAccount clientAccount;
        BankAccount bankAccount;
        String number;

        if (size<6 || size>8) {
            return false;
        }
        for (int i=0; i<size; i++) {
            c = clientAccountNumber.charAt(i);
            if (!Util.isUpperCaseLetter(c) && !Util.isDigit(c)) {
                return false;
            }
        }
        size = pin.length();
        if (size<4 || size>5) {
            return false;
        }
        for (int i=0; i<size; i++) {
            c = pin.charAt(i);
            if (!Util.isDigit(c)) {
                return false;
            }
        }
        size = this.accounts.size();
        for (int i=0;i<size;i++) {
            if (clientAccountNumber.equals(accounts.get(i).getNumber())) {
                return false;
            }
        }
        clientAccount = new ClientAccount(clientAccountNumber,pin);
        number = getNewBankAccountNumber(); //makes sure the number is not already used

        bankAccount = new CheckingAccount(number);
        clientAccount.add(bankAccount);

        return this.accounts.add(clientAccount);
    }

    /**
     * Returns the checking account number of a client from the client account number.
     *
     * @param clientAccountNumber client account number
     * @return number of the checking account of the client having this client account number
     */
    public String getDefaultAccountNumber(String clientAccountNumber) {
        //Question 2.3 :
        ClientAccount clientAccount = this.getClientAccount(clientAccountNumber);
        BankAccount bankAccount;
        ListIterator<BankAccount> accountList;
        if (clientAccount!=null) {
            accountList = clientAccount.getAccounts().listIterator();
            while (accountList.hasNext()) {
                bankAccount = accountList.next();
                if (bankAccount.getType()==AccountType.CHECKING) {
                    return bankAccount.getNumber();
                }
            }
        }
        return null;
    }
    //Question 4.2 :
    public boolean hasSavingsAccount(String clientAccountNumber) {
        ListIterator<BankAccount> list = getClientAccount(clientAccountNumber).getAccounts().listIterator();
        while (list.hasNext())
            if (list.next().getType()==AccountType.SAVINGS)
                return true;
        return false;
    }

    public boolean numberAlreadyUsed(String number) {
        int nbClientAccounts = this.accounts.size();
        int nbBankAccounts;
        List<BankAccount> accs;
        for (int i=0;i<nbClientAccounts;i++) {
            accs = accounts.get(i).getAccounts();
            nbBankAccounts = accs.size();
            for (int j=0;j<nbBankAccounts;j++) {
                if (accs.get(j).getNumber().equals(number))
                    return true;
            }
        }
        return false;
    }
    public String getNewBankAccountNumber() {
        String num = BankAccount.generateNewNumber();
        while (numberAlreadyUsed(num))
            num = BankAccount.generateNewNumber();
        return num;
    }

    public boolean addSavingsAccount(String clientAccountNumber) {
        BankAccount account;
        if (hasSavingsAccount(clientAccountNumber))
            return false;
        String number = getNewBankAccountNumber();
        account = new SavingsAccount(number,0.05);
        System.out.println("Savings account added");
        return getClientAccount(clientAccountNumber).add(account);
    }

    public String getBankAccountNumber(String clientAccountNumber, String accountType) {
        String accountNumber = null;
        ClientAccount clientAccount = this.getClientAccount(clientAccountNumber);
        BankAccount bankAccount;
        if ("checking".equals(accountType)) {
            bankAccount = clientAccount.getCheckingAccount();
            if (bankAccount!=null) {
                accountNumber = bankAccount.getNumber();
            }
        }
        else if ("savings".equals(accountType)) {
            bankAccount = clientAccount.getSavingsAccount();
            if (bankAccount!=null) {
                accountNumber = bankAccount.getNumber();
            }
        }
        return accountNumber;
    }
    public BankAccount getBankAccountByNumber(String number) {
        int nbClientAccounts = this.accounts.size();
        int nbBankAccounts;
        BankAccount acc;
        List<BankAccount> accs;
        for (int i=0;i<nbClientAccounts;i++) {
            accs = accounts.get(i).getAccounts();
            nbBankAccounts = accs.size();
            for (int j=0;j<nbBankAccounts;j++) {
                acc = accs.get(j);
                if (acc.getNumber().equals(number))
                    return acc;
            }
        }
        return null;
    }
}
