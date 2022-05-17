package com.gmail.valvol98.db;

import javax.naming.NamingException;
import java.sql.SQLException;

/**
 * User's exception class.
 *
 * @author V.Volovshchykov
 *
 */
public class DBException extends Exception {

    /**
     * Constractor to create  DBException object.
     */
    public DBException(String message, Exception cause) {
        super(message, cause);
    }

    public DBException(String message) {
        super(message);
    }

}