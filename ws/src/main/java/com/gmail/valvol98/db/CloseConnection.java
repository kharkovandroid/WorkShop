package com.gmail.valvol98.db;

import java.sql.SQLException;

public class CloseConnection {

    public void close(AutoCloseable ac) throws SQLException {
        if (ac != null) {
            try {
                ac.close();
            } catch (Exception ex) {
                throw new SQLException("errorMessage.ErrorDuringCloseConnection", ex);
            }
            ac = null;
        }
    }
}
