package de.hrw.wi.persistence;

import java.util.Set;

/**
 * Das ist das Interface zur Datenbank. Warenhaeuser koennen Ihre Daten nur aus der Datenbank holen.
 * 
 * @author andriesc
 *
 *
 */
public interface DatabaseReadInterface {

    /**
     * Liefert eine Liste der Nummern aller Lager in der Datenbank zurück
     * 
     * @return Die Liste der Nummern aller Lager oder <code>null</code>, wenn ein Fehler aufgetreten
     *         ist
     * 
     */
    Set<Integer> getAllWarehouses();

    /**
     * Gibt die maximale Anzahl von Fächern eines Lagers zurück
     * 
     * @param numberOfWarehouse
     *            Die Nummer des Lagers
     * @return Die maximale Anzahl von Fächern oder <code>0</code>, wenn das Lager nicht existiert
     *         oder <code>-1</code>, wenn ein Fehler aufgetreten ist
     */
    int getMaxAmountOfBins(int numberOfWarehouse);

    /**
     * Gibt die maximale Größe der Fächer eines Lagers zurück
     * 
     * @param numberOfWarehouse
     *            Die Nummer des Lagers
     * @return Die maximale Größe der Fächer oder <code>0</code>, wenn das Lager nicht existiert
     *         oder <code>-1</code>, wenn ein Fehler aufgetreten ist
     */
    int getMaxSizeOfBins(int numberOfWarehouse);

    /**
     * Gibt den Code der Artikel zurück, die in einem Fach eines Lagers gespeichert sind
     * 
     * @param numberOfWarehouse
     *            Die Nummer des Lagers
     * @param numberOfBin
     *            Die Nummer des Fachs
     * @return Der einheitliche EAN-Code der Artikel, die im Fach gespeichert sind, ein leerer
     *         String bei leeren Fächern oder <code>null</code>, wenn ein Fehler aufgetreten ist
     */
    String getArticleCodeForBin(int numberOfWarehouse, int numberOfBin);

    /**
     * Gibt die Anzahl der Artikel zurück, die in einem Fach eines Lagers gespeichert sind
     * 
     * @param numberOfWarehouse
     *            Die Nummer des Lagers
     * @param numberOfBin
     *            Die Nummer des Fachs
     * @return Die Anzahl der Artikel, die im Fach gespeichert sind, <code>0</code> bei nicht
     *         existierendem Fach oder Lager oder <code>-1</code>, wenn ein Fehler aufgetreten ist
     */
    int getAmountForBin(int numberOfWarehouse, int numberOfBin);

    /**
     * Gibt eine Liste der Codes aller Artikel zurück, die in der Datenbank gespeichert sind, egal,
     * ob diese in einem Lager vorhanden sind oder nicht.
     * 
     * @return Eine Liste aller EAN-Artikelcodes in der Datenbank oder <code>null</code>, wenn ein
     *         Fehler aufgetreten ist
     * 
     */
    Set<String> getAllProducts();

    /**
     * Gibt den Artikelnamen zu einem Artikelcode zurück
     * 
     * @param articleCode
     *            Artikelcode (EAN, 13-stellig)
     * @return Der Artikelname zum Artikelcode zurück oder einen leeren String, wenn der Artikelcode
     *         unbekannt ist oder <code>null</code>, wenn ein Fehler aufgetreten ist
     */
    String getNameOfProduct(String articleCode);

    /**
     * Gibt die Größe des Artikels zurück
     * 
     * @param articleCode
     *            der EAN-Code des Artikels
     * @return Die Größe des Artikels oder <code>0</code>, wenn der Artikel nicht in der Liste der
     *         Artikel vorhanden ist oder <code>-1</code>, wenn ein Fehler aufgetreten ist
     */
    int getSizeOfProduct(String articleCode);
}
