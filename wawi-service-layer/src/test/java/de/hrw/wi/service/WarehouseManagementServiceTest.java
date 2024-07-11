package de.hrw.wi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.hrw.wi.business.Product;
import de.hrw.wi.business.StorageBin;
import de.hrw.wi.persistence.DatabaseReadInterface;
import de.hrw.wi.persistence.DatabaseWriteInterface;

/**
 * 
 * @author Andriessens
 *
 */
public class WarehouseManagementServiceTest {

    private static final String ARTICLE_1_CODE = "0799637096608";
    private static final String ARTICLE_1_NAME = "Nutella";
    private static final int ARTICLE_1_SIZE = 1;
    private static final String ARTICLE_2_CODE = "1234567895608";
    private static final String ARTICLE_2_NAME = "Marmelade";
    private static final int ARTICLE_2_SIZE = 1;

    private WarehouseManagementServiceInterface wmService;
    private DatabaseReadInterface dbReadMock;
    private DatabaseWriteInterface dbWriteMock;

    @BeforeEach
    public void setup() {
        // Create mock object
        dbReadMock = Mockito.mock(DatabaseReadInterface.class);
        dbWriteMock = Mockito.mock(DatabaseWriteInterface.class);

        Set<Integer> numbers = new HashSet<>(Arrays.asList(123, 456));
        Set<String> productCodes = new HashSet<>(Arrays.asList(ARTICLE_1_CODE, ARTICLE_2_CODE));

        when(dbReadMock.getAllWarehouses()).thenReturn(numbers);
        when(dbReadMock.getAmountForBin(123, 2)).thenReturn(10);
        when(dbReadMock.getAmountForBin(456, 1)).thenReturn(2);
        when(dbReadMock.getMaxAmountOfBins(123)).thenReturn(3);
        when(dbReadMock.getMaxAmountOfBins(456)).thenReturn(2);

        when(dbReadMock.getArticleCodeForBin(123, 2)).thenReturn(ARTICLE_1_CODE);
        when(dbReadMock.getArticleCodeForBin(456, 1)).thenReturn(ARTICLE_1_CODE);
        when(dbReadMock.getArticleCodeForBin(123, 1)).thenReturn(ARTICLE_2_CODE);

        when(dbReadMock.getAllProducts()).thenReturn(productCodes);
        when(dbReadMock.getNameOfProduct(ARTICLE_1_CODE)).thenReturn(ARTICLE_1_NAME);
        when(dbReadMock.getNameOfProduct(ARTICLE_2_CODE)).thenReturn(ARTICLE_2_NAME);

        // TODO dbWriteMock does not have an interface description yet

        wmService = new WarehouseManagementService(dbReadMock, dbWriteMock);
    }

    @Test
    public void testGetWarehouseNumbers() {
        Set<Integer> numbers = wmService.getWarehouseNumbers();
        assertNotNull(numbers);
        assertEquals(2, numbers.size());
        for (Integer n : numbers) {
            if (n == 456) {
                return;
            }
        }
        fail("Aktives Lager mit Nummer 456 nicht gefunden!");
    }

    @Test
    public void testAvailableStockOfProduct() {
        Product p = new Product(ARTICLE_1_NAME, ARTICLE_1_CODE, ARTICLE_1_SIZE);
        int anzahl = wmService.availableStock(p);
        assertEquals(12, anzahl);
    }

    @Test
    public void testAvailableStock() {
        Set<Product> availableStock = wmService.availableStock();
        assertNotNull(availableStock);
        assertEquals(2, availableStock.size());

        boolean nutellaFound = false;
        boolean marmeladeFound = false;
        for (Product product : availableStock) {
            if (product.getName().equals(ARTICLE_1_NAME)) {
                nutellaFound = true;
            } else if (product.getName().equals(ARTICLE_2_NAME)) {
                marmeladeFound = true;
            }
        }
        assertTrue(nutellaFound && marmeladeFound);

        verify(dbReadMock, times(1)).getNameOfProduct(ARTICLE_1_CODE);
        verify(dbReadMock, times(1)).getNameOfProduct(ARTICLE_2_CODE);

    }

    @Test
    public void testStorageBinsFor() {
        Product product = new Product(ARTICLE_2_NAME, ARTICLE_2_CODE, ARTICLE_2_SIZE);
        Set<StorageBin> bins = wmService.storageBinsFor(product);
        assertNotNull(bins);
        assertEquals(1, bins.size());

        for (StorageBin bin : bins) {
            assertEquals(1, bin.getBinNumber());
            assertEquals(123, bin.getWareHouseNumber());
        }

        verify(dbReadMock).getAllWarehouses();
        verify(dbReadMock, times(2)).getMaxAmountOfBins(anyInt());
        verify(dbReadMock, times(5)).getArticleCodeForBin(anyInt(), anyInt());
    }

}
