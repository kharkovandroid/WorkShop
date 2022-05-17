package com.gmail.valvol98.command;

import com.gmail.valvol98.data.UnChangeData;
import com.gmail.valvol98.db.DBException;
import com.gmail.valvol98.db.DBManager;
import com.gmail.valvol98.db.entity.User;
import com.gmail.valvol98.service.ServiceCalculation;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Command class to adding funds to account
 *
 * @author V.Volovshchykov
 *
 */
public class AddSumToAccountCommand implements Command{

    private static final Logger log = Logger.getLogger(AddSumToAccountCommand.class);

    /**
     * Execute method to command pattern that provide funds to account
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        log.debug("AddSumToAccountCommand Starts");

        HttpSession session = req.getSession();
        User manager = (User)session.getAttribute("loggedUser");
        int managerId = manager.getId();

        int userId = Integer.valueOf(req.getParameter("checkedUser"));
        double sumToAccount;
        try {
            sumToAccount = Double.valueOf(req.getParameter("sumToAccount"));
            if (sumToAccount <= 0) {
                throw new DBException("errorMessage.AddSummaIsNegative");
            }
        } catch (NumberFormatException ex) {
            String errorMessage = new ServiceCalculation().findFromResourceBundle(session, "errorMessage.AddSummaIsNotNumber");
            session.setAttribute("errorSessionMessage", errorMessage);
            session.setAttribute("errorSessionCause", ex.getStackTrace());
            return UnChangeData.PAGE_ERROR;
        } catch (DBException ex) {
            String errorMessage = new ServiceCalculation().findFromResourceBundle(session, ex.getMessage());
            session.setAttribute("errorSessionMessage", errorMessage);
            session.setAttribute("errorSessionCause", ex.getStackTrace());
            return UnChangeData.PAGE_ERROR;
        }

        log.trace("User Id --> " + userId);
        log.trace("Summa to account --> " + sumToAccount);

        boolean insertSumToAccount = false;
        String address = UnChangeData.PAGE_ERROR;
        try {
            insertSumToAccount = DBManager.getInstance().insertSumToAccount(managerId, userId, sumToAccount);
            address = UnChangeData.COMMAND_MANAGER_START_PAGE;
            log.debug("AddSumToAccountCommand Finished, now go to address --> " + address);
        } catch(DBException ex) {
            String errorMessage = new ServiceCalculation().findFromResourceBundle(session, ex.getMessage());
            req.getSession().setAttribute("errorSessionMessage", errorMessage);
            req.getSession().setAttribute("errorSessionCause", ex.getStackTrace());
        } finally {
            log.trace("Result of Insert summa to account --> " + insertSumToAccount);
        }

        return address;
    }
}
