/**
 * 
 */
package de.hrw.wi.persistence.dbunit;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;

/**
 * @author andriesc
 *
 */
public final class DBUnitExporter {
    private final static String DB_URL = "jdbc:hsqldb:file:../wawi-db-layer/DUE_database/wawi_db";
    private final static String USER = "sa";
    private final static String PASSWORD = "";

    private DBUnitExporter() {
        // vermeiden, dass der Konstruktur dieser Hilfsklasse aufgerufen wird:
        // Deshalb Sichtbarkeit private
    }

    /**
     * @param args
     *            nicht verwendet
     * @throws SQLException
     * @throws DatabaseUnitException
     * @throws IOException
     * @throws FileNotFoundException
     * 
     */
    public static void main(String[] args)
            throws SQLException, DatabaseUnitException, FileNotFoundException, IOException {
        // database connection
        Connection jdbcConnection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);

        // DBUnit's DatabaseSequenceFilter automatically determines table order
        // using foreign keys information. Without, IDatabaseTester.onSetup()
        // would produce constraint violation exceptions on foreign keys.
        ITableFilter filter = new DatabaseSequenceFilter(connection);

        // full database export
        IDataSet fullDataSet = new FilteredDataSet(filter, connection.createDataSet());

        FlatXmlDataSet.write(fullDataSet, new FileOutputStream("db_full_export.xml"));
        connection.close();
        jdbcConnection.close();
    }

}
