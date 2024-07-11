package de.hrw.wi.service;

import de.hrw.wi.business.Product;
import de.hrw.wi.persistence.RealDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Integrationstest für den WarehouseManagementService
 * <p>
 * 
 * @author Andriessens
 *
 */
public class WarehouseManagementServiceIntegrationTest {

    private WarehouseManagementServiceInterface wmService;

    @BeforeEach
    public void setup() {
        // Create real database
        RealDatabase db = new RealDatabase();
        wmService = new WarehouseManagementService(db, db);
    }

    @Test
    public void testGetWarehouseNumbers() {
        Set<Integer> numbers = wmService.getWarehouseNumbers();
        assertNotNull(numbers);
        assertEquals(2, numbers.size());
        for (Integer n : numbers) {
            if (n == 1) {
                return;
            }
        }
        fail("Aktives Lager mit Nummer 1 nicht gefunden!");
    }

    @Test
    public void testAvailableStockOfProduct() {
        Product p = new Product("Samsung BD-H5500 3D Blu-ray-Player", "8806085948587", 3);
        int anzahl = wmService.availableStock(p);
        assertEquals(3, anzahl);
    }
    
    @Test
    public void testAvailableStock() {
        Set<Product> availableStock = wmService.availableStock();

        String expected = "0885909560462";
        String nonExisting = "1234567890000";

        assertNotNull(availableStock);
        assertEquals(6, availableStock.size());

        boolean expectedFound = false;
        boolean nonexistingFound = false;
        for (Product product : availableStock) {
            if (product.getProductCode().equals(expected)) {
                expectedFound = true;
            } else if (product.getName().equals(nonExisting)) {
                nonexistingFound = true;
            }
        }
        assertTrue(expectedFound);
        assertFalse(nonexistingFound);
    }

}
