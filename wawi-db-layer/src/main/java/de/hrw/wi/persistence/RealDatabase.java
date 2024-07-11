/**
 * 
 */
package de.hrw.wi.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author andriesc
 *
 */
@SuppressWarnings("ALL")
public class RealDatabase implements DatabaseReadInterface, DatabaseWriteInterface {
    private static final String DB_URL = "jdbc:hsqldb:file:../wawi-db-layer/database/wawi_db";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static final String PRODUCT_COULD_NOT_BE_ADDED = "Product could not be added.";
    private static final String PRODUCT_COULD_NOT_BE_DELETED = "Product could not be deleted.";
    private static final String WAREHOUSE_COULD_NOT_BE_ADDED = "Warehouse could not be added.";
    private static final String STOCK_COULD_NOT_BE_SET = "Stock could not be set.";
    private static final String BIN_IS_TOO_SMALL = "Bin is too small.";
    private static final String CONNECTION_CANNOT_BE_CLOSED =
            "Database connection cannot be closed.";

    private void safeClose(Connection c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (SQLException e) {
            throw new PersistenceException(CONNECTION_CANNOT_BE_CLOSED);
        }
    }

    /**
     * 
     * @param sql
     * @return ein <code>ResultSet</code>, der vom Aufrufer geschlossen werden muss
     * @throws SQLException
     */
    private ResultSet executeQuery(String sql) throws SQLException {
        Connection c = null;
        try {
            c = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            ResultSet rs = c.createStatement().executeQuery(sql);
            c.commit();
            return rs;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * Führt Schreibzugriffe auf die Datenbank durch
     * 
     * @param c
     *            Die Datenbankverbindung, mit der die Schreibzugriffe durchgeführt werden
     * @param statementList
     *            Eine Liste von PreparedStatements, die der Reihe nach ausgeführt werden
     * @return Die Anzahl der veränderten Datensätze oder -1, wenn ein Fehler aufgetreten ist
     * @throws SQLException
     */
    private int executePreparedStatementsUpdate(Connection c, List<PreparedStatement> statementList)
            throws SQLException {
        if (c == null) {
            return -1;
        }
        try {
            c.setAutoCommit(false);

            int result = -1;
            int resultSum = 0;
            Iterator<PreparedStatement> it = statementList.iterator();
            if (it.hasNext()) {
                do {
                    PreparedStatement statement = it.next();
                    result = statement.executeUpdate();
                    resultSum += result;
                } while (it.hasNext() && result > 0);
            }

            if (result != 0) {
                c.commit();
            } else {
                c.rollback();
                resultSum = 0;
            }

            return resultSum;
        } catch (SQLException e) {
            c.rollback();
        } finally {
            safeClose(c);
        }
        return 0;
    }

    private List<String> getStringList(ResultSet result) throws SQLException {
        List<String> list = new ArrayList<String>();
        while (result.next())
            list.add(result.getString(1));
        result.close();
        return list;
    }

    private List<Integer> getIntList(ResultSet result) throws SQLException {
        List<Integer> list = new ArrayList<Integer>();
        while (result.next())
            list.add(result.getInt(1));
        result.close();
        return list;
    }

    private int getInt(ResultSet result) throws SQLException {
        if (result.next()) {
            int resultInt = result.getInt(1);
            result.close();
            return resultInt;
        } else {
            result.close();
            return 0;
        }
    }

    private String getString(ResultSet result) throws SQLException {
        if (result.next()) {
            String resultStr = result.getString(1);
            result.close();
            return resultStr;
        } else {
            result.close();
            return "";
        }
    }

    /**
     * {@inheritDoc}
     */
    public Set<Integer> getAllWarehouses() {
        try {
            return new HashSet<Integer>(getIntList(executeQuery("SELECT number FROM WAREHOUSES")));
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getMaxAmountOfBins(int numberOfWarehouse) {
        PreparedStatement statement = null;
        Connection c = null;
        int result = -1;
        try {
            c = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            statement = c.prepareStatement("SELECT maxBin FROM WAREHOUSES WHERE number = ?");
            statement.setInt(1, numberOfWarehouse);
            result = getInt(statement.executeQuery());
        } catch (SQLException e) {
            result = -1;
        } finally {
            safeClose(c);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public int getMaxSizeOfBins(int numberOfWarehouse) {
        PreparedStatement statement = null;
        Connection c = null;
        int result = -1;
        try {
            c = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            statement = c.prepareStatement("SELECT maxSize FROM WAREHOUSES WHERE number = ?");
            statement.setInt(1, numberOfWarehouse);
            result = getInt(statement.executeQuery());
        } catch (SQLException e) {
            result = -1;
        } finally {
            safeClose(c);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public String getArticleCodeForBin(int numberOfWarehouse, int numberOfBin) {
        PreparedStatement statement = null;
        Connection c = null;
        String result = null;
        try {
            String sqlStr = "SELECT articleCode FROM STOCK WHERE number = ? AND BIN = ?";
            c = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            statement = c.prepareStatement(sqlStr);
            statement.setInt(1, numberOfWarehouse);
            statement.setInt(2, numberOfBin);
            result = getString(statement.executeQuery());
        } catch (SQLException e) {
            result = null;
        } finally {
            safeClose(c);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public int getAmountForBin(int numberOfWarehouse, int numberOfBin) {
        PreparedStatement statement = null;
        Connection c = null;
        int result = -1;
        try {
            String sqlStr = "SELECT amount FROM STOCK WHERE number = ? AND BIN = ?";
            c = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            statement = c.prepareStatement(sqlStr);
            statement.setInt(1, numberOfWarehouse);
            statement.setInt(2, numberOfBin);
            result = getInt(statement.executeQuery());
        } catch (SQLException e) {
            result = -1;
        } finally {
            safeClose(c);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public Set<String> getAllProducts() {
        try {
            return new HashSet<String>(
                    getStringList(executeQuery("SELECT articleCode FROM PRODUCTS")));
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getNameOfProduct(String articleCode) {
        PreparedStatement statement = null;
        Connection c = null;
        String result = null;
        try {
            String sqlStr = "SELECT name FROM PRODUCTS WHERE articleCode = ?";
            c = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            statement = c.prepareStatement(sqlStr);
            statement.setString(1, articleCode);
            result = getString(statement.executeQuery());
        } catch (SQLException e) {
            result = null;
        } finally {
            safeClose(c);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public int getSizeOfProduct(String articleCode) {
        PreparedStatement statement = null;
        Connection c = null;
        int result = -1;
        try {
            String sqlStr = "SELECT size FROM PRODUCTS WHERE articleCode = ?";
            c = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            statement = c.prepareStatement(sqlStr);
            statement.setString(1, articleCode);
            result = getInt(statement.executeQuery());
        } catch (SQLException e) {
            result = -1;
        } finally {
            safeClose(c);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void addProduct(String articleCode, String name, int size) throws PersistenceException {
        if (articleCode.length() != 13) {
            throw new PersistenceException("Article code does not have 13 digits.");
        }

        PreparedStatement statement = null;
        Connection c = null;
        try {
            c = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            String sqlStr = "INSERT INTO PRODUCTS VALUES(?, ?, ?)";
            statement = c.prepareStatement(sqlStr);
            statement.setString(1, articleCode);
            statement.setString(2, name);
            statement.setInt(3, size);
            int res = executePreparedStatementsUpdate(c, Arrays.asList(statement));
            if (res == 0) {
                throw new PersistenceException(PRODUCT_COULD_NOT_BE_ADDED);
            }
        } catch (SQLException e) {
            throw new PersistenceException(PRODUCT_COULD_NOT_BE_ADDED, e);
        } finally {
            safeClose(c);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void deleteProduct(String articleCode) throws PersistenceException {
        Connection c = null;
        try {
            c = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            var statementList = new ArrayList<PreparedStatement>();
            String sqlStrStock = "DELETE FROM STOCK WHERE articleCode = ? ";
            PreparedStatement statement = c.prepareStatement(sqlStrStock);
            statement.setString(1, articleCode);
            statementList.add(statement);

            String sqlStrProducts = "DELETE FROM PRODUCTS WHERE articleCode= ?";
            statement = c.prepareStatement(sqlStrProducts);
            statement.setString(1, articleCode);
            statementList.add(statement);

            int res = executePreparedStatementsUpdate(c, statementList);

            if (res == 0) {
                throw new PersistenceException(PRODUCT_COULD_NOT_BE_DELETED);
            }
        } catch (SQLException e) {
            throw new PersistenceException(PRODUCT_COULD_NOT_BE_DELETED);
        } finally {
            safeClose(c);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setStock(int numberOfWarehouse, int numberOfBin, String articleCode, int amount)
            throws PersistenceException {
        // Nur abspeichern, wenn es das Lager auch gibt
        HashSet<Integer> warehouses = new HashSet<Integer>(getAllWarehouses());
        if (!warehouses.contains(numberOfWarehouse)) {
            throw new PersistenceException("Warehouse does not exist.");
        }

        HashSet<String> products = new HashSet<String>(getAllProducts());

        // Nur abspeichern, wenn es das Produkt bereits in der Datenbank
        // gibt
        if (!products.contains(articleCode)) {
            throw new PersistenceException("Product does not exist.");
        }

        // Genuegend Platz? Falls nicht, abbrechen
        int maxSize = getMaxSizeOfBins(numberOfWarehouse);
        if (maxSize < amount * getSizeOfProduct(articleCode)) {
            throw new PersistenceException(BIN_IS_TOO_SMALL);
        }

        // Wenn wir bis hierhin kommen, existieren Produkt und Lager und das Fach ist groß genug

        // Lagerplatz leer?
        if (getArticleCodeForBin(numberOfWarehouse, numberOfBin).equals("")) {

            Connection c = null;
            try {
                c = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                String sqlStr = "INSERT INTO STOCK VALUES(?, ?, ?, ?)";
                PreparedStatement statement = c.prepareStatement(sqlStr);
                statement.setInt(1, numberOfWarehouse);
                statement.setInt(2, numberOfBin);
                statement.setString(3, articleCode);
                statement.setInt(4, amount);
                int res = executePreparedStatementsUpdate(c, Arrays.asList(statement));
                if (res == 0) {
                    throw new PersistenceException(STOCK_COULD_NOT_BE_SET);
                }

            } catch (SQLException e) {
                throw new PersistenceException(STOCK_COULD_NOT_BE_SET, e);
            } finally {
                safeClose(c);
            }
        } else {
            // Lagerplatz nicht leer
            // Es sind keine zwei verschiedenen Produkte pro Platz
            // moeglich, liegt bereits das richtige Produkt auf dem Platz?
            String exArtCode = getArticleCodeForBin(numberOfWarehouse, numberOfBin);

            if (!exArtCode.equals(articleCode)) {
                throw new PersistenceException("Bin is already taken for different product.");
            }

            Connection c = null;
            try {
                c = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                String sqlStr = "UPDATE STOCK SET articlecode=?, amount=? WHERE number=? AND bin=?";
                PreparedStatement statement = c.prepareStatement(sqlStr);
                statement.setString(1, articleCode);
                statement.setInt(2, amount);
                statement.setInt(3, numberOfWarehouse);
                statement.setInt(4, numberOfBin);
                int res = executePreparedStatementsUpdate(c, Arrays.asList(statement));
                if (res == 0) {
                    throw new PersistenceException(STOCK_COULD_NOT_BE_SET);
                }

            } catch (SQLException e) {
                throw new PersistenceException(STOCK_COULD_NOT_BE_SET, e);
            } finally {
                safeClose(c);
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    public void addWarehouse(int number, int maxNumberOfBins, int maxSizeOfBins)
            throws PersistenceException {
        if (number >= 0 && maxNumberOfBins >= 1 && maxSizeOfBins >= 1) {
            Connection c = null;

            try {
                c = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                String sqlStr = "INSERT INTO WAREHOUSES VALUES(?, ?, ?)";
                PreparedStatement statement = c.prepareStatement(sqlStr);
                statement.setInt(1, number);
                statement.setInt(2, maxNumberOfBins);
                statement.setInt(3, maxSizeOfBins);

                int res = executePreparedStatementsUpdate(c, Arrays.asList(statement));
                if (res == 0) {
                    throw new PersistenceException(WAREHOUSE_COULD_NOT_BE_ADDED);
                }
            } catch (SQLException e) {
                throw new PersistenceException(WAREHOUSE_COULD_NOT_BE_ADDED, e);
            } finally {
                safeClose(c);
            }
        } else {
            throw new PersistenceException(WAREHOUSE_COULD_NOT_BE_ADDED);
        }
    }

}
