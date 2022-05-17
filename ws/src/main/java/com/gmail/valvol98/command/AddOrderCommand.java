package com.gmail.valvol98.command;

import com.gmail.valvol98.data.UnChangeData;
import com.gmail.valvol98.db.DBException;
import com.gmail.valvol98.db.DBManager;
import com.gmail.valvol98.db.entity.User;
import com.gmail.valvol98.service.ServiceCalculation;
import com.mysql.cj.Constants;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Command class to adding new order
 *
 * @author V.Volovshchykov
 *
 */
public class AddOrderCommand implements Command {

    private static final Logger log = Logger.getLogger(AddOrderCommand.class);

    /**
     * Execute method to command pattern that provide adding new order
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        log.debug("AddOrder Starts");

        HttpSession session = req.getSession();
        User user = (User)session.getAttribute("loggedUser");
        int userId = user.getId();
        String description = req.getParameter("description");

        log.trace("User Id --> " + userId);
        log.trace("Description --> " + description);

        String errorMessage = null;
        //String address = UnChangeData.PAGE_USER;
        String address = UnChangeData.COMMAND_USER_START_PAGE;
        session.setAttribute("isDescriptionExists", "-1");
        if (description == null || description.isEmpty()) {
            errorMessage = "Description cannot be empty";
            session.setAttribute("isDescriptionExists", "no");
            log.error("errorMessage --> " + errorMessage);
            return address;
        }
        boolean insertResult = false;
        try {
            insertResult = DBManager.getInstance().insertOrderByUser(userId, description);
        } catch (DBException ex) {
            address = UnChangeData.PAGE_ERROR;
            errorMessage = new ServiceCalculation().findFromResourceBundle(session, ex.getMessage());
            req.getSession().setAttribute("errorSessionMessage", errorMessage);
            req.getSession().setAttribute("errorSessionCause", ex.getStackTrace());
        } finally {
            log.trace("Result of Insert Order By User --> " + insertResult);
        }

        log.debug("AddOrder Finished, now go to address --> " + address);
        return address;
    }
}
