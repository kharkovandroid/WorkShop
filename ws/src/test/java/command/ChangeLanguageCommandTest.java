package command;

import com.gmail.valvol98.command.ChangeLanguageCommand;
import com.gmail.valvol98.data.UnChangeData;
import com.gmail.valvol98.db.DBException;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChangeLanguageCommandTest {

    @Test
    public void testExecuteChangeLanguageCommandSessionAttribiteIsNull() throws ServletException, IOException, SQLException, DBException {
        HttpServletRequest req = mock(HttpServletRequest.class);

        String language = "en";
        when(req.getParameter("lang"))
                .thenReturn(language);

        HttpSession session = mock(HttpSession.class);
        when(req.getSession())
                .thenReturn(session);

        when(session.getAttribute("pageName"))
                .thenReturn(null);

        ChangeLanguageCommand command = new ChangeLanguageCommand();
        String address = command.execute(req, null);
        assertEquals(UnChangeData.PAGE_INDEX, address);
    }

    @Test
    public void testExecuteChangeLanguageCommandSessionAttributeIsNotNull() throws ServletException, IOException, SQLException, DBException {
        HttpServletRequest req = mock(HttpServletRequest.class);

        String language = "en";
        when(req.getParameter("lang"))
                .thenReturn(language);

        HttpSession session = mock(HttpSession.class);
        when(req.getSession())
                .thenReturn(session);

        String pageName = "index.jsp";
        when(session.getAttribute("pageName"))
                .thenReturn(pageName);

        ChangeLanguageCommand command = new ChangeLanguageCommand();
        String address = command.execute(req, null);
        assertEquals(UnChangeData.PAGE_INDEX, address);
    }
}
