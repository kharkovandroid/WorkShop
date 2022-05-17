package command;

import com.gmail.valvol98.command.AddCommentCommand;
import com.gmail.valvol98.data.UnChangeData;
import com.gmail.valvol98.db.DBException;
import com.gmail.valvol98.db.DBManager;
import com.gmail.valvol98.db.DBUtils;
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

public class AddCommentCommandTest {

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
    public void testExecuteAddCommentCommandCommentNull() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);

        when(req.getParameter("orderId"))
                .thenReturn(String.valueOf(1));

        when(req.getParameter("comment"))
                .thenReturn(null);

        AddCommentCommand command = new AddCommentCommand();
        String address = command.execute(req, null);

        assertEquals(UnChangeData.COMMAND_USER_START_PAGE, address);
    }

    @Test
    public void testExecuteAddCommentCommandCommandUserStartPage() throws ServletException, IOException, SQLException, DBException {
        HttpServletRequest req = mock(HttpServletRequest.class);

        int orderId = 1;
        String comment = "Заказ на ремон выполнен отлично";
        int successfulValue = 1;

        when(req.getParameter("orderId"))
                .thenReturn(String.valueOf(1));

        when(req.getParameter("comment"))
                .thenReturn(comment);

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeUpdate())
                .thenReturn(successfulValue);
        doNothing().when(pstmt).setString(1, comment);
        doNothing().when(pstmt).setInt(2, orderId);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlUpdateCommentByOrder()))
                .thenReturn(pstmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        AddCommentCommand command = new AddCommentCommand();
        String address = command.execute(req, null);
        assertEquals(UnChangeData.COMMAND_USER_START_PAGE, address);
    }

    @Test
    public void testExecuteAddCommentCommandException() throws ServletException, IOException, SQLException, DBException {
        HttpServletRequest req = mock(HttpServletRequest.class);

        HttpSession session = mock(HttpSession.class);
        when(req.getSession())
                .thenReturn(session);

        int orderId = 1;
        String comment = "Заказ на ремон выполнен отлично";
        int successfulValue = 1;

        when(req.getParameter("orderId"))
                .thenReturn(String.valueOf(1));

        when(req.getParameter("comment"))
                .thenReturn(comment);

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeUpdate())
                .thenReturn(successfulValue);
        doNothing().when(pstmt).setString(1, comment);
        doNothing().when(pstmt).setInt(2, orderId);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlUpdateCommentByOrder()))
                .thenThrow(SQLException.class);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        AddCommentCommand command = new AddCommentCommand();
        String address = command.execute(req, null);
        assertEquals(UnChangeData.PAGE_ERROR, address);
    }

}
