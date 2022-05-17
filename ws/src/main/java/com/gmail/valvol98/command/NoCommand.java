package com.gmail.valvol98.command;

import com.gmail.valvol98.data.UnChangeData;
import com.gmail.valvol98.service.ServiceCalculation;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Command class to wrong command
 *
 * @author V.Volovshchykov
 *
 */
public class NoCommand implements Command {

    private static final Logger log = Logger.getLogger(ManagerCommand.class);

    /**
     * Execute method to command pattern that provide wrong command
     */
    @Override
    public String execute(HttpServletRequest req,
                          HttpServletResponse response) throws IOException, ServletException {
        log.debug("NoCommand starts");

        HttpSession session = req.getSession();
        String errorMessage = new ServiceCalculation().findFromResourceBundle(session, "errorMessage.CommandUnknow");
        String address = UnChangeData.PAGE_ERROR;
        req.getSession().setAttribute("errorSessionMessage", errorMessage);
        req.getSession().setAttribute("errorSessionCause", "");

        log.error("Set the request attribute: errorMessage --> " + errorMessage);

        log.debug("NoCommand finished");
        //System.out.println("No command----------------");
        return address;
    }
}
