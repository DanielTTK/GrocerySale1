package se.kth.iv1350.sem3.view;

import java.util.Random;

public class RandomImplementation extends Random {

    /**
     * Adds logging before calling <code>super.nextInt(maxInt)</code>
     */
    @Override
    public int nextInt(int maxInt) {
        System.out.println("Generating a random number from 0 to " + maxInt);
        System.out.print("Random number generated is: ");

        return super.nextInt(maxInt);
    }
}
