/**
 *
 */
package de.hrw.wi.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author andriesc
 *
 */
public class DatabaseReadInterfaceTest {
    private static final Set<Integer> ALLWAREHOUSES = new HashSet<>(Arrays.asList(0, 1));
    private static final String ARTICLE_CODE_WITHOUT_PRODUCT_IN_DATABASE = "0123456789123";
    private static final String SAMSUNG_BLURAY_PLAYER_CODE = "8806085948587";
    private static final String APPLE_TV_CODE = "0885909560462";
    private static final String TOMTOM_EUROPE_TRAFFIC_CODE = "0636926062442";
    private static final String LG_40UB800V_TV_CODE = "8806084893826";
    private static final String GIGASET_C430_AB_CODE = "4250366833286";
    private static final String IPOW_SELFIE_STANGE_CODE = "0799637096608";
    private static final Set<String> ALLPRODUCTS = new HashSet<>(
            Arrays.asList(SAMSUNG_BLURAY_PLAYER_CODE, APPLE_TV_CODE, TOMTOM_EUROPE_TRAFFIC_CODE,
                    LG_40UB800V_TV_CODE, GIGASET_C430_AB_CODE, IPOW_SELFIE_STANGE_CODE));
    RealDatabase db;

    /**
     */
    @BeforeEach
    public void setUp() {
        db = new RealDatabase();
    }

    private <E> void assertEqualSet(Set<E> expected, Set<E> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (E s : actual) {
            assertTrue(expected.contains(s));
        }
    }

    @Test
    public void testGetAllWarehouses() {
        assertEqualSet(ALLWAREHOUSES, db.getAllWarehouses());
    }

    @Test
    public void testGetAllProducts() {
        assertEqualSet(ALLPRODUCTS, db.getAllProducts());
    }

    @Test
    public void testAmountForBin() {
        assertEquals(1, db.getAmountForBin(0, 0));
        assertEquals(1, db.getAmountForBin(0, 1));
        assertEquals(1, db.getAmountForBin(0, 2));
        assertEquals(2, db.getAmountForBin(0, 3));
        assertEquals(2, db.getAmountForBin(0, 4));
        assertEquals(2, db.getAmountForBin(0, 5));
        assertEquals(4, db.getAmountForBin(0, 6));
        assertEquals(1, db.getAmountForBin(1, 0));
        assertEquals(1, db.getAmountForBin(1, 1));
        assertEquals(5, db.getAmountForBin(1, 2));
    }

    @Test
    public void testArticleCodeForBin() {
        assertEquals(SAMSUNG_BLURAY_PLAYER_CODE, db.getArticleCodeForBin(0, 0));
        assertEquals(SAMSUNG_BLURAY_PLAYER_CODE, db.getArticleCodeForBin(0, 1));
        assertEquals(SAMSUNG_BLURAY_PLAYER_CODE, db.getArticleCodeForBin(0, 2));
        assertEquals(APPLE_TV_CODE, db.getArticleCodeForBin(0, 3));
        assertEquals(TOMTOM_EUROPE_TRAFFIC_CODE, db.getArticleCodeForBin(0, 4));
        assertEquals(GIGASET_C430_AB_CODE, db.getArticleCodeForBin(0, 5));
        assertEquals(IPOW_SELFIE_STANGE_CODE, db.getArticleCodeForBin(0, 6));
        assertEquals(LG_40UB800V_TV_CODE, db.getArticleCodeForBin(1, 0));
        assertEquals(LG_40UB800V_TV_CODE, db.getArticleCodeForBin(1, 1));
        assertEquals(TOMTOM_EUROPE_TRAFFIC_CODE, db.getArticleCodeForBin(1, 2));
        // leeres Fach, da Lager nicht existiert
        assertEquals("", db.getArticleCodeForBin(3, 4));
    }

    @Test
    public void testMaxAmountOfBins() {
        assertEquals(30, db.getMaxAmountOfBins(0));
        assertEquals(30, db.getMaxAmountOfBins(1));
        // Lager 99 gibt es nicht
        assertEquals(0, db.getMaxAmountOfBins(99));
    }

    @Test
    public void testMaxSizeOfBins() {
        assertEquals(5, db.getMaxSizeOfBins(0));
        assertEquals(10, db.getMaxSizeOfBins(1));
        // Lager 99 gibt es nicht
        assertEquals(0, db.getMaxSizeOfBins(99));
    }

    @Test
    public void testGetNameOfProduct() {
        assertEquals("Samsung BD-H5500 3D Blu-ray-Player",
                db.getNameOfProduct(SAMSUNG_BLURAY_PLAYER_CODE));
        assertEquals("Apple TV MD199FD/A", db.getNameOfProduct(APPLE_TV_CODE));
        assertEquals("TomTom Start 25 M Europe Traffic",
                db.getNameOfProduct(TOMTOM_EUROPE_TRAFFIC_CODE));
        assertEquals("LG 40UB800V 101 cm (40 Zoll) LED-Backlight-Fernseher",
                db.getNameOfProduct(LG_40UB800V_TV_CODE));
        assertEquals("Gigaset C430 A Duo Dect-Schnurlostelefon mit Anrufbeantworter",
                db.getNameOfProduct(GIGASET_C430_AB_CODE));
        assertEquals("Ipow schwarz Selfie Stange", db.getNameOfProduct(IPOW_SELFIE_STANGE_CODE));
        assertEquals("", db.getNameOfProduct(ARTICLE_CODE_WITHOUT_PRODUCT_IN_DATABASE));
    }

    @Test
    public void testSizeOfProduct() {
        assertEquals(3, db.getSizeOfProduct(SAMSUNG_BLURAY_PLAYER_CODE));
        assertEquals(2, db.getSizeOfProduct(APPLE_TV_CODE));
        assertEquals(2, db.getSizeOfProduct(TOMTOM_EUROPE_TRAFFIC_CODE));
        assertEquals(8, db.getSizeOfProduct(LG_40UB800V_TV_CODE));
        assertEquals(2, db.getSizeOfProduct(GIGASET_C430_AB_CODE));
        assertEquals(1, db.getSizeOfProduct(IPOW_SELFIE_STANGE_CODE));
        assertEquals(0, db.getSizeOfProduct(ARTICLE_CODE_WITHOUT_PRODUCT_IN_DATABASE));
    }

}
