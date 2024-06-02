package se.kth.iv1350.sem3.model;

import java.util.List;

import se.kth.iv1350.sem3.integration.ItemDTO;

public class Reciept {
    private Sale sale;

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
                            + sale.mathRound(itemInstance.getCost() / itemInstance.getQuantity())
                            + "      " +
                            itemInstance.getCost() + " SEK");
        }
        write(reciept,
                "\nTotal: " + sale.mathFloor(sale.getTotalCost()) + " SEK" +
                        "\nVAT:   " + sale.mathFloor(sale.getTotalVAT()) +

                        "\n\nPayment: " + sale.mathFloor(sale.getPaidAmount()) +
                        "\nChange:  " + sale.mathFloor(sale.getChange()) +
                        "\n------------------- End receipt -------------------" +
                        "\n\nChange to give the customer: " + sale.mathFloor(sale.getChange()));

        return reciept.toString();
    }

    /**
     * functions exactly like <code>println</code>. Adds a linebreak at the end of
     * any message.
     * 
     * @param reciept
     * @param string
     */
    private void write(StringBuilder reciept, String string) {
        reciept.append(string);
        reciept.append("\n");
    }
}
