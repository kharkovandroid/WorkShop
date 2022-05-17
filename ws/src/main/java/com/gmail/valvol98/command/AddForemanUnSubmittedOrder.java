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

/**
 * Command class to adding foreman to order
 *
 * @author V.Volovshchykov
 *
 */
public class AddForemanUnSubmittedOrder implements Command {

    private static final Logger log = Logger.getLogger(AddForemanUnSubmittedOrder.class);

    /**
     * Execute method to command pattern that provide adding foreman to order
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        log.debug("AddForemanUnSubmittedOrder Starts");

        HttpSession session = req.getSession();
        int foremanId = Integer.valueOf(req.getParameter("checkedForeman"));
        String listIdUnSubmittedOrder = req.getParameter("listIdUnSubmittedOrder");
        log.trace("To Unsubmitted order put foreman Id --> " + foremanId);
        log.trace("List Unsubmitted order Id --> " + listIdUnSubmittedOrder);

        String address = UnChangeData.COMMAND_MANAGER_START_PAGE;
        boolean updateUnSubmittedOrder = false;
        try {
            updateUnSubmittedOrder = DBManager.getInstance().updateUnSubmittedOrder(foremanId, listIdUnSubmittedOrder);
        } catch (DBException ex) {
            address = UnChangeData.PAGE_ERROR;
            String errorMessage = new ServiceCalculation().findFromResourceBundle(session, ex.getMessage());
            req.getSession().setAttribute("errorSessionMessage", errorMessage);
            req.getSession().setAttribute("errorSessionCause", ex.getStackTrace());
        } finally {
            log.trace("Result of update Unsubmitted order --> " + updateUnSubmittedOrder);
        }

        log.debug("AddForemanUnSubmittedOrder Finished, now go to address --> " + address);
        return address;
    }
}
