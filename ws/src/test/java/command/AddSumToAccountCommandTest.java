package command;

import com.gmail.valvol98.command.AddSumToAccountCommand;
import com.gmail.valvol98.data.UnChangeData;
import com.gmail.valvol98.db.DBException;
import com.gmail.valvol98.db.DBManager;
import com.gmail.valvol98.db.DBUtils;
import com.gmail.valvol98.db.entity.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class AddSumToAccountCommandTest {


    static MockedStatic<DBUtils> mocked;

    @Before
    public void init() {
        mocked = mockStatic(DBUtils.class);
    }

    @After
    public void close() {
        mocked.close();
    }

    @Test
    public void testExecuteAddSumToAccountCommandNumberFormatException() throws ServletException, IOException, SQLException, DBException {
        HttpServletRequest req = mock(HttpServletRequest.class);

        HttpSession session = mock(HttpSession.class);
        when(req.getSession())
                .thenReturn(session);

        User manager = new User(1, "mehlogin", "mehpass", "Мехайлов Алексей Петрович", 1, "менеджер");
        when(session.getAttribute("loggedUser"))
                .thenReturn(manager);

        int managerId = manager.getId();

        int userId = 3;
        when(req.getParameter("checkedUser"))
                .thenReturn(String.valueOf(userId));

        when(req.getParameter("sumToAccount"))
                .thenThrow(NumberFormatException.class);

        AddSumToAccountCommand command = new AddSumToAccountCommand();
        String address = command.execute(req, null);
        assertEquals(UnChangeData.PAGE_ERROR, address);
    }

    @Test
    public void testExecuteAddSumToAccountCommandCommandManagerStartPage() throws ServletException, IOException, SQLException, DBException {
        HttpServletRequest req = mock(HttpServletRequest.class);

        HttpSession session = mock(HttpSession.class);
        when(req.getSession())
                .thenReturn(session);

        User manager = new User(1, "mehlogin", "mehpass", "Мехайлов Алексей Петрович", 1, "менеджер");
        when(session.getAttribute("loggedUser"))
                .thenReturn(manager);

        int managerId = manager.getId();

        int userId = 3;
        when(req.getParameter("checkedUser"))
                .thenReturn(String.valueOf(userId));

        double sumToAccount = 30.5;
        when(req.getParameter("sumToAccount"))
                .thenReturn(String.valueOf(sumToAccount));

        int successfulValue = 1;

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeUpdate())
                .thenReturn(successfulValue);
        doNothing().when(pstmt).setInt(1, userId);
        doNothing().when(pstmt).setInt(2, managerId);
        doNothing().when(pstmt).setDouble(3, sumToAccount);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlInsertSumToAccountByUserManager()))
                .thenReturn(pstmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        AddSumToAccountCommand command = new AddSumToAccountCommand();
        String address = command.execute(req, null);
        assertEquals(UnChangeData.COMMAND_MANAGER_START_PAGE, address);
    }

    @Test
    public void testExecuteAddSumToAccountCommandPageError() throws ServletException, IOException, SQLException, DBException {
        HttpServletRequest req = mock(HttpServletRequest.class);

        HttpSession session = mock(HttpSession.class);
        when(req.getSession())
                .thenReturn(session);

        User manager = new User(1, "mehlogin", "mehpass", "Мехайлов Алексей Петрович", 1, "менеджер");
        when(session.getAttribute("loggedUser"))
                .thenReturn(manager);

        int managerId = manager.getId();

        int userId = 3;
        when(req.getParameter("checkedUser"))
                .thenReturn(String.valueOf(userId));

        double sumToAccount = 30.5;
        when(req.getParameter("sumToAccount"))
                .thenReturn(String.valueOf(sumToAccount));

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeUpdate())
                .thenThrow(SQLException.class);
        doNothing().when(pstmt).setInt(1, userId);
        doNothing().when(pstmt).setInt(2, managerId);
        doNothing().when(pstmt).setDouble(3, sumToAccount);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlInsertSumToAccountByUserManager()))
                .thenReturn(pstmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        AddSumToAccountCommand command = new AddSumToAccountCommand();
        String address = command.execute(req, null);
        assertEquals(UnChangeData.PAGE_ERROR, address);
    }
}
