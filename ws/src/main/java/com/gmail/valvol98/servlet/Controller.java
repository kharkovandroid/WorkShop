package com.gmail.valvol98.servlet;

import com.gmail.valvol98.command.Command;
import com.gmail.valvol98.command.CommandContainer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;

import com.gmail.valvol98.data.UnChangeData;
import com.gmail.valvol98.db.DBException;
import org.apache.log4j.Logger;

/**
 * Main servlet controller.
 *
 * @author V.Volovshchykov
 *
 */
@WebServlet ("/controller")
public class Controller  extends HttpServlet {

    @Serial
    private static final long serialVersionUID = -5510539940723847971L;

    private static final Logger log = Logger.getLogger(Controller.class);

    /**
     * Method doGet of this controller.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String address = process(req, resp);
        RequestDispatcher disp = req.getRequestDispatcher(address);
        disp.forward(req, resp);
    }

    /**
     * Method doGet of this controller.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String address = process(req, resp);
        resp.sendRedirect(address);
    }

    /**
     * Method process of this controller.
     */
    private String process(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        System.out.println("Controller Started");
        log.debug("Controller Starts");

        String commandName = req.getParameter("command");
        log.trace("Request parameter: command --> " + commandName);
        System.out.println("Command " + commandName);

        Command command = CommandContainer.getCommand(commandName);
        log.trace("Obtained command --> " + command);
        System.out.println("Command " + command);

        String address = command.execute(req, resp);
        log.trace("Forward address --> " + address);
        System.out.println("Address " + address);

        log.debug("Controller finished, now go to address --> " + address);
        return address;
    }
}
