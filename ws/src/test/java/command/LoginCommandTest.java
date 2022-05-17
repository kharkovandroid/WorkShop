package command;

import com.gmail.valvol98.command.LoginCommand;
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

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class LoginCommandTest {

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
    public void testExecuteLoginCommandPageIndexLoginNull() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);

        when(req.getParameter("login"))
                .thenReturn(null);

        when(req.getParameter("password"))
                .thenReturn(null);

        HttpSession session = mock(HttpSession.class);

        when(req.getSession())
                .thenReturn(session);

        LoginCommand command = new LoginCommand();
        String address = command.execute(req, null);

        assertEquals(UnChangeData.PAGE_INDEX, address);
    }

    @Test
    public void testExecuteLoginCommandPageIndexLoginWrong() throws ServletException, IOException, SQLException, DBException {
        HttpServletRequest req = mock(HttpServletRequest.class);

        String login = "wrongLogin";
        when(req.getParameter("login"))
                .thenReturn(login);

        when(req.getParameter("password"))
                .thenReturn("wrongPassword");

        HttpSession session = mock(HttpSession.class);

        when(req.getSession())
                .thenReturn(session);

        ResultSet rs = mock(ResultSet.class);

        when(rs.next())
                .thenReturn(false);

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeQuery())
                .thenReturn(rs);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlFindUserByLogin()))
                .thenReturn(pstmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        DBManager dbManager = DBManager.getInstance();

        User user = dbManager.findUser(login);
        verify(pstmt).setString(1, login);

        LoginCommand command = new LoginCommand();
        String address = command.execute(req, null);
        assertEquals(UnChangeData.PAGE_INDEX, address);
    }

    @Test
    public void testExecuteLoginCommandManagerStartPage() throws ServletException, IOException, SQLException, DBException {
        HttpServletRequest req = mock(HttpServletRequest.class);

        String login = "mehlogin";
        String password = "mehpass";
        when(req.getParameter("login"))
                .thenReturn(login);

        when(req.getParameter("password"))
                .thenReturn(password);

        HttpSession session = mock(HttpSession.class);

        when(req.getSession())
                .thenReturn(session);

        ResultSet rs = mock(ResultSet.class);

        when(rs.next())
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getInt("user.id"))
                .thenReturn(1);

        when(rs.getString("user.login"))
                .thenReturn(login);

        when(rs.getString("user.password"))
                .thenReturn(password);

        when(rs.getString("user.name"))
                .thenReturn("Мехайлов Алексей Петрович");

        when(rs.getInt("user.role_id"))
                .thenReturn(1);

        when(rs.getString("role.role"))
                .thenReturn(UnChangeData.ROLE_MANAGER);

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeQuery())
                .thenReturn(rs);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlFindUserByLogin()))
                .thenReturn(pstmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        LoginCommand command = new LoginCommand();
        String address = command.execute(req, null);
        assertEquals(UnChangeData.COMMAND_MANAGER_START_PAGE, address);
    }

    @Test
    public void testExecuteLoginCommandUserStartPage() throws ServletException, IOException, SQLException, DBException {
        HttpServletRequest req = mock(HttpServletRequest.class);

        String login = "pollogin";
        String password = "polpass";
        when(req.getParameter("login"))
                .thenReturn(login);

        when(req.getParameter("password"))
                .thenReturn(password);

        HttpSession session = mock(HttpSession.class);

        when(req.getSession())
                .thenReturn(session);

        ResultSet rs = mock(ResultSet.class);

        when(rs.next())
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getInt("user.id"))
                .thenReturn(3);

        when(rs.getString("user.login"))
                .thenReturn(login);

        when(rs.getString("user.password"))
                .thenReturn(password);

        when(rs.getString("user.name"))
                .thenReturn("Польских Игорь Викторович");

        when(rs.getInt("user.role_id"))
                .thenReturn(2);

        when(rs.getString("role.role"))
                .thenReturn(UnChangeData.ROLE_USER);

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeQuery())
                .thenReturn(rs);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlFindUserByLogin()))
                .thenReturn(pstmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        LoginCommand command = new LoginCommand();
        String address = command.execute(req, null);
        assertEquals(UnChangeData.COMMAND_USER_START_PAGE, address);
    }

    @Test
    public void testExecuteLoginCommandForemanStartPage() throws ServletException, IOException, SQLException, DBException {
        HttpServletRequest req = mock(HttpServletRequest.class);

        String login = "mallogin";
        String password = "malpass";
        when(req.getParameter("login"))
                .thenReturn(login);

        when(req.getParameter("password"))
                .thenReturn(password);

        HttpSession session = mock(HttpSession.class);

        when(req.getSession())
                .thenReturn(session);

        ResultSet rs = mock(ResultSet.class);

        when(rs.next())
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getInt("user.id"))
                .thenReturn(5);

        when(rs.getString("user.login"))
                .thenReturn(login);

        when(rs.getString("user.password"))
                .thenReturn(password);

        when(rs.getString("user.name"))
                .thenReturn("Малых Виктор Сергеевич");

        when(rs.getInt("user.role_id"))
                .thenReturn(3);

        when(rs.getString("role.role"))
                .thenReturn(UnChangeData.ROLE_FOREMAN);

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeQuery())
                .thenReturn(rs);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlFindUserByLogin()))
                .thenReturn(pstmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        LoginCommand command = new LoginCommand();
        String address = command.execute(req, null);
        assertEquals(UnChangeData.COMMAND_FOREMAN_START_PAGE, address);
    }

    @Test
    public void testExecuteLoginCommandException() throws ServletException, IOException, SQLException, DBException {
        HttpServletRequest req = mock(HttpServletRequest.class);

        String login = "mallogin";
        String password = "malpass";
        when(req.getParameter("login"))
                .thenReturn(login);

        when(req.getParameter("password"))
                .thenReturn(password);

        HttpSession session = mock(HttpSession.class);

        when(req.getSession())
                .thenReturn(session);

        ResultSet rs = mock(ResultSet.class);

        when(rs.next())
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getInt("user.id"))
                .thenReturn(5);

        when(rs.getString("user.login"))
                .thenReturn(login);

        when(rs.getString("user.password"))
                .thenReturn(password);

        when(rs.getString("user.name"))
                .thenReturn("Малых Виктор Сергеевич");

        when(rs.getInt("user.role_id"))
                .thenReturn(3);

        when(rs.getString("role.role"))
                .thenReturn(UnChangeData.ROLE_FOREMAN);

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeQuery())
                .thenReturn(rs);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlFindUserByLogin()))
                .thenThrow(SQLException.class);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        LoginCommand command = new LoginCommand();
        String address = command.execute(req, null);
        assertEquals(UnChangeData.PAGE_ERROR, address);
    }
}
