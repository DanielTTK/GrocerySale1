package se.kth.iv1350.sem3.integration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se.kth.iv1350.sem3.model.Amount;
import se.kth.iv1350.sem3.model.Sale;

import static org.junit.jupiter.api.Assertions.*;

public class SaleRegistryTest {
    private ByteArrayOutputStream printoutBuffer;
    private PrintStream originalSysOut;
    private SystemDelegator delegator;
    private SaleRegistry saleRegistry;
    private Sale sale;

    @BeforeEach
    public void renewSetUp() throws IOException {
        delegator = new SystemDelegator();
        saleRegistry = delegator.getSaleRegistry();
        printoutBuffer = new ByteArrayOutputStream();
        PrintStream inMemSysOut = new PrintStream(printoutBuffer);
        originalSysOut = System.out;
        System.setOut(inMemSysOut);

        sale = new Sale(delegator);
    }

    @AfterEach
    public void tearDown() {
        delegator = null;
        sale = null;
        saleRegistry = null;
        printoutBuffer = null;
        System.setOut(originalSysOut);
    }

    @Test
    public void testSaveSale() {
        SaleDTO dto = new SaleDTO(null, null, new Amount(0), new Amount(0), new Amount(0), new Amount(0));
        saleRegistry.saveSale(dto);

        int outcome = saleRegistry.getSaleRegistryList().size();
        int expectedOutcome = 1;

        assertTrue(expectedOutcome == outcome, "Nothing was added to the saleRegistry");
    }

    @Test
    public void testSaveSaleListSize() throws ItemDoesNotExistException {
        SaleRegistry accounting = delegator.getSaleRegistry();

        sale.addItemToBasket("abc123", 1);
        sale.calcTotal(1);

        sale.finishSale(new Amount(100));

        sale.logSaleInAccounting();

        int outcome = accounting.getSaleRegistryList().size();
        int expectedSaleListSize = 1;

        assertTrue(expectedSaleListSize == outcome, "Nothing was added to the saleRegistry/accounting");
    }

    @Test
    public void testSaveSaleSaleTime() throws ItemDoesNotExistException {
        SaleRegistry accounting = delegator.getSaleRegistry();

        sale.addItemToBasket("abc123", 1);
        sale.calcTotal(1);

        sale.finishSale(new Amount(100));

        sale.logSaleInAccounting();

        SaleDTO expectedStoredSaleDTO = new SaleDTO(sale.getSaleTime(), sale.getBasket(),
                sale.getTotalCost().mathFloor(),
                sale.getTotalVAT(), sale.getPaidAmount(), sale.getChange().mathFloor());

        SaleDTO storedSaleDTO = accounting.getSaleRegistryList().get(0);
        assertTrue(expectedStoredSaleDTO.getSaleSaleTime().equals(storedSaleDTO.getSaleSaleTime()),
                "SaleTime for sale instance inside accounting is incorrect!");
    }

    @Test
    public void testSaveSaleSaleBasket() throws ItemDoesNotExistException {
        SaleRegistry accounting = delegator.getSaleRegistry();

        sale.addItemToBasket("abc123", 1);
        sale.calcTotal(1);

        sale.finishSale(new Amount(100));

        sale.logSaleInAccounting();

        SaleDTO expectedStoredSaleDTO = new SaleDTO(sale.getSaleTime(), sale.getBasket(),
                sale.getTotalCost().mathFloor(),
                sale.getTotalVAT(), sale.getPaidAmount(), sale.getChange().mathFloor());

        SaleDTO storedSaleDTO = accounting.getSaleRegistryList().get(0);
        assertTrue(expectedStoredSaleDTO.getSaleBasket().equals(storedSaleDTO.getSaleBasket()),
                "Basket for sale instance inside accounting is incorrect!");
    }

    @Test
    public void testSaveSaleTotalCost() throws ItemDoesNotExistException {
        SaleRegistry accounting = delegator.getSaleRegistry();

        sale.addItemToBasket("abc123", 1);
        sale.calcTotal(1);

        sale.finishSale(new Amount(100));

        sale.logSaleInAccounting();

        SaleDTO expectedStoredSaleDTO = new SaleDTO(sale.getSaleTime(), sale.getBasket(),
                sale.getTotalCost(),
                sale.getTotalVAT(), sale.getPaidAmount(), sale.getChange());

        SaleDTO storedSaleDTO = accounting.getSaleRegistryList().get(0);

        assertTrue(expectedStoredSaleDTO.getSaleTotalCost().equals(storedSaleDTO.getSaleTotalCost()),
                "TotalCost for sale instance inside accounting is incorrect!");
    }

    @Test
    public void testSaveSaleSaleTotalVAT() throws ItemDoesNotExistException {
        SaleRegistry accounting = delegator.getSaleRegistry();

        sale.addItemToBasket("abc123", 1);
        sale.calcTotal(1);

        sale.finishSale(new Amount(100));

        sale.logSaleInAccounting();

        SaleDTO expectedStoredSaleDTO = new SaleDTO(sale.getSaleTime(), sale.getBasket(), sale.getTotalCost(),
                sale.getTotalVAT(), sale.getPaidAmount(), sale.getChange());

        SaleDTO storedSaleDTO = accounting.getSaleRegistryList().get(0);

        assertTrue(expectedStoredSaleDTO.getSaleTotalVAT().equals(storedSaleDTO.getSaleTotalVAT()),
                "TotalVAT for sale instance inside accounting is incorrect!");
    }

    @Test
    public void testSaveSaleSalePaidAmount() throws ItemDoesNotExistException {
        SaleRegistry accounting = delegator.getSaleRegistry();

        sale.addItemToBasket("abc123", 1);
        sale.calcTotal(1);

        sale.finishSale(new Amount(100));

        sale.logSaleInAccounting();

        SaleDTO expectedStoredSaleDTO = new SaleDTO(sale.getSaleTime(), sale.getBasket(), sale.getTotalCost(),
                sale.getTotalVAT(), sale.getPaidAmount(), sale.getChange());

        SaleDTO storedSaleDTO = accounting.getSaleRegistryList().get(0);

        assertTrue(expectedStoredSaleDTO.getSalePaidAmount().equals(storedSaleDTO.getSalePaidAmount()),
                "PaidAmount for sale instance inside accounting is incorrect!");
    }

    @Test
    public void testSaveSaleSaleChange() throws ItemDoesNotExistException {
        SaleRegistry accounting = delegator.getSaleRegistry();

        sale.addItemToBasket("abc123", 1);
        sale.calcTotal(1);

        sale.finishSale(new Amount(100));

        sale.logSaleInAccounting();

        SaleDTO expectedStoredSaleDTO = new SaleDTO(sale.getSaleTime(), sale.getBasket(), sale.getTotalCost(),
                sale.getTotalVAT(), sale.getPaidAmount(), sale.getChange());

        SaleDTO storedSaleDTO = accounting.getSaleRegistryList().get(0);

        assertTrue(expectedStoredSaleDTO.getSaleChange().equals(storedSaleDTO.getSaleChange()),
                "Change for sale instance inside accounting is incorrect!");
    }
}
