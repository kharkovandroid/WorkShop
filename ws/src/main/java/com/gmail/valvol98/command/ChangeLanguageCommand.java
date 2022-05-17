package com.gmail.valvol98.command;

import com.gmail.valvol98.data.UnChangeData;
import com.gmail.valvol98.db.DBException;
import com.gmail.valvol98.db.DBManager;
import com.gmail.valvol98.db.entity.User;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Command class to change interface language
 *
 * @author V.Volovshchykov
 *
 */
public class ChangeLanguageCommand implements Command {

    private static final Logger log = Logger.getLogger(ChangeOrderStatusByForemanCommand.class);

    /**
     * Execute method to command pattern that provide changing interface language
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        log.debug("ChangeLanguageCommand Starts");

        String language = req.getParameter("lang");

        HttpSession session = req.getSession();
        session.setAttribute("language", language);
        session.setAttribute("isUserExists", "-1");

        log.trace("Change language to --> " + language);

        String address = UnChangeData.PAGE_INDEX;
        if (session.getAttribute("pageName") != null) {
            address = (String) session.getAttribute("pageName");
        }
        log.debug("ChangeLanguageCommand Finished, now go to address --> " + address);

        return address;
    }
}
