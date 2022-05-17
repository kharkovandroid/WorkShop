package com.gmail.valvol98.command;

import com.gmail.valvol98.data.UnChangeData;
import com.gmail.valvol98.db.DBException;
import com.gmail.valvol98.db.DBManager;
import com.gmail.valvol98.db.entity.User;
import com.gmail.valvol98.service.ServiceCalculation;
import com.gmail.valvol98.servlet.Controller;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Command class to acceess to the heart of the application
 *
 * @author V.Volovshchykov
 *
 */
public class LoginCommand implements Command {

    private static final Logger log = Logger.getLogger(Controller.class);

    /**
     * Execute method to command pattern that provide access to the heart of the application
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        log.debug("LoginCommand Starts");

        String login = req.getParameter("login");
        log.trace("Login --> " + login);
        String password = req.getParameter("password");
        log.trace("Password --> " + password);
        HttpSession session = req.getSession();
        String errorMessage = null;
        String address = UnChangeData.PAGE_INDEX;
        session.setAttribute("isUserExists", "-1");
        session.setAttribute("pageName", address);
        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            errorMessage = "Login/password cannot be empty";
            session.setAttribute("isUserExists", "no");
            log.error("errorMessage --> " + errorMessage);
            return address;
        }
        User user = null;
        try {
            user = DBManager.getInstance().findUser(login);
            log.trace("User --> " + user);
            if (user == null || !DigestUtils.md5Hex(password).equals(user.getPassword())) {
                errorMessage = "Cannot find user with such login/password";
                session.setAttribute("isUserExists", "no");
                log.error("errorMessage --> " + errorMessage);
                return address;
            } else {
                session.setAttribute("loggedUser", user);
                if (user.getRole().equals(UnChangeData.ROLE_MANAGER)) {
                    address = UnChangeData.COMMAND_MANAGER_START_PAGE;
                }
                if (user.getRole().equals(UnChangeData.ROLE_USER)) {
                    address = UnChangeData.COMMAND_USER_START_PAGE;
                }
                if (user.getRole().equals(UnChangeData.ROLE_FOREMAN)) {
                    address = UnChangeData.COMMAND_FOREMAN_START_PAGE;
                }
            }
        } catch (DBException ex) {
            address = UnChangeData.PAGE_ERROR;
            errorMessage = new ServiceCalculation().findFromResourceBundle(session, ex.getMessage());
            req.getSession().setAttribute("errorSessionMessage", errorMessage);
            req.getSession().setAttribute("errorSessionCause", ex.getStackTrace());
        } finally {
            log.trace("Set the session attribute: user --> " + user);
        }

        log.debug("LoginCommand Finished, now go to address --> " + address);

        return address;
    }
}
