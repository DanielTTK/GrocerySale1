package se.kth.iv1350.sem3.model;

public class Pay {
    private final double totalAmount;
    private final double totalVAT;

    public Pay(double totalAmount, double totalVAT) { // make calccost here?
        this.totalAmount = totalAmount;
        this.totalVAT = totalVAT;
    }

    public double getTotal() {
        return totalAmount;
    }

    public double getTotalVAT() {
        return totalVAT;
    }
}
