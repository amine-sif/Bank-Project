package bank;

public class WithdrawalOperation extends Operation {
    private double amount;
    public WithdrawalOperation(double amount) {
        super(OperationType.WITHDRAWAL);
        this.amount = amount;
    }
    @Override
    public String toString() {
        return super.toString() + "\t"+ amount;
    }
}
