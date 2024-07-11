package de.hrw.wi.persistence;

import org.dbunit.Assertion;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test für <code>DatabaseWriteInterface</code> of <code>RealDatabase</code>
 *
 * @author Andriessens
 */
public class DatabaseWriteInterfaceTest {
    private static final String DB_URL = "jdbc:hsqldb:file:../wawi-db-layer/database/wawi_db";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static final String DB_FULL_EXPORT_FILE_NAME = "db_full_export.xml";

    private static final String SAMSUNG_BLURAY_PLAYER_CODE = "8806085948587";
    private static final String APPLE_TV_CODE = "0885909560462";
    private static final String XORO_HRS_8560_SAT_RECEIVER_CODE = "4260001038167";
    private static final String XORO_HRS_8560_SAT_RECEIVER_NAME =
            "XORO HRS 8560 digitaler Satelliten-Receiver";
    private static final String SAMSUNG_UE48H6470_TV_CODE = "8806085983083";
    private static final String SAMSUNG_UE48H6470_TV_NAME =
            "Samsung UE48H6470 121 cm (48 Zoll) 3D LED-Backlight-Fernseher";

    RealDatabase db;
    IDatabaseTester databaseTester;

    @BeforeEach
    public void setUp() throws Exception {
        databaseTester = new JdbcDatabaseTester("org.hsqldb.jdbcDriver", DB_URL, USER, PASSWORD);
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new File(DB_FULL_EXPORT_FILE_NAME));
        databaseTester.setDataSet(dataSet);
        databaseTester.onSetup();
        db = new RealDatabase();
    }

    @Test
    public void testAddIllegalWarehouse() throws Exception {
        try {
            db.addWarehouse(-1, 0, 0);
        } catch (PersistenceException e) {
            IDataSet actual = databaseTester.getConnection().createDataSet();
            Assertion.assertEquals(
                    new FlatXmlDataSetBuilder().build(new File(DB_FULL_EXPORT_FILE_NAME)), actual);
            return;
        }
        fail();
    }

    @Test
    public void testDeleteProduct() {
        int numberOfProducts = db.getAllProducts().size();
        db.deleteProduct(APPLE_TV_CODE);
        assertEquals(numberOfProducts - 1, db.getAllProducts().size());

        String s = db.getNameOfProduct(APPLE_TV_CODE);
        assertEquals("", s);
    }

    @Test
    public void testAddWarehouse() {
        db.addWarehouse(2, 5, 20);
        assertEquals(3, db.getAllWarehouses().size());
    }

    @Test
    public void testAddProduct() {
        db.addProduct(XORO_HRS_8560_SAT_RECEIVER_CODE, XORO_HRS_8560_SAT_RECEIVER_NAME, 2);
        db.addProduct(SAMSUNG_UE48H6470_TV_CODE, SAMSUNG_UE48H6470_TV_NAME, 9);
        assertEquals(XORO_HRS_8560_SAT_RECEIVER_NAME,
                db.getNameOfProduct(XORO_HRS_8560_SAT_RECEIVER_CODE));
        assertEquals(SAMSUNG_UE48H6470_TV_NAME, db.getNameOfProduct(SAMSUNG_UE48H6470_TV_CODE));
    }

    @Test
    public void testSetStock() {
        try {
            // illegale Operation auf Datenbank ausfuehren
            db.setStock(1, 3, XORO_HRS_8560_SAT_RECEIVER_CODE, 2);
        } catch (PersistenceException e) {
            // Erwartete Exception wurde geworfen, nun pruefen, ob die
            // Fehlermeldung auch die erwartete ist und ob die Datenbank im
            // erwarteten richtigen Zustand ist.
            assertEquals("Product does not exist.", e.getMessage());
            db.setStock(1, 3, SAMSUNG_BLURAY_PLAYER_CODE, 3);
            assertEquals(3, db.getAmountForBin(1, 3));
            assertEquals(SAMSUNG_BLURAY_PLAYER_CODE, db.getArticleCodeForBin(1, 3));
            return;
        }
        fail();
    }

    @Test
    public void testSetStockUpdate() {
        // INSERT: 3 x Produkt in Fach 3 von Lager 1 einfügen 
        db.setStock(1, 3, SAMSUNG_BLURAY_PLAYER_CODE, 3);
        assertEquals(3, db.getAmountForBin(1, 3));
        assertEquals(SAMSUNG_BLURAY_PLAYER_CODE, db.getArticleCodeForBin(1, 3));
        // UPDATE: Produkt ändern und Bestand senken auf 2 x
        db.setStock(1, 3, SAMSUNG_BLURAY_PLAYER_CODE, 2);
        assertEquals(2, db.getAmountForBin(1, 3));
        assertEquals(SAMSUNG_BLURAY_PLAYER_CODE, db.getArticleCodeForBin(1, 3));
    }

}
