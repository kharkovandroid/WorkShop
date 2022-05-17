package com.gmail.valvol98.command;

import com.gmail.valvol98.data.UnChangeData;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Command class that provide leaving from the application
 *
 * @author V.Volovshchykov
 *
 */
public class LogOutCommand implements Command {

    private static final Logger log = Logger.getLogger(LogOutCommand.class);

    /**
     * Execute method to command pattern that provide leaving from the application
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        log.debug("LogOutCommand Starts");

        HttpSession session = req.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        String address = UnChangeData.PAGE_INDEX;
        UnChangeData.ROW_SINCE_FIND_LIMITED_ACCOUNTS_COLLECTION = 0;
        return address;
    }
}
