package bank;

public class DepositOperation extends Operation {
    private double amount;
    public DepositOperation(double amount) {
        super(OperationType.DEPOSIT);
        this.amount = amount;
    }
    @Override
    public String toString() {
        return super.toString() + "\t"+ amount;
    }
}
