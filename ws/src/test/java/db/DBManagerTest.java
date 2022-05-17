package db;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.gmail.valvol98.data.UnChangeData;
import com.gmail.valvol98.db.DBException;
import com.gmail.valvol98.db.DBManager;
import com.gmail.valvol98.db.DBUtils;
import com.gmail.valvol98.db.entity.Account;
import com.gmail.valvol98.db.entity.Order;
import com.gmail.valvol98.db.entity.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

public class DBManagerTest {

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
    public void testFindUsers() throws Exception {
        List<User> expected = new ArrayList<>();
        expected.add(new User(1,"mehlogin","mehpass", "Мехайлов Алексей Петрович", 1, "менеджер"));
        expected.add(new User(2,"mevlogin","mevpass", "Мевеев Виктор Львович", 1, "менеджер"));
        expected.add(new User(3,"pollogin","polpass", "Польских Игорь Викторович", 2, "пользователь"));
        expected.add(new User(4,"potlogin","potpass", "Потаев Олег Анатольевич", 2, "пользователь"));
        expected.add(new User(5,"mallogin","malpass", "Малых Виктор Сергеевич", 3, "мастер"));
        expected.add(new User(6,"matlogin","matpass", "Матюшенко Сидор Петрович", 3, "мастер"));

        ResultSet rs = mock(ResultSet.class);

        when(rs.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getInt("user.id"))
                .thenReturn(1)
                .thenReturn(2)
                .thenReturn(3)
                .thenReturn(4)
                .thenReturn(5)
                .thenReturn(6);

        when(rs.getString("user.login"))
                .thenReturn("mehlogin")
                .thenReturn("mevlogin")
                .thenReturn("pollogin")
                .thenReturn("potlogin")
                .thenReturn("mallogin")
                .thenReturn("matlogin");

        when(rs.getString("user.password"))
                .thenReturn("mehpass")
                .thenReturn("mevpass")
                .thenReturn("polpass")
                .thenReturn("potpass")
                .thenReturn("malpass")
                .thenReturn("matpass");

        when(rs.getString("user.name"))
                .thenReturn("Мехайлов Алексей Петрович")
                .thenReturn("Мевеев Виктор Львович")
                .thenReturn("Польских Игорь Викторович")
                .thenReturn("Потаев Олег Анатольевич")
                .thenReturn("Малых Виктор Сергеевич")
                .thenReturn("Матюшенко Сидор Петрович");

        when(rs.getInt("user.role_id"))
                .thenReturn(1)
                .thenReturn(1)
                .thenReturn(2)
                .thenReturn(2)
                .thenReturn(3)
                .thenReturn(3);

        when(rs.getString("role.role"))
                .thenReturn("менеджер")
                .thenReturn("менеджер")
                .thenReturn("пользователь")
                .thenReturn("пользователь")
                .thenReturn("мастер")
                .thenReturn("мастер");

        Statement stmt = mock(Statement.class);
        when(stmt.executeQuery(DBManager.getSqlFindAllUsers()))
                .thenReturn(rs);

        Connection con = mock(Connection.class);
        when(con.createStatement())
                .thenReturn(stmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        DBManager dbManager = DBManager.getInstance();
        List<User> users = dbManager.findUsers();
        assertEquals(expected.toString(), users.toString());
    }

    @Test
    public void testFindUsersByLogin() throws Exception {
        String loginParam = "mehlogin";
        User expected = new User(1,loginParam,"mehpass", "Мехайлов Алексей Петрович", 1, "менеджер");

        ResultSet rs = mock(ResultSet.class);

        when(rs.next())
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getInt("user.id"))
                .thenReturn(1);

        when(rs.getString("user.login"))
                .thenReturn(loginParam);

        when(rs.getString("user.password"))
                .thenReturn("mehpass");

        when(rs.getString("user.name"))
                .thenReturn("Мехайлов Алексей Петрович");

        when(rs.getInt("user.role_id"))
                .thenReturn(1);

        when(rs.getString("role.role"))
                .thenReturn("менеджер");

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
        User user = dbManager.findUser(loginParam);

        verify(pstmt).setString(1, loginParam);

        assertEquals(expected.toString(), user.toString());
    }

    @Test
    public void testFindUnSubmittedOrder() throws Exception {
        List<User> expected = new ArrayList<>();

        ResultSet rs = mock(ResultSet.class);

        when(rs.next())
                .thenReturn(false);

        Statement stmt = mock(Statement.class);
        when(stmt.executeQuery(DBManager.getSqlFindUnsubmittedOrder()))
                .thenReturn(rs);

        Connection con = mock(Connection.class);
        when(con.createStatement())
                .thenReturn(stmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        DBManager dbManager = DBManager.getInstance();
        List<Order> orders = dbManager.findUnSubmittedOrder();
        assertEquals(expected.toString(), orders.toString());
    }

    @Test
    public void testFindUsersByRole() throws Exception {
        String roleParam = "unKnownRole";
        List<User> expected = new ArrayList<>();

        ResultSet rs = mock(ResultSet.class);

        when(rs.next())
                .thenReturn(false);

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeQuery())
                .thenReturn(rs);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlFindAllUserByRole()))
                .thenReturn(pstmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        DBManager dbManager = DBManager.getInstance();
        List<User> users = dbManager.findUsersByRole(roleParam);

        verify(pstmt).setString(1, roleParam);

        assertEquals(expected.toString(), users.toString());
    }

    @Test
    public void testFindStatus() throws Exception {
        Integer expected = 4;
        String typeParam = UnChangeData.ORDER_PAID;

        ResultSet rs = mock(ResultSet.class);

        when(rs.next())
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getInt("id"))
                .thenReturn(4);

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeQuery())
                .thenReturn(rs);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlFindStatusByType()))
                .thenReturn(pstmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        DBManager dbManager = DBManager.getInstance();
        Integer id = dbManager.findStatus(typeParam);

        verify(pstmt).setString(1, typeParam);

        assertEquals(expected.toString(), id.toString());
    }

    @Test
    public void testFindAccountByUser() throws Exception {
        int userIdParam = 3;
        int rowSinceFindParam = 1;
        List<Account> expected = new ArrayList<>();
        expected.add(new Account(1, 3, 1, 10.0, "2022-04-02 14:04:57", "Польских Игорь Викторович"));
        expected.add(new Account(2, 3, 1, 20.0, "2022-04-02 14:04:58", "Польских Игорь Викторович"));

        ResultSet rs = mock(ResultSet.class);

        when(rs.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);

       when(rs.getInt("account.id"))
                .thenReturn(1)
                .thenReturn(2);

        when(rs.getInt("account.user_id"))
                .thenReturn(3)
                .thenReturn(3);

        when(rs.getInt("account.manager_id"))
                .thenReturn(1)
                .thenReturn(1);

        when(rs.getDouble("account.payment"))
                .thenReturn(10.0)
                .thenReturn(20.0);

        when(rs.getString("account.datetime"))
                .thenReturn("2022-04-02 14:04:57")
                .thenReturn("2022-04-02 14:04:58");

        when(rs.getString("user.name"))
                .thenReturn("Польских Игорь Викторович")
                .thenReturn("Польских Игорь Викторович");

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeQuery())
                .thenReturn(rs);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlFindAccountByUser()))
                .thenReturn(pstmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        DBManager dbManager = DBManager.getInstance();
        List<Account> accounts = dbManager.findAccountByUser(userIdParam, rowSinceFindParam);

        verify(pstmt).setInt(1, userIdParam);
        verify(pstmt).setInt(2, rowSinceFindParam);
        verify(pstmt).setInt(3, UnChangeData.NUMBER_ACCOUNTS_PER_PAGE);

        assertEquals(expected.toString(), accounts.toString());
    }

    @Test
    public void testFindRowsAccountByUser() throws Exception {
        int expected = 11;
        int userIdParam = 3;

        ResultSet rs = mock(ResultSet.class);

        when(rs.next())
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getInt("accountRows"))
                .thenReturn(11);

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeQuery())
                .thenReturn(rs);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlFindRowsAccountByUser()))
                .thenReturn(pstmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        DBManager dbManager = DBManager.getInstance();
        int rows = dbManager.findRowsAccountByUser(userIdParam);

        verify(pstmt).setInt(1, userIdParam);

        assertEquals(expected, rows);
    }

    @Test
    public void testFindSumAccountUser() throws Exception {
        double expected = 660.0;
        int userIdParam = 3;

        ResultSet rs = mock(ResultSet.class);

        when(rs.next())
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getDouble("sumAccount"))
                .thenReturn(660.0);

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeQuery())
                .thenReturn(rs);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlFindSumAccountByUser()))
                .thenReturn(pstmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        DBManager dbManager = DBManager.getInstance();
        double sum = dbManager.findSumAccountUser(userIdParam);

        verify(pstmt).setInt(1, userIdParam);

        double epsilon = 0.1;
        assertEquals(true, Math.abs(expected - sum) < epsilon);
    }

    @Test
    public void testFindSumPaidUser() throws Exception {
        double expected = 0.0;
        int userIdParam = 3;

        ResultSet rs = mock(ResultSet.class);

        when(rs.next())
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getDouble("sumPaid"))
                .thenReturn(0.0);

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeQuery())
                .thenReturn(rs);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlFindSumPaidOrderByUser()))
                .thenReturn(pstmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        DBManager dbManager = DBManager.getInstance();
        double sum = dbManager.findSumPaidUser(userIdParam);

        verify(pstmt).setInt(1, userIdParam);

        double epsilon = 0.1;
        assertEquals(true, Math.abs(expected - sum) < epsilon);
    }

    @Test
    public void testFindSumRefuseUser() throws Exception {
        double expected = 35.0;
        int userIdParam = 3;

        ResultSet rs = mock(ResultSet.class);

        when(rs.next())
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getDouble("sumRefuse"))
                .thenReturn(35.0);

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeQuery())
                .thenReturn(rs);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlFindSumRefuseOrderByUser()))
                .thenReturn(pstmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        DBManager dbManager = DBManager.getInstance();
        double sum = dbManager.findSumRefuseUser(userIdParam);

        verify(pstmt).setInt(1, userIdParam);

        double epsilon = 0.1;
        assertEquals(true, Math.abs(expected - sum) < epsilon);
    }

    @Test
    public void testFindSumReservePaidRefuseUser() throws Exception {
        double expected = 130.0;
        int userIdParam = 3;

        ResultSet rs = mock(ResultSet.class);

        when(rs.next())
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getDouble("sumReservePaidRefuse"))
                .thenReturn(130.0);

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeQuery())
                .thenReturn(rs);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlFindSumReservePaidRefuseOrderByUser()))
                .thenReturn(pstmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        DBManager dbManager = DBManager.getInstance();
        double sum = dbManager.findSumReservePaidRefuseUser(userIdParam);

        verify(pstmt).setInt(1, userIdParam);

        double epsilon = 0.1;
        assertEquals(true, Math.abs(expected - sum) < epsilon);
    }

    @Test
    public void testInsertOrderByUser() throws Exception {
        int userIdParam = 3;
        String descriptionParam = "Ремонт форточки";
        int successfulValue = 1;
        boolean expected = true;

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeUpdate())
                .thenReturn(successfulValue);
        doNothing().when(pstmt).setInt(1, userIdParam);
        doNothing().when(pstmt).setString(2, descriptionParam);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlInsertOrderByUser()))
                .thenReturn(pstmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        DBManager dbManager = DBManager.getInstance();
        boolean isInsert = dbManager.insertOrderByUser(userIdParam, descriptionParam);

        verify(pstmt).setInt(1, userIdParam);
        verify(pstmt).setString(2, descriptionParam);

        assertEquals(expected, isInsert);
    }

    @Test(expected = DBException.class)
    public void testInsertOrderByUserException() throws DBException, SQLException {
        int userIdParam = 3;
        String descriptionParam = "1234567890123456789012345678901234567890";

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeUpdate())
                .thenThrow(SQLException.class);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlInsertOrderByUser()))
                .thenReturn(pstmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        DBManager dbManager = DBManager.getInstance();
        boolean isInsert = dbManager.insertOrderByUser(userIdParam, descriptionParam);

        verify(pstmt).setInt(1, userIdParam);
        verify(pstmt).setString(2, descriptionParam);
    }

    @Test
    public void testInsertSumToAccount() throws Exception {
        int managerIdParam = 1;
        int userIdParam = 3;
        double sumToAccountParam = 30;

        int successfulValue = 1;
        boolean expected = true;

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeUpdate())
                .thenReturn(successfulValue);
        doNothing().when(pstmt).setInt(1, userIdParam);
        doNothing().when(pstmt).setInt(2, managerIdParam);
        doNothing().when(pstmt).setDouble(3, sumToAccountParam);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlInsertSumToAccountByUserManager()))
                .thenReturn(pstmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        DBManager dbManager = DBManager.getInstance();
        boolean isInsert = dbManager.insertSumToAccount(managerIdParam, userIdParam, sumToAccountParam);

        verify(pstmt).setInt(1, userIdParam);
        verify(pstmt).setInt(2, managerIdParam);
        verify(pstmt).setDouble(3, sumToAccountParam);

        assertEquals(expected, isInsert);
    }

    @Test(expected = DBException.class)
    public void testInsertSumToAccountException() throws DBException, SQLException {
        int managerIdParam = 1;
        int userIdParam = 3;
        double sumToAccountParam = 30000;

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeUpdate())
                .thenThrow(SQLException.class);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlInsertSumToAccountByUserManager()))
                .thenReturn(pstmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        DBManager dbManager = DBManager.getInstance();
        boolean isInsert = dbManager.insertSumToAccount(managerIdParam, userIdParam, sumToAccountParam);

        verify(pstmt).setInt(1, userIdParam);
        verify(pstmt).setInt(2, managerIdParam);
        verify(pstmt).setDouble(3, sumToAccountParam);
    }

    @Test
    public void testInsertNextStatusToOrderStatusByOrderStatusManager() throws Exception {
        int orderIdParam = 3;
        int statusIdParam = 5;
        int managerIdParam = 1;

        int successfulValue = 1;
        boolean expected = true;

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeUpdate())
                .thenReturn(successfulValue);
        doNothing().when(pstmt).setInt(1, orderIdParam);
        doNothing().when(pstmt).setInt(2, statusIdParam);
        doNothing().when(pstmt).setDouble(3, managerIdParam);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlInsertStatusNext()))
                .thenReturn(pstmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        DBManager dbManager = DBManager.getInstance();
        boolean isInsert = dbManager.insertNextStatusToOrderStatusByOrderStatusManager(orderIdParam, statusIdParam, managerIdParam);

        verify(pstmt).setInt(1, orderIdParam);
        verify(pstmt).setInt(2, statusIdParam);
        verify(pstmt).setInt(3, managerIdParam);

        assertEquals(expected, isInsert);
    }

    @Test(expected = DBException.class)
    public void testInsertNextStatusToOrderStatusByOrderStatusManagerException() throws Exception {
        int orderIdParam = 3;
        int statusIdParam = 5;
        int managerIdParam = 1;

        int successfulValue = 1;

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeUpdate())
                .thenReturn(successfulValue);
        doNothing().when(pstmt).setInt(1, orderIdParam);
        doNothing().when(pstmt).setInt(2, statusIdParam);
        doNothing().when(pstmt).setDouble(3, managerIdParam);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlInsertStatusNext()))
                .thenReturn(pstmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenThrow(DBException.class);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        DBManager dbManager = DBManager.getInstance();
        boolean isInsert = dbManager.insertNextStatusToOrderStatusByOrderStatusManager(orderIdParam, statusIdParam, managerIdParam);

        verify(pstmt).setInt(1, orderIdParam);
        verify(pstmt).setInt(2, statusIdParam);
        verify(pstmt).setInt(3, managerIdParam);
    }

    @Test
    public void testUpdateCommentByOrder() throws Exception {
        int orderIdParam = 1;
        String commentParam = "Заказ на ремонт выполнен безупречно";

        int successfulValue = 1;
        boolean expected = true;

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeUpdate())
                .thenReturn(successfulValue);
        doNothing().when(pstmt).setString(1, commentParam);
        doNothing().when(pstmt).setInt(2, orderIdParam);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlUpdateCommentByOrder()))
                .thenReturn(pstmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        DBManager dbManager = DBManager.getInstance();
        boolean isUpdate = dbManager.updateCommentByOrder(orderIdParam, commentParam);

        verify(pstmt).setString(1, commentParam);
        verify(pstmt).setInt(2, orderIdParam);

        assertEquals(expected, isUpdate);
    }

    @Test
    public void testUpdateCostByOrderId() throws Exception {
        int managerIdParam = 1;
        int idUnSubmittedOrderParam = 1;
        double costParam= 25.0;

        int successfulValue = 1;
        boolean expected = true;

        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.executeUpdate())
                .thenReturn(successfulValue);
        doNothing().when(pstmt).setDouble(1, costParam);
        doNothing().when(pstmt).setInt(2, managerIdParam);
        doNothing().when(pstmt).setInt(3, idUnSubmittedOrderParam);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(DBManager.getSqlUpdateOrderCostManaderidByOrder()))
                .thenReturn(pstmt);

        DBUtils dbUtils = mock(DBUtils.class);
        when(dbUtils.getConnection())
                .thenReturn(con);

        mocked.when(DBUtils::getInstance)
                .thenReturn(dbUtils);

        DBManager dbManager = DBManager.getInstance();
        boolean isUpdate = dbManager.updateCostByOrderId(managerIdParam, idUnSubmittedOrderParam, costParam);

        verify(pstmt).setDouble(1, costParam);
        verify(pstmt).setInt(2, managerIdParam);
        verify(pstmt).setInt(3, idUnSubmittedOrderParam);

        assertEquals(expected, isUpdate);
    }

    @Test
    public void testUpdateUnSubmittedOrder() throws Exception {
        int foremanIdParam = 5;
        String listIdUnSubmittedOrderParam = "1_9_";

        int successfulValue = 1;
        boolean expected = true;

        PreparedStatement pstmt = mock(PreparedStatement.class);
        Connection con = mock(Connection.class);
        String[] idUnSubmittedOrder = listIdUnSubmittedOrderParam.split("_");

        for (String str : idUnSubmittedOrder) {
            when(pstmt.executeUpdate())
                    .thenReturn(successfulValue);
            doNothing().when(pstmt).setInt(1, foremanIdParam);
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

        DBManager dbManager = DBManager.getInstance();
        boolean isUpdate = dbManager.updateUnSubmittedOrder(foremanIdParam, listIdUnSubmittedOrderParam);

        verify(pstmt, times(idUnSubmittedOrder.length)).setInt(1, foremanIdParam);
        for (String str : idUnSubmittedOrder) {
            verify(pstmt).setInt(2, Integer.parseInt(str));
        }

        assertEquals(expected, isUpdate);
    }

}
