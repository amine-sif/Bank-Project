package bank;

public class BillOperation extends Operation {
    private double amount;
    private String number, description;

    public BillOperation(double amount, String number, String description) {
        super(OperationType.BILL);
        this.amount = amount;
        this.number = number;
        this.description = description;
    }
    @Override
    public String toString() {
        return super.toString() + "\t"+ amount + "$\t" + number+ "$\t" + description;
    }
}
