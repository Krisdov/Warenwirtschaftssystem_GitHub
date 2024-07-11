/**
 * 
 */
package de.hrw.wi.business;

/**
 * @author andriesc
 * 
 *         StorageBin ist ein Ablagefach: Ein Fach, dass zu einem Lager mit einer Nummer gehört und
 *         auch als Fach eine Nummer hat.
 * 
 *         Ein StorageBin kann nach der Erzeugung nicht mehr verändert werden.
 * 
 */
public final class StorageBin {
    private final int wareHouseNumber;
    private final int bin;

    /**
     * @param wareHouseNumber
     *            Die Nummer des Lagers, zu dem dieser StorageBin gehört
     * @param binNumber
     *            Die Nummer des StorageBins in dem Lager (sollte eindeutig sein)
     */
    public StorageBin(int wareHouseNumber, int binNumber) {
        this.wareHouseNumber = wareHouseNumber;
        this.bin = binNumber;
    }

    /**
     * 
     * @return Die Nummer des Lagers, zu dem dieses Fach gehört
     */
    public int getWareHouseNumber() {
        return wareHouseNumber;
    }

    /**
     * 
     * @return Die Nummer des Faches im Lager
     */
    public int getBinNumber() {
        return bin;
    }

}
