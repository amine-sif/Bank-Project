package bank;

public class TransferOperation extends Operation{
    private double amount;
    private String recipientNumber;

    public TransferOperation(double amount, String recipientNumber) {
        super(OperationType.TRANSFER);
        this.amount = amount;
        this.recipientNumber = recipientNumber;
    }

    @Override
    public String toString() {
        String s = (amount>0)?"Sent to":"Received from";
        return super.toString() + "\t"+ Math.abs(amount) + "$\t"+s+" " + recipientNumber;
    }
}
