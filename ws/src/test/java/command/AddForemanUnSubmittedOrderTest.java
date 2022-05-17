package command;

import com.gmail.valvol98.command.AddForemanUnSubmittedOrder;
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
import static org.mockito.Mockito.when;

public class AddForemanUnSubmittedOrderTest {


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
    public void testExecuteAddForemanUnSubmittedOrderCommandManagerStartPage() throws ServletException, IOException, SQLException, DBException {
        HttpServletRequest req = mock(HttpServletRequest.class);

        int foremanId = 3;
        String listIdUnSubmittedOrder = "1_9_";
        int successfulValue = 1;

        when(req.getParameter("checkedForeman"))
                .thenReturn(String.valueOf(foremanId));

        when(req.getParameter("listIdUnSubmittedOrder"))
                .thenReturn(listIdUnSubmittedOrder);

        PreparedStatement pstmt = mock(PreparedStatement.class);
        Connection con = mock(Connection.class);
        String[] idUnSubmittedOrder = listIdUnSubmittedOrder.split("_");

        for (String str : idUnSubmittedOrder) {
            when(pstmt.executeUpdate())
                    .thenReturn(successfulValue);
            doNothing().when(pstmt).setInt(1, foremanId);
            doNothing().when(pstmt).setInt(2, Integer.parseInt(str));
            when(con.prepareStatement(DBManager.getSqlUpdateOrderForemanIdByOrderId()))
                    .thenReturn(pstmt);
        }

        doNothing().when(con).commit();

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        AddForemanUnSubmittedOrder command = new AddForemanUnSubmittedOrder();
        String address = command.execute(req, null);
        assertEquals(UnChangeData.COMMAND_MANAGER_START_PAGE, address);
    }

    @Test
    public void testExecuteAddForemanUnSubmittedOrderException() throws ServletException, IOException, SQLException, DBException {
        HttpServletRequest req = mock(HttpServletRequest.class);

        HttpSession session = mock(HttpSession.class);

        when(req.getSession())
                .thenReturn(session);

        int foremanId = 3;
        String listIdUnSubmittedOrder = "1_9_";
        int successfulValue = 1;

        when(req.getParameter("checkedForeman"))
                .thenReturn(String.valueOf(foremanId));

        when(req.getParameter("listIdUnSubmittedOrder"))
                .thenReturn(listIdUnSubmittedOrder);

        PreparedStatement pstmt = mock(PreparedStatement.class);
        Connection con = mock(Connection.class);
        String[] idUnSubmittedOrder = listIdUnSubmittedOrder.split("_");

        when(pstmt.executeUpdate())
                .thenReturn(successfulValue);
        doNothing().when(pstmt).setInt(1, foremanId);
        doNothing().when(pstmt).setInt(2, Integer.parseInt(idUnSubmittedOrder[0]));
        when(con.prepareStatement(DBManager.getSqlUpdateOrderForemanIdByOrderId()))
                .thenThrow(SQLException.class);

        doNothing().when(con).commit();

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        AddForemanUnSubmittedOrder command = new AddForemanUnSubmittedOrder();
        String address = command.execute(req, null);

        assertEquals(UnChangeData.PAGE_ERROR, address);
    }
}
