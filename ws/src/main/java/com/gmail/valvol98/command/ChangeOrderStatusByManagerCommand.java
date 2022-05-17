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
 * Command class to change order's status by manager
 *
 * @author V.Volovshchykov
 *
 */
public class ChangeOrderStatusByManagerCommand implements Command {

    private static final Logger log = Logger.getLogger(ChangeOrderStatusByManagerCommand.class);

    /**
     * Execute method to command pattern that provide changing order's status by manager
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        log.debug("ChangeOrderStatusByManagerCommand Starts");

        int orderId = Integer.parseInt(req.getParameter("orderId"));
        int statusId = Integer.parseInt(req.getParameter("statusId"));;
        int managerId = Integer.parseInt(req.getParameter("managerId"));;

        HttpSession session = req.getSession();
        User user = (User)session.getAttribute("loggedUser");

        log.trace("Change order status by maager for Order Id --> " + orderId);
        log.trace("Change order status by manager for Status Id --> " + statusId);
        log.trace("Change order status by manager --> " + user.getId());

        String address = UnChangeData.COMMAND_MANAGER_START_PAGE;
        boolean insertResult = false;

        try {
            insertResult = DBManager.getInstance().insertNextStatusToOrderStatusByOrderStatusManager(orderId, statusId, managerId);
        } catch (DBException ex) {
            address = UnChangeData.PAGE_ERROR;
            String errorMessage = new ServiceCalculation().findFromResourceBundle(session, ex.getMessage());
            req.getSession().setAttribute("errorSessionMessage", errorMessage);
            req.getSession().setAttribute("errorSessionCause", ex.getStackTrace());
        } finally {
            log.trace("Result of Change Order's Status by Manager --> " + insertResult);
        }

        return address;
    }
}
