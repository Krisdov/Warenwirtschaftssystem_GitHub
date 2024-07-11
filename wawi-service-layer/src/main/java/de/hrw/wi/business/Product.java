/**
 * 
 */
package de.hrw.wi.business;

/**
 * @author andriesc
 *
 *         Die Klasse <code>Product</code> repräsentiert einen Artikel, der in einem Lager ein- und
 *         ausgelagert werden kann. Artikel haben einen Namen, einen 13-stelligen EAN-Code und eine
 *         Größe.
 *
 */
public class Product {
    private static final String PRODUCT_CODE_DOES_NOT_HAVE_13_DIGITS =
            "Product code does not have 13 digits.";

    /**
     * name speichert die Bezeichnung des Produkts.
     */
    private String name;

    /**
     * productCode speichert die 13stellige EAN-Nummer. productCode muss immer eine 13stellige
     * Zeichenkette aus den Ziffern 0-9 sein.
     */
    private String productCode;

    /**
     * size speichert die Groesse des Produktes als Zahl. Ein Produkt kann beispielsweise die
     * Groesse 1, 5, 8 oder 24 haben. Die Groesse darf nicht 0 und nicht negativ sein.
     */
    private int size;

    /**
     * Erzeugt einen neuen Artikel.
     * 
     * @param name
     *            Der Name des Artikels
     * @param productCode
     *            Der 13-stellige EAN-Code des Artikels.
     * @param size
     *            Die Größe des Artikels
     */
    public Product(String name, String productCode, int size) {
        super();
        if (productCode.length() != 13) {
            throw new RuntimeException(PRODUCT_CODE_DOES_NOT_HAVE_13_DIGITS);
        }
        this.name = name;
        this.productCode = productCode;
        this.size = size;
    }

    /**
     * 
     * @return Gibt den Namen des Artikels zurück.
     */
    public String getName() {
        return name;
    }

    /**
     * Überschreibt den Namen eines Artikels.
     * 
     * @param name
     *            Der neue Name des Artikels.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return Der 13-stellige EAN-Code des Artikels.
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * Überschreibt den EAN-Code eines Artikels.
     * 
     * @param productCode
     *            Der neue 13-stellige Code des Artikels.
     */
    public void setProductCode(String productCode) {
        if (productCode.length() != 13) {
            throw new RuntimeException(PRODUCT_CODE_DOES_NOT_HAVE_13_DIGITS);
        }
        this.productCode = productCode;
    }

    /**
     * Gibt die Größe des Artikels zurück.
     * 
     * @return Die Größe des Artikels.
     */
    public int getSize() {
        return size;
    }

    /**
     * Überschreibt die Größe des Artikels.
     * 
     * @param size
     *            Die neue Größe des Artikels.
     */
    public void setSize(int size) {
        this.size = size;
    }

}
