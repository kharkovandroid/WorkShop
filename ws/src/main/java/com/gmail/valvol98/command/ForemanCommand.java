package com.gmail.valvol98.command;

import com.gmail.valvol98.data.UnChangeData;
import com.gmail.valvol98.db.DBException;
import com.gmail.valvol98.db.DBManager;
import com.gmail.valvol98.db.entity.Account;
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
 * Command class to foreman interface
 *
 * @author V.Volovshchykov
 *
 */
public class ForemanCommand implements Command {

    private static final Logger log = Logger.getLogger(ForemanCommand.class);

    /**
     * Execute method to command pattern that provide foreman interface
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        log.debug("ForemanCommand Starts");

        HttpSession session = req.getSession();
        User user = (User)session.getAttribute("loggedUser");
        int userId = user.getId();

        log.trace("User Id --> " + userId);

        String address = UnChangeData.PAGE_FOREMAN;
        session.setAttribute("pageName", UnChangeData.COMMAND_FOREMAN_START_PAGE);
        List<Order> orders = null;
        try {
            orders = DBManager.getInstance().findOrderByForeman(userId);
        } catch (DBException ex) {
            address = UnChangeData.PAGE_ERROR;
            String errorMessage = new ServiceCalculation().findFromResourceBundle(session, ex.getMessage());
            req.getSession().setAttribute("errorSessionMessage", errorMessage);
            req.getSession().setAttribute("errorSessionCause", ex.getStackTrace());
        } finally {
            log.trace("Result of find orders by foreman --> " + orders);
            req.setAttribute("OrdersTable", orders);
        }

        log.debug("ForemanCommand Finished, now go to address --> " + address);
        //System.out.println("UserCommand " + address);
        return address;
    }
}
