package se.kth.iv1350.sem3.model;

public final class Amount {
    private final double amount;

    public Amount() {
        this(0.0);
    }

    public Amount(double amount) {
        this.amount = amount;
    }

    /**
     * Floors amount with 2 significant decimals
     * 
     * @param number number to be floored
     * @return returns floored amount
     */
    public Amount mathFloor() {
        return new Amount(Math.floor(amount * 100) / 100);
    }

    /**
     * Rounds amount with 1 significant decimal
     * 
     * @param number number to be floored
     * @return returns floored amount
     */
    public Amount mathRound() {
        return new Amount(Math.round(amount * 10.0) / 10.0);
    }

    public Amount multiplyAmt(double multiplier) {
        return new Amount(amount * multiplier);
    }

    public Amount subtractAmt(Amount subber) {
        return new Amount(amount - subber.amount);
    }

    public Amount addAmt(Amount adder) {
        return new Amount(amount + adder.amount);
    }

    public Amount addSavedAmt(Amount adder) {
        return new Amount(amount + adder.amount);
    }

    /**
     * Overrides the supertype method equals and adds functionality to fit Amount.
     * This allows for all use cases of <code>.equals()</code> method to apply for
     * <code>Amount</code> type too
     */
    @Override
    public boolean equals(Object comparator) {
        if (comparator == null || !(comparator instanceof Amount)) {
            return false;
        }
        Amount comparatorAmount = (Amount) comparator;
        return amount == comparatorAmount.amount;
    }

    /**
     * Overrides the supertype method toStrings and adds functionality to fit
     * Amount. This allows for all use cases of <code>.toString()</code> method to
     * apply for <code>Amount</code> type too.
     */
    @Override
    public String toString() {
        return Double.toString(amount);
    }
}
