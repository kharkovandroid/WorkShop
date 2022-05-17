package com.gmail.valvol98.command;

import com.gmail.valvol98.data.UnChangeData;
import com.gmail.valvol98.db.DBException;
import com.gmail.valvol98.db.DBManager;
import com.gmail.valvol98.db.entity.Account;
import com.gmail.valvol98.db.entity.Order;
import com.gmail.valvol98.db.entity.User;
import com.gmail.valvol98.service.ServiceCalculation;
import com.gmail.valvol98.servlet.Controller;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Command class to user interface
 *
 * @author V.Volovshchykov
 *
 */
public class UserCommand implements Command {

    private static final Logger log = Logger.getLogger(UserCommand.class);

    /**
     * Execute method to command pattern that provide user interface
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        log.debug("UserCommand Starts");

        HttpSession session = req.getSession();
        User user = (User)session.getAttribute("loggedUser");
        int userId = user.getId();
        log.trace("User Id --> " + userId);

        String address = UnChangeData.PAGE_USER;
        session.setAttribute("pageName", UnChangeData.COMMAND_USER_START_PAGE);
        List<Account> accounts = null;
        List<Order> orders = null;
        try {
            int rows = DBManager.getInstance().findRowsAccountByUser(userId);
            int wholeNumber = rows / UnChangeData.NUMBER_ACCOUNTS_PER_PAGE;
            int remainder = rows % UnChangeData.NUMBER_ACCOUNTS_PER_PAGE;
            if (remainder != 0) {
                wholeNumber++;
            }
            List<Integer> rowsList = new ArrayList<>();
            for (int i = 0; i < wholeNumber; i++) {
                rowsList.add(i + 1);
            }
            req.setAttribute("rowList", rowsList);

            int rowSinceFind = UnChangeData.ROW_SINCE_FIND_LIMITED_ACCOUNTS_COLLECTION;
            accounts = DBManager.getInstance().findAccountByUser(userId, rowSinceFind);
            req.setAttribute("AcoountsTable", accounts);

            double sumAccount = DBManager.getInstance().findSumAccountUser(userId);
            req.setAttribute("SumAccount", sumAccount);

            double sumPaid = DBManager.getInstance().findSumPaidUser(userId);
            //req.setAttribute("SumPaid", sumPaid);
            double sumRefuse = DBManager.getInstance().findSumRefuseUser(userId);
            //req.setAttribute("SumRefuse", sumRefuse);
            double sumReservePaidRefuse = DBManager.getInstance().findSumReservePaidRefuseUser(userId);
            //req.setAttribute("SumReservePaidRefuse", sumReservePaidRefuse);
            double sumReservePaid = sumReservePaidRefuse - sumRefuse;
            req.setAttribute("SumReservePaid", sumReservePaid);
            req.setAttribute("SumAccountMinusSumReservePaid", new ServiceCalculation().findGeneralActiveMoneyOnAccount(userId));

            orders = DBManager.getInstance().findOrderUser(userId);
            req.setAttribute("OrdersTable", orders);
        } catch (DBException ex) {
            address = UnChangeData.PAGE_ERROR;
            String errorMessage = new ServiceCalculation().findFromResourceBundle(session, ex.getMessage());
            req.getSession().setAttribute("errorSessionMessage", errorMessage);
            req.getSession().setAttribute("errorSessionCause", ex.getStackTrace());
        } finally {
            log.trace("Result of find account by user --> " + accounts);
            log.trace("Result of find order by user --> " + orders);
        }

        log.debug("UserCommand Finished, now go to address --> " + address);
        //System.out.println("UserCommand " + address);
        return address;
    }
}
