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
 * Command class to adding comment to order
 *
 * @author V.Volovshchykov
 *
 */
public class AddCommentCommand implements Command {

    private static final Logger log = Logger.getLogger(AddCommentCommand.class);

    /**
     * Execute method to command pattern that provide adding comment to order
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        log.debug("AddCommentCommand Starts");

        HttpSession session = req.getSession();
        int orderId  = Integer.parseInt(req.getParameter("orderId"));
        String comment  = req.getParameter("comment");

        log.trace("Order Id --> " + orderId);
        log.trace("Comment --> " + comment);

        String errorMessage = null;
        //String address = UnChangeData.PAGE_USER;
        String address = UnChangeData.COMMAND_USER_START_PAGE;
        if (comment == null || comment.isEmpty()) {
            errorMessage = "Comment cannot be empty";
            log.error("errorMessage --> " + errorMessage);
            return address;
        }
        boolean insertResult = false;
        try {
            insertResult = DBManager.getInstance().updateCommentByOrder(orderId, comment);
        } catch (DBException ex) {
            address = UnChangeData.PAGE_ERROR;
            errorMessage = new ServiceCalculation().findFromResourceBundle(session, ex.getMessage());
            req.getSession().setAttribute("errorSessionMessage", errorMessage);
            req.getSession().setAttribute("errorSessionCause", ex.getStackTrace());
        } finally {
            log.trace("Result of Insert Comment By Order --> " + insertResult);
        }
        log.debug("AddCommentCommand Finished, now go to address --> " + address);
        return address;
    }
}
