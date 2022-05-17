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
 * Command class to adding cost to order
 *
 * @author V.Volovshchykov
 *
 */
public class AddCostUnSubmittedOrderCommand implements Command {

    private static final Logger log = Logger.getLogger(AddCostUnSubmittedOrderCommand.class);

    /**
     * Execute method to command pattern that provide adding cost to order
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        log.debug("AddCostUnSubmittedOrderCommand Starts");

        HttpSession session = req.getSession();
        User manager = (User)session.getAttribute("loggedUser");
        int managerId = manager.getId();

        int idUnSubmittedOrder = Integer.valueOf(req.getParameter("idUnSubmittedOrder"));
        int idUser = Integer.valueOf(req.getParameter("idUser"));

        boolean updateCost = false;
        String address = UnChangeData.COMMAND_MANAGER_START_PAGE;
        try {
            double cost = Double.valueOf(req.getParameter("cost"));

            log.trace("Unsubmitted order Id --> " + idUnSubmittedOrder);
            log.trace("Unsubmitted order cost --> " + cost);

            double generalActiveMoneyOnAccount = new ServiceCalculation().findGeneralActiveMoneyOnAccount(idUser);

            if (generalActiveMoneyOnAccount >= cost && cost > 0) {
                session.setAttribute("idUnSubmittedOrder", "-1");
                updateCost = DBManager.getInstance().updateCostByOrderId(managerId, idUnSubmittedOrder, cost);
            } else {
                session.setAttribute("idUnSubmittedOrder", idUnSubmittedOrder + "");
                log.trace("There is not enough money on account. There are " + generalActiveMoneyOnAccount + " but you need " + cost);
            }
        } catch (NumberFormatException ex) {
            String errorMessage = "Стоимость не имеет числового формата";
            session.setAttribute("errorSessionMessage", errorMessage);
            session.setAttribute("errorSessionCause", ex.getStackTrace());
            return UnChangeData.PAGE_ERROR;
        } catch (DBException ex) {
            String errorMessage = new ServiceCalculation().findFromResourceBundle(session, ex.getMessage());
            session.setAttribute("errorSessionMessage", errorMessage);
            session.setAttribute("errorSessionCause", ex.getStackTrace());
            return UnChangeData.PAGE_ERROR;
        } finally {
            log.trace("Result of update cost By Order --> " + updateCost);
        }
        log.debug("AddCostUnSubmittedOrderCommand Finished, now go to address --> " + address);
        return address;
    }
}
