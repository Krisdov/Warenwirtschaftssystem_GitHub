package de.hrw.wi.service;

import java.util.Set;

import de.hrw.wi.business.Product;
import de.hrw.wi.business.StorageBin;

/**
 * Ein WarehoseManagementService stellt Funktionen zum Einlagern von Produkten, Auslagern von
 * Produkten etc. bereit. Er erhaelt seine Daten von den einzelnen Laegern (Warehouse), gibt aber
 * nie direkt Zugriff auf Laeger sondern maximal deren jeweilige Nummer.
 * 
 * @author andriesc
 *
 */
public interface WarehouseManagementServiceInterface {
    /**
     * Erzeugt die angegebene Anzahl von Laegern, alle mit der gleichen Menge an Faechern und
     * Groesse der einzelnen Faecher.
     * 
     * @param amount
     *            Anzahl zu erzeugender Laeger
     * @param numberOfBins
     *            Anzahl der Faecher jeweils in den Lägern
     * @param sizeOfEachBin
     *            Groesse der Faecher jeweils
     */
    void createWarehouses(int amount, int numberOfBins, int sizeOfEachBin);

    /**
     * @return Die Nummern der aktiven Laeger, egal ob in ihnen Produkte lagern oder nicht.
     */
    Set<Integer> getWarehouseNumbers();

    /**
     * Legt das Lager mit der angegebenen Nummer still. Ein Lager kann nicht stillgelegt werden,
     * wenn in ihm noch Gegenstaende gelagert sind. Nur ein leeres Lager kann stillgelegt werden.
     * 
     * @param number
     *            Nummer des stillzulegenden Lagers
     * @return true, wenn das Lager erfolgreich stillgelegt werden konnte
     */
    boolean removeWarehouse(int number);

    /**
     * Schlaegt Lager und Fach vor:
     * 
     * <ul>
     * <li>Existierendes Fach, wenn es das Produktschon gibt und der Platz im Fach reicht
     * 
     * <li>sonst Neues Fach im selben Lager, wenn das Lager noch Platz hat,
     * 
     * <li>sonst neues Fach in neuem Lager, wenn es noch ein Lager mit Platz gibt
     * 
     * <li>Sonst Fehler
     * </ul>
     * 
     * @param p
     *            Produkte
     * @param amount
     *            Anzahl
     * @return StorageBin der das angegebene Produkt in der angegebenen Anzahl aufnehmen kann, sonst
     *         null.
     */
    StorageBin proposeStorageBinFor(Product p, int amount);

    /**
     * Lagert angegebene Menge des angegebenen Produktes ein. Kann das Produkt in der angegebenen
     * Anzahl nicht eingelagert werden, wird gar nichts eingelagert.
     * 
     * @param p
     *            Produkte
     * @param amount
     *            Anzahl
     * @return true, wenn alles eingelagert werden konnte, sonst false.
     */
    boolean placeIntoStock(Product p, int amount);

    /**
     * @param p
     *            Produkte
     * @return Gibt zurueck, welche Stueckzahl vom Produkt p in allen Laegern insgesamt vorhanden
     *         ist.
     */
    int availableStock(Product p);

    /**
     * Liefert alle Lagerplaetze fuer das angegebene Produkt zurueck.
     * 
     * @param p
     *            Produkte
     * @return Eine Menge aller Lagerplaetze fuer das angegebene Produkt
     */
    Set<StorageBin> storageBinsFor(Product p);

    /**
     * @return Gibt zurueck, welche Produkte insgesamt in den Laegern vorhanden sind, ohne die
     *         jeweilige Stueckzahl zu beruecksichtigen
     */
    Set<Product> availableStock();

    /**
     * Vom angegebenen Produkt die angegebene Menge auslagern.
     * 
     * @param p
     *            Das auszulagernde Produkt
     * @param amount
     *            Die auszulagernde Menge
     * @return Die Lagerplaetze, aus denen das Produkt ausgelagert wurde.
     */
    Set<StorageBin> removeFromStock(Product p, int amount);
}
