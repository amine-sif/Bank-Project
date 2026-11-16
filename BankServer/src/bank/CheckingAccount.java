package bank;

//Question 2.1 :
public class CheckingAccount extends BankAccount {

    /**
     * Creates a checking account.
     *
     * @param number account number
     */
    public CheckingAccount(String number) {
        super(number, AccountType.CHECKING);
    }

    @Override
    public boolean credit(double amount) {
        if (amount>0) {
            balance += amount;
            history.push(new DepositOperation(amount));
            return true;
        }
        return false;
    }

    @Override
    public boolean debit(double amount) {
        if (amount>0 && balance>=amount) {
            balance -= amount;
            history.push(new WithdrawalOperation(amount));
            return true;
        }
        return false;
    }

    @Override
    public boolean payBill(String billNumber, double amount, String description) {
        if (amount>0 && balance>=amount) {
            balance -= amount;
            history.push(new BillOperation(amount,billNumber,description));
            return true;
        }
        return false;
    }
    @Override
    public boolean transfer(double amount, String recipientAccountNumber) {
        if (amount>0) { //money taken from this account
            if (balance<amount)
                return false;
            balance -= amount;
            history.push(new TransferOperation(amount,recipientAccountNumber));
            return true;
        }
        if (amount<0) { //money deposited into this account
            balance += -amount;
            history.push(new TransferOperation(amount,recipientAccountNumber));
            return true;
        }
        return false;
    }
}
