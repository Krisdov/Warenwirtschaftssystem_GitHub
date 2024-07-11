package de.hrw.wi.service;

import java.util.HashSet;
import java.util.Set;

import de.hrw.wi.business.Product;
import de.hrw.wi.business.StorageBin;
import de.hrw.wi.persistence.DatabaseReadInterface;
import de.hrw.wi.persistence.DatabaseWriteInterface;

/**
 * @author kreuzeal
 */
public class WarehouseManagementService implements WarehouseManagementServiceInterface {

    private final DatabaseReadInterface dbRead;

    @SuppressWarnings("unused")
    private final DatabaseWriteInterface dbWrite;

    /**
     * Erzeugt einen neuen Service, um verschiedene Lager zu verwalten
     *
     * @param dbRead  Datenbankinterface für Lesezugriffe, das der Service verwenden soll
     * @param dbWrite Datenbankinterface für Schreibzugriffe, das der Service verwenden soll
     */
    public WarehouseManagementService(DatabaseReadInterface dbRead,
                                      DatabaseWriteInterface dbWrite) {
        this.dbRead = dbRead;
        this.dbWrite = dbWrite;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createWarehouses(int amount, int numberOfBins, int sizeOfEachBin) {
        // TODO service logic not implemented yet
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Integer> getWarehouseNumbers() {
        return dbRead.getAllWarehouses();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeWarehouse(int number) {
        // TODO service logic not implemented yet
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StorageBin proposeStorageBinFor(Product p, int amount) {
        // TODO service logic not implemented yet
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean placeIntoStock(Product p, int amount) {
        // TODO service logic not implemented yet
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int availableStock(Product p) {
        String code = p.getProductCode();
        int stock = 0;
        Set<Integer> allWarehouses = dbRead.getAllWarehouses();
        for (Integer w : allWarehouses) {
            int binCount = dbRead.getMaxAmountOfBins(w);
            for (int i = 0; i < binCount; i++) {
                String product = dbRead.getArticleCodeForBin(w, i);
                if (product != null && product.equals(code)) {
                    stock += dbRead.getAmountForBin(w, i);
                }
            }
        }
        return stock;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<StorageBin> storageBinsFor(Product p) {
        String code = p.getProductCode();
        Set<StorageBin> bins = new HashSet<>();
        Set<Integer> allWarehouses = dbRead.getAllWarehouses();
        for (Integer w : allWarehouses) {
            int binCount = dbRead.getMaxAmountOfBins(w);
            for (int i = 0; i < binCount; i++) {
                String product = dbRead.getArticleCodeForBin(w, i);
                if (product != null && product.equals(code)) {
                    bins.add(new StorageBin(w, i));
                }
            }
        }
        return bins;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Product> availableStock() {
        Set<Product> stock = new HashSet<>();
        Set<String> codes = dbRead.getAllProducts();
        for (String code : codes) {
            String name = dbRead.getNameOfProduct(code);
            Product product = new Product(name, code, dbRead.getSizeOfProduct(code));
            stock.add(product);
        }
        return stock;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<StorageBin> removeFromStock(Product p, int amount) {
        // TODO service logic not implemented yet
        return null;
    }

}
