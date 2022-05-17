package com.gmail.valvol98.listener;

import com.gmail.valvol98.db.DBException;
import com.gmail.valvol98.db.DBManager;
import com.gmail.valvol98.db.DBUtils;
import com.gmail.valvol98.service.ServiceCalculation;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Context listener.
 *
 * @author V.Volovshchykov
 *
 */public class ContextListener implements ServletContextListener {

    private static final Logger log = Logger.getLogger(ContextListener.class);

    public void contextDestroyed(ServletContextEvent event) {
        log.debug("Servlet context destruction starts");
        // do nothing
        log.debug("Servlet context destruction finished");
    }

    public void contextInitialized(ServletContextEvent event) {
        log.info("Project home dir " + event.getServletContext().getRealPath(""));

        ServletContext servletContext = event.getServletContext();
        try (Connection con = DBUtils.getInstance().getConnection()) {
        } catch (SQLException e) {
            log.fatal("Cannot initialize DB subsystem" + e.getMessage());
            String errorMessage = new ServiceCalculation().findFromResourceBundle((HttpSession) servletContext, "errorMessage.DbcsDoesNotInit");
            servletContext.setAttribute("errorSessionMessage", errorMessage);
            servletContext.setAttribute("errorSessionCause", e.getStackTrace());
            //throw new IllegalStateException("Cannot initialize DB subsystem", e);
        } catch (DBException e) {
            String errorMessage = new ServiceCalculation().findFromResourceBundle((HttpSession) servletContext, e.getMessage());
            servletContext.setAttribute("errorSessionMessage", errorMessage);
            servletContext.setAttribute("errorSessionCause", e.getStackTrace());
        }
        log.debug("Servlet context initialization starts");

        initCommandContainer();

        log.debug("Servlet context initialization finished");
    }

    /**
     * Initializes CommandContainer.
     */
    private void initCommandContainer() {
        log.debug("Command container initialization started");

        // initialize commands container
        // just load class to JVM
        try {
            Class.forName("com.gmail.valvol98.command.CommandContainer");
        } catch (ClassNotFoundException ex) {
            log.debug("Exception " + ex.getMessage());
            throw new RuntimeException(ex);
        }

        log.debug("Command container initialization finished");
    }

}