package command;

import com.gmail.valvol98.command.AddOrderCommand;
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

public class AddOrderCommandTest {

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
    public void testExecuteAddOrderCommandCommandUserStartPageUnsuccessful() throws ServletException, IOException, SQLException, DBException {
        HttpServletRequest req = mock(HttpServletRequest.class);

        HttpSession session = mock(HttpSession.class);

        when(req.getSession())
                .thenReturn(session);

        User user = new User(3, "pollogin", "polpass", "Польских Игорь Викторович", 3, "мастер");
        when(session.getAttribute("loggedUser"))
                .thenReturn(user);

        String description = null;
        when(req.getParameter("description"))
                .thenReturn(description);

        AddOrderCommand command = new AddOrderCommand();
        String address = command.execute(req, null);
        assertEquals(UnChangeData.COMMAND_USER_START_PAGE, address);
    }

    @Test
    public void testExecuteAddOrderCommandCommandUserStartPageSuccessful() throws ServletException, IOException, SQLException, DBException {
        HttpServletRequest req = mock(HttpServletRequest.class);

        HttpSession session = mock(HttpSession.class);

        when(req.getSession())
                .thenReturn(session);

        User user = new User(3, "pollogin", "polpass", "Польских Игорь Викторович", 3, "мастер");
        when(session.getAttribute("loggedUser"))
                .thenReturn(user);

        String description = "Ремонт настольной лампы";
        when(req.getParameter("description"))
                .thenReturn(description);

        int userId = user.getId();
        int successfulValue = 1;

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeUpdate())
                .thenReturn(successfulValue);
        doNothing().when(pstmt).setInt(1, userId);
        doNothing().when(pstmt).setString(2, description);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlInsertOrderByUser()))
                .thenReturn(pstmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        AddOrderCommand command = new AddOrderCommand();
        String address = command.execute(req, null);

        assertEquals(UnChangeData.COMMAND_USER_START_PAGE, address);
    }

    @Test
    public void testExecuteAddOrderCommandException() throws ServletException, IOException, SQLException, DBException {
        HttpServletRequest req = mock(HttpServletRequest.class);

        HttpSession session = mock(HttpSession.class);

        when(req.getSession())
                .thenReturn(session);

        User user = new User(3, "pollogin", "polpass", "Польских Игорь Викторович", 3, "мастер");
        when(session.getAttribute("loggedUser"))
                .thenReturn(user);

        String description = "Ремонт настольной лампы";
        when(req.getParameter("description"))
                .thenReturn(description);

        int userId = user.getId();
        int successfulValue = 1;

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeUpdate())
                .thenReturn(successfulValue);
        doNothing().when(pstmt).setInt(1, userId);
        doNothing().when(pstmt).setString(2, description);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlInsertOrderByUser()))
                .thenThrow(SQLException.class);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        AddOrderCommand command = new AddOrderCommand();
        String address = command.execute(req, null);
        assertEquals(UnChangeData.PAGE_ERROR, address);
    }
}
