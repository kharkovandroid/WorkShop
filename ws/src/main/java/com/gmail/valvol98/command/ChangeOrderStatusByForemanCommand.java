package com.gmail.valvol98.command;

import com.gmail.valvol98.data.UnChangeData;
import com.gmail.valvol98.db.DBException;
import com.gmail.valvol98.db.DBManager;
import com.gmail.valvol98.db.entity.Order;
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
 * Command class to change order's status by foreman
 *
 * @author V.Volovshchykov
 *
 */
public class ChangeOrderStatusByForemanCommand implements Command {

    private static final Logger log = Logger.getLogger(ChangeOrderStatusByForemanCommand.class);

    /**
     * Execute method to command pattern that provide changing order's status by foreman
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        log.debug("ChangeOrderStatusByForemanCommand Starts");

        int orderId = Integer.parseInt(req.getParameter("orderId"));
        String type = req.getParameter("type");

        HttpSession session = req.getSession();
        User user = (User)session.getAttribute("loggedUser");

        log.trace("Change order status by foreman for Order Id --> " + orderId);
        log.trace("Change order status by foreman for type --> " + type);
        log.trace("Change order status by foreman --> " + user.getId());

        String address = UnChangeData.COMMAND_FOREMAN_START_PAGE;
        boolean insertResult = false;
        try {
            insertResult = DBManager.getInstance().insertOrderStatusByOrderAndType(orderId, type, user.getId());
        } catch (DBException ex) {
            address = UnChangeData.PAGE_ERROR;
            String errorMessage = new ServiceCalculation().findFromResourceBundle(session, ex.getMessage());
            req.getSession().setAttribute("errorSessionMessage", errorMessage);
            req.getSession().setAttribute("errorSessionCause", ex.getStackTrace());
        } finally {
            log.trace("Result of Change Order's Status by Foreman --> " + insertResult);
        }

        log.debug("ChangeOrderStatusByForemanCommand Finished, now go to address --> " + address);
        System.out.println("--------------------ForemanUpdateStatusOrder------------------");
        System.out.println(address);
        return address;
    }
}
