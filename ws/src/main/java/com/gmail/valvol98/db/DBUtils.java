package com.gmail.valvol98.db;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Access to database.
 *
 * @author V.Volovshchykov
 *
 */
public class DBUtils {

    private static DBUtils instance;

    /**
     * Method that discribe Singleton to create  DBUtils object.
     */
    public static synchronized DBUtils getInstance() throws DBException {
        if (instance == null) {
            instance = new DBUtils();
        }
        return instance;
    }

    private DataSource ds;

    /**
     * Constractor to create  DBUtils object.
     */
    private DBUtils() throws DBException {
        try {
            Context initContext = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            ds = (DataSource)envContext.lookup("jdbc/ResourceWorkShopDB");
        } catch (NamingException ex) {
            throw new DBException("errorMessage.DataSourceIsUnavailable", ex);
        }
    }

    /**
     * Method that discribe get connection to database.
     * @return connection
     */
    public Connection getConnection() throws DBException {
        Connection con = null;
        try {
            con = ds.getConnection();
        } catch (SQLException ex) {
            throw new DBException("errorMessage.ConnectionIsUnavailable", ex);
        }
        return con;
    }

}
