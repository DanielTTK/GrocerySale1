package se.kth.iv1350.sem3.model;

/**
 * Type representing amount of money.
 */
public final class Amount {
    private final double amount;

    /**
     * Instanciates the amount to a specific double value.
     * 
     * @param amount the double value to be assigned as an amount instance.
     */
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

    /**
     * Multiplies amount value with a double, multipler.
     * 
     * @param multiplier the double to multiple the amount with.
     * @return returns an Amount with multiplied value
     */
    public Amount multiplyAmt(double multiplier) {
        return new Amount(amount * multiplier);
    }

    /**
     * Subtracts amount value with another amount value, subber.
     * 
     * @param subber the amount to subtract the original amount with.
     * @return returns resulting amount
     */
    public Amount subtractAmt(Amount subber) {
        return new Amount(amount - subber.amount);
    }

    /**
     * Adds amount value with another amount value, adder.
     * 
     * @param adder the amount to add the original amount with.
     * @return returns resulting amount
     */
    public Amount addAmt(Amount adder) {
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
