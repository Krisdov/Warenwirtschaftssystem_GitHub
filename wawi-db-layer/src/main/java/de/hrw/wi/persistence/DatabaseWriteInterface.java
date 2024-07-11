package de.hrw.wi.persistence;

/**
 * @author andriesc
 *
 */
public interface DatabaseWriteInterface {

    /**
     * Fügt ein Lager mit der gegebenen Nummer und Beschaffenheit der Fächer hinzu.
     * 
     * @param number
     *            die Nummer des Lagers
     * @param maxNumberOfBins
     *            Anzahl der Fächer, die das Lager enthalten soll
     * @param maxSizeOfBins
     *            die einheitliche, maximale Größe der Fächer als Zahl
     */
    void addWarehouse(int number, int maxNumberOfBins, int maxSizeOfBins);

    /**
     * Fügt einen neuen Artikel in die Liste der Artikel ein, damit diese später abgespeichert
     * werden können.
     * 
     * @param articleCode
     *            Der Artikelcode mit 13 Stellen
     * @param name
     *            Der Name des Artikels
     * @param size
     *            Die Größe des Artikels
     */
    void addProduct(String articleCode, String name, int size);

    /**
     * Löscht einen Artikel mit gegebenem Code komplett aus der Datenbank: Sowohl aus den
     * Lagerbeständen wie auch aus der Liste der Produkte.
     * 
     * @param articleCode
     *            der Code des zu löschenden Artikels
     */
    void deleteProduct(String articleCode);

    /**
     * Ändert die Belegung eines Faches in einem Lager. Ist das Fach schon belegt, wird die alte
     * Belegung überschrieben falls das Fach nicht bereits durch einen Artikel mit einem anderen
     * Code belegt ist (Es wird also nur die Menge überschrieben).
     * 
     * @param numberOfWarehouse
     *            die Nummer des Lagers, in dem das Fach liegt
     * @param numberOfBin
     *            die Nummer des Faches, dessen Belegung geändert werden soll
     * @param articleCode
     *            der Code des Artikels, der im Fach gelagert wird
     * @param amount
     *            Menge der Artikel mit dem gegebenen Code
     */
    void setStock(int numberOfWarehouse, int numberOfBin, String articleCode, int amount);
}
