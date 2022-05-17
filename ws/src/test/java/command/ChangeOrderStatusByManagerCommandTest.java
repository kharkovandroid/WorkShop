package command;

import com.gmail.valvol98.command.AddCommentCommand;
import com.gmail.valvol98.command.ChangeOrderStatusByManagerCommand;
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

public class ChangeOrderStatusByManagerCommandTest {

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
    public void testExecuteChangeOrderStatusByManagerCommandCommandManagerStartPage() throws ServletException, IOException, SQLException, DBException {
        HttpServletRequest req = mock(HttpServletRequest.class);

        int orderId = 1;
        int statusId = 1;
        int managerId = 1;

        when(req.getParameter("orderId"))
                .thenReturn(String.valueOf(orderId));

        when(req.getParameter("statusId"))
                .thenReturn(String.valueOf(statusId));

        when(req.getParameter("managerId"))
                .thenReturn(String.valueOf(managerId));

        when(req.getParameter("orderId"))
                .thenReturn(String.valueOf(1));

        HttpSession session = mock(HttpSession.class);
        when(req.getSession())
                .thenReturn(session);

        User user = new User(1, "mehlogin", "mehpass", "Мехайлов Алексей Петрович", 1, "менеджер");
        when(session.getAttribute("loggedUser"))
                .thenReturn(user);

        int successfulValue = 1;

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeUpdate())
                .thenReturn(successfulValue);
        doNothing().when(pstmt).setInt(1, orderId);
        doNothing().when(pstmt).setInt(2, statusId);
        doNothing().when(pstmt).setDouble(3, managerId);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlInsertStatusNext()))
                .thenReturn(pstmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        ChangeOrderStatusByManagerCommand command = new ChangeOrderStatusByManagerCommand();
        String address = command.execute(req, null);
        assertEquals(UnChangeData.COMMAND_MANAGER_START_PAGE, address);
    }

    @Test
    public void testExecuteChangeOrderStatusByManagerCommandException() throws ServletException, IOException, SQLException, DBException {
        HttpServletRequest req = mock(HttpServletRequest.class);

        int orderId = 1;
        int statusId = 1;
        int managerId = 1;

        when(req.getParameter("orderId"))
                .thenReturn(String.valueOf(orderId));

        when(req.getParameter("statusId"))
                .thenReturn(String.valueOf(statusId));

        when(req.getParameter("managerId"))
                .thenReturn(String.valueOf(managerId));

        when(req.getParameter("orderId"))
                .thenReturn(String.valueOf(1));

        HttpSession session = mock(HttpSession.class);
        when(req.getSession())
                .thenReturn(session);

        User user = new User(1, "mehlogin", "mehpass", "Мехайлов Алексей Петрович", 1, "менеджер");
        when(session.getAttribute("loggedUser"))
                .thenReturn(user);

        int successfulValue = 1;

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeUpdate())
                .thenReturn(successfulValue);
        doNothing().when(pstmt).setInt(1, orderId);
        doNothing().when(pstmt).setInt(2, statusId);
        doNothing().when(pstmt).setDouble(3, managerId);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlInsertStatusNext()))
                .thenReturn(pstmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenThrow(DBException.class);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        ChangeOrderStatusByManagerCommand command = new ChangeOrderStatusByManagerCommand();
        String address = command.execute(req, null);
        assertEquals(UnChangeData.PAGE_ERROR, address);
    }
}
