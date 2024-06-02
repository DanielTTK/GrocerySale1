package se.kth.iv1350.sem3.model;

import java.util.List;

import se.kth.iv1350.sem3.integration.ItemDTO;

/**
 * Creates reciepts that details sales
 */
public class Reciept {
    private Sale sale;

    /**
     * Instanciates a reciept depending on a particular sale.
     * 
     * @param sale
     */
    public Reciept(Sale sale) {
        this.sale = sale;
    }

    /**
     * Prints a reciept containing all information needed to prove that a sale has
     * taken place. Details the sale and payment.
     */
    public String createRecieptDigital() {
        StringBuilder reciept = new StringBuilder();

        List<ItemDTO> currBasket = sale.getBasket();
        write(reciept, "\n------------------ Begin receipt ------------------");
        write(reciept, "Time of Sale: " + sale.getSaleTime());
        for (int i = 0; i < currBasket.size(); i++) {
            ItemDTO itemInstance = currBasket.get(i);
            sale.calcTotal(i + 1);
            write(reciept,
                    itemInstance.getName() + "             " +
                            itemInstance.getQuantity() + " x "
                            + (itemInstance.getCost().multiplyAmt(1 / itemInstance.getQuantity()))
                            + "      " +
                            itemInstance.getCost().toString() + " SEK");
        }
        write(reciept,
                "\nTotal: " + (sale.getTotalCost()).mathFloor().toString() + " SEK" +
                        "\nVAT:   " + (sale.getTotalVAT()).mathFloor().toString() +

                        "\n\nPayment: " + (sale.getPaidAmount()).mathFloor().toString() +
                        "\nChange:  " + (sale.getChange()).mathFloor().toString() +
                        "\n------------------- End receipt -------------------" +
                        "\n\nChange to give the customer: " + (sale.getChange()).mathFloor().toString());

        return reciept.toString();
    }

    /**
     * functions exactly like <code>println</code>. Adds a linebreak at the end of
     * any message.
     * 
     * @param reciept the string builder, in this case the reciept you are adding to
     * @param string  whatever you want to add to the reciept
     */
    private void write(StringBuilder reciept, String string) {
        reciept.append(string);
        reciept.append("\n");
    }
}
