package com.gmail.valvol98.service;

import com.gmail.valvol98.db.DBException;
import com.gmail.valvol98.db.DBManager;
import com.gmail.valvol98.db.entity.Account;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Helper type to make a calculation.
 *
 * @author V.Volovshchykov
 *
 */
public class ServiceCalculation implements Serializable {

    private static final long serialVersionUID = 356482363574902594L;

    /**
     * Method that provide finding general active money on a user's account.
     * @param userId user's code
     * @return user's general active money
     */
    public double findGeneralActiveMoneyOnAccount(int userId) throws DBException {
        double result = 0;

        double sumAccount = DBManager.getInstance().findSumAccountUser(userId);

        double sumRefuse = DBManager.getInstance().findSumRefuseUser(userId);

        double sumReservePaidRefuse = DBManager.getInstance().findSumReservePaidRefuseUser(userId);

        double sumReservePaid = sumReservePaidRefuse - sumRefuse;

        double sumAccountMinusSumReservePaid = sumAccount - sumReservePaid;

        result = sumAccountMinusSumReservePaid;

        return result;
    }

    /**
     * Method that provide finding data from resource file.
     * @param session current session
     * @param element element data from resource file
     * @return text data from resource file
     */
    public String findFromResourceBundle(HttpSession session, String element) {
        String locate = (String) session.getAttribute("language");
        ResourceBundle resourceBundle;
        if (locate == null) {
            resourceBundle = ResourceBundle.getBundle("textdata");
        } else {
            resourceBundle = ResourceBundle.getBundle("textdata", new Locale((String) session.getAttribute("language")));
        }
        return resourceBundle.getString(element);
    }

    /**
     * Method that provide finding general active money on a user's account.
     * @param accounts accounts's collection
     * @return general money on an account
     */
    public double sumAccount(List<Account> accounts) {
        double sum = 0;
        for(Account account : accounts) {
            sum += account.getPayment();
        }
        return sum;
    }
}
