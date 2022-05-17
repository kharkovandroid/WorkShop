package com.gmail.valvol98.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * Main interface for the Command pattern implementation.
 *
 * @author V.Volovshchykov
 *
 */
public interface Command extends Serializable {

    static final long serialVersionUID = 3294332963257082854L;

    /**
     * Execution method for command.
     * @return Address to go once the command is executed.
     */
    String execute(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException;

}
