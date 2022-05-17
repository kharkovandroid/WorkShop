package com.gmail.valvol98.command;

import com.gmail.valvol98.data.UnChangeData;
import com.gmail.valvol98.db.DBException;
import com.gmail.valvol98.db.DBManager;
import com.gmail.valvol98.db.entity.Account;
import com.gmail.valvol98.db.entity.User;
import com.gmail.valvol98.service.ServiceCalculation;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Command class to finding limited accounts collection
 *
 * @author V.Volovshchykov
 *
 */
public class NextAccountPageCommand implements Command {

    private static final Logger log = Logger.getLogger(NextAccountPageCommand.class);

    /**
     * Execute method to command pattern that provide finding limited accounts collection
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        log.debug("NextAccountPageCommand Starts");

        int startRow  = Integer.parseInt(req.getParameter("startRow"));
        UnChangeData.ROW_SINCE_FIND_LIMITED_ACCOUNTS_COLLECTION = startRow;
        log.trace("Start row --> " + startRow);

        String address = UnChangeData.COMMAND_USER_START_PAGE;

        log.debug("NextAccountPageCommand Finished, now go to address --> " + address);
        return address;
    }
}
