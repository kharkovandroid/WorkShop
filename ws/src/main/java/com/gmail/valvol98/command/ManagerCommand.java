package com.gmail.valvol98.command;

import com.gmail.valvol98.data.UnChangeData;
import com.gmail.valvol98.db.DBException;
import com.gmail.valvol98.db.DBManager;
import com.gmail.valvol98.db.entity.Account;
import com.gmail.valvol98.db.entity.Order;
import com.gmail.valvol98.db.entity.User;
import com.gmail.valvol98.repositories.AllOrder;
import com.gmail.valvol98.service.ServiceCalculation;
import com.gmail.valvol98.service.ServiceOrder;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Command class to manager interface
 *
 * @author V.Volovshchykov
 *
 */
public class ManagerCommand implements Command {

    private static final Logger log = Logger.getLogger(ManagerCommand.class);

    /**
     * Execute method to command pattern that provide manager interface
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        log.debug("ManagerCommand Starts");

        HttpSession session = req.getSession();
        User user = (User)session.getAttribute("loggedUser");
        int userId = user.getId();

        log.trace("User Id --> " + userId);

        List<User> users = null;
        List<User> foremen = null;
        List<Order> unSubmittedOrders = null;
        List<Order> allOrders = null;
        String address = UnChangeData.PAGE_MANAGER;
        session.setAttribute("pageName", UnChangeData.COMMAND_MANAGER_START_PAGE);
        try {
            users = DBManager.getInstance().findUsersByRole(UnChangeData.ROLE_USER);
            req.setAttribute("GeneralAcoountsTable", users);

            foremen = DBManager.getInstance().findUsersByRole(UnChangeData.ROLE_FOREMAN);
            req.setAttribute("ForemenList", foremen);

            unSubmittedOrders = DBManager.getInstance().findUnSubmittedOrder();
            req.setAttribute("UnsubmittedOrdersTable", unSubmittedOrders);

            allOrders = ServiceOrder.getInstance().serviceFindAllOrder();
            req.setAttribute("AllOrders", allOrders);
        } catch (DBException ex) {
            address = UnChangeData.PAGE_ERROR;
            String errorMessage = new ServiceCalculation().findFromResourceBundle(session, ex.getMessage());
            req.getSession().setAttribute("errorSessionMessage", errorMessage);
            req.getSession().setAttribute("errorSessionCause", ex.getStackTrace());
        } finally {
            log.trace("Result of find users --> " + users);
            log.trace("Result of find foreman --> " + foremen);
            log.trace("Result of find un submitted orders by all users --> " + unSubmittedOrders);
            log.trace("Result of find all orders --> " + allOrders);
        }

        log.debug("ManagerCommand Finished, now go to address --> " + address);
        //System.out.println("UserCommand " + address);
        return address;
    }
}
