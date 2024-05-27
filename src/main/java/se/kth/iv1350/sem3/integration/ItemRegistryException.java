package se.kth.iv1350.sem3.integration;

public class ItemRegistryException extends RuntimeException {
    /**
     * Instanciates condition described in message parameter
     * 
     * @param msg A message that describes the error
     */
    public ItemRegistryException(String msg) {
        super(msg);
    }
}
