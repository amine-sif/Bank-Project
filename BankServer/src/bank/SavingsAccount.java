package bank;

//Question 4.1 :
public class SavingsAccount extends BankAccount {
    public final double OPERATION_FEE = 2;
    public final double LIMIT = 1000;
    private double interestRate;
    /**
     * Creates a savings account.
     *
     * @param number account number
     * @param interestRate interest rate
     */
    public SavingsAccount(String number, double interestRate) {
        super(number, AccountType.SAVINGS);
        this.interestRate = interestRate;
    }
    public void addInterest() {
       balance += balance*interestRate;
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
        double fee = (balance<LIMIT) ? OPERATION_FEE : 0;
        if (amount<=0 || balance<amount+fee) {
            return false;
        }
        balance -= amount + fee;
        history.push(new WithdrawalOperation(amount));
        return true;
    }
    @Override
    public boolean payBill(String billNumber, double amount, String description) {
        double fee = (balance<LIMIT) ? OPERATION_FEE : 0;
        if (amount<=0 || balance<amount+fee) {
            return false;
        }
        balance -= amount + fee;
        history.push(new BillOperation(amount,billNumber,description));
        return true;
    }
    @Override
    public boolean transfer(double amount, String recipientAccountNumber) {
        if (amount>0) { //money taken from this account
            double fee = (balance<LIMIT) ? OPERATION_FEE : 0;
            if (balance<amount+fee) {
                return false;
            }
            balance -= amount + fee;
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
