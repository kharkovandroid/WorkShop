package com.gmail.valvol98.db;

import com.gmail.valvol98.data.UnChangeData;
import com.gmail.valvol98.db.entity.*;
import com.gmail.valvol98.service.ServiceCalculation;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.*;
import java.util.*;

import javax.sql.DataSource;

/**
 * Work with database.
 *
 * @author V.Volovshchykov
 *
 */
public class DBManager {

	private static final String SQL_FIND_ALL_USERS = 
			"SELECT user.id, user.login, user.password, user.name, user.role_id, role.role " +
					"FROM user, role WHERE user.role_id = role.id";

	public static String getSqlFindAllUsers() {
		return SQL_FIND_ALL_USERS;
	}

	private static final String SQL_FIND_USER_BY_LOGIN =
			"SELECT user.id, user.login, user.password, user.name, user.role_id, role.role " +
					"FROM user, role WHERE user.role_id = role.id " +
					"AND user.login = ?";

	public static String getSqlFindUserByLogin() {
		return SQL_FIND_USER_BY_LOGIN;
	}

	private static final String SQL_INSERT_ORDER_BY_USER =
			"INSERT INTO workshop.order (user_id, description) " +
			"VALUES (?, ?)";

	public static String getSqlInsertOrderByUser() {
		return SQL_INSERT_ORDER_BY_USER;
	}

	private static final String SQL_FIND_ACCOUNT_BY_USER =
			"SELECT account.id, account.user_id, account.manager_id, account.payment, account.datetime, user.name " +
					"FROM account, user WHERE account.user_id = user.id  AND " +
					"account.user_id = ? " +
					"ORDER BY datetime LIMIT ?, ?";

	public static String getSqlFindAccountByUser() {
		return SQL_FIND_ACCOUNT_BY_USER;
	}

	private static final String SQL_FIND_ROWS_ACCOUNT_BY_USER =
			"SELECT COUNT(*) AS accountRows " +
					"FROM account, user WHERE account.user_id = user.id  AND " +
					"account.user_id = ?";

	public static String getSqlFindRowsAccountByUser() {
		return SQL_FIND_ROWS_ACCOUNT_BY_USER;
	}

	private static final String SQL_FIND_SUM_ACCOUNT_BY_USER =
			"SELECT COALESCE(SUM(account.payment), 0) AS sumAccount " +
					"FROM account WHERE account.user_id = ?";

	public static String getSqlFindSumAccountByUser() {
		return SQL_FIND_SUM_ACCOUNT_BY_USER;
	}

	/*private static final String SQL_FIND_ORDER_STATUS_BY_ORDER =
			"SELECT order_status.order_id, order_status.status_id, order_status.manager_foreman_id, order_status.datetime, status.type " +
				"FROM order_status, status WHERE " +
				"order_status.status_id = status.id AND order_status.order_id = ? " +
				"ORDER BY order_status.datetime DESC";

	public static String getSqlFindOrderStatusByOrder() {
		return SQL_FIND_ORDER_STATUS_BY_ORDER;
	}*/

	private static final String SQL_FIND_ORDER_BY_USER =
			"SELECT workshop.order.id, workshop.order.foreman_id, workshop.order.user_id, " +
				"workshop.order.manager_id, workshop.order.description, workshop.order.cost, " +
				"workshop.order.comment, workshop.order.datetime " +
				"FROM workshop.order WHERE user_id = ? ORDER BY workshop.order.datetime DESC";

	public static String getSqlFindOrderByUser() {
		return SQL_FIND_ORDER_BY_USER;
	}

	private static final String SQL_UPDATE_COMMENT_BY_ORDER =
			"UPDATE workshop.order SET comment = ? WHERE id = ?";

	public static String getSqlUpdateCommentByOrder() {
		return SQL_UPDATE_COMMENT_BY_ORDER;
	}

	//1 затраты пользователя по оплаченным заказам - ПЛЮСУЮТСЯ К ЗАТРАТАМ
	private static final String SQL_FIND_SUM_PAID_ORDER_BY_USER =
			"SELECT COALESCE(SUM(workshop.order.cost), 0) AS sumPaid FROM workshop.order " +
					"WHERE workshop.order.user_id = ? AND " +
					"id IN (SELECT workshop.order_status.order_id FROM workshop.order_status " +
					"WHERE order_status.status_id IN " +
					"(SELECT status.id FROM status WHERE type = '" +
					UnChangeData.ORDER_PAID + "'))";

	public static String getSqlFindSumPaidOrderByUser() {
		return SQL_FIND_SUM_PAID_ORDER_BY_USER;
	}

	//2 затраты пользователя по отмененным заказам - МИНУСУЮТСЯ ОТ ЗАТРАТ
	private static final String SQL_FIND_SUM_REFUSE_ORDER_BY_USER =
			"SELECT COALESCE(SUM(workshop.order.cost), 0) AS sumRefuse " +
					"FROM workshop.order WHERE user_id = ? AND " +
					"id IN (SELECT order_id FROM order_status WHERE " +
					"status_id IN (SELECT id FROM status WHERE type = '" +
					UnChangeData.ORDER_REFUSE + "'))";

	public static String getSqlFindSumRefuseOrderByUser() {
		return SQL_FIND_SUM_REFUSE_ORDER_BY_USER;
	}

	//3 все затраты пользователя
	private static final String SQL_FIND_SUM_RESERVE_PAID_REFUSE_ORDER_BY_USER =
			"SELECT COALESCE(SUM(workshop.order.cost), 0) AS sumReservePaidRefuse " +
					"FROM workshop.order WHERE user_id = ?";

	public static String getSqlFindSumReservePaidRefuseOrderByUser() {
		return SQL_FIND_SUM_RESERVE_PAID_REFUSE_ORDER_BY_USER;
	}

	//все затраты пользователя (оплаченные и зарезервированные заказы) = 3 - 2
	//все затраты пользователя (зарезервированные) = 3 - 1 - 2

	private static final String SQL_FIND_ORDER_UNREFUSED_AND_UNUSED_BY_FOREMAN =
			"SELECT workshop.order.id, workshop.order.foreman_id, " +
					"workshop.order.description, workshop.order.datetime " +
					"FROM workshop.order WHERE foreman_id = ? " +
					"AND manager_id IS NOT NULL AND cost > 0 " +
					"ORDER BY datetime";

	private static final String SQL_INSERT_ORDERSTATUS_BY_ORDER_TYPE =
			"INSERT INTO workshop.order_status (order_id, status_id, " +
					"manager_foreman_id) " +
					"VALUES (?, ?, ?)";

	private static final String SQL_FIND_STATUS_BY_TYPE =
			"SELECT status.id, status.type FROM status WHERE type = ?";

	public static String getSqlFindStatusByType() {
		return SQL_FIND_STATUS_BY_TYPE;
	}

	private static final String SQL_FIND_ALL_USER_BY_ROLE =
			"SELECT user.id, user.name from user WHERE " +
					"user.role_id IN (SELECT role.id FROM " +
					"role WHERE role.role = ?) ORDER BY user.name";

	public static String getSqlFindAllUserByRole() {
		return SQL_FIND_ALL_USER_BY_ROLE;
	}

	private static final String SQL_INSERT_SUM_TO_ACCOUNT_BY_USER_MANAGER =
			"INSERT INTO ACCOUNT (user_id, manager_id, payment) VALUES (?, ?, ?);";

	public static String getSqlInsertSumToAccountByUserManager() {
		return SQL_INSERT_SUM_TO_ACCOUNT_BY_USER_MANAGER;
	}

	private static final String SQL_FIND_UNSUBMITTED_ORDER =
			"SELECT workshop.order.id, workshop.order.cost, " +
					"workshop.order.description, workshop.order.datetime, " +
					"workshop.user.name,  workshop.order.user_id, " +
					"workshop.order.foreman_id, workshop.order.manager_id " +
					"FROM workshop.order, workshop.user " +
					"WHERE (workshop.order.manager_id IS NULL OR " +
					"workshop.order.foreman_id IS NULL OR workshop.order.cost = 0) AND " +
    				"(workshop.order.user_id = workshop.user.id) " +
					"ORDER BY workshop.order.datetime";

	public static String getSqlFindUnsubmittedOrder() {
		return SQL_FIND_UNSUBMITTED_ORDER;
	}

	private static final String SQL_UPDATE_ORDER_COST_MANADERID_BY_ORDER =
			"UPDATE workshop.order SET cost = ?, manager_id = ? " +
					"WHERE workshop.order.id = ?";

	public static String getSqlUpdateOrderCostManaderidByOrder() {
		return SQL_UPDATE_ORDER_COST_MANADERID_BY_ORDER;
	}

	private static final String SQL_UPDATE_ORDER_FOREMAN_ID_BY_ORDER_ID =
			"UPDATE workshop.order SET foreman_id = ? WHERE id = ?";

	public static String getSqlUpdateOrderForemanIdByOrderId() {
		return SQL_UPDATE_ORDER_FOREMAN_ID_BY_ORDER_ID;
	}

	/*private static final String SQL_FIND_ALL_ORDER =
			"SELECT workshop.order.id, workshop.order.foreman_id, " +
					"workshop.order.user_id, " +
					"workshop.order.manager_id, workshop.order.description, " +
					"workshop.order.cost, workshop.order.datetime " +
					"FROM workshop.order";*/

	/*private static final String SQL_FIND_USER_BY_ID =
			"SELECT name FROM USER WHERE id = ?";*/

	/*private static final String SQL_FIND_STATUS_NEXT =
			"SELECT list_status.status_to_id, status.type FROM " +
					"list_status, status WHERE status_from_id = ? " +
					"AND status_to_id = status.id AND " +
					"role_id IN (SELECT id FROM role WHERE role = ?)";*/

	private static final String SQL_INSERT_STATUS_NEXT =
			"INSERT INTO order_status (order_id, status_id, manager_foreman_id) " +
					"VALUES(?, ?, ?)";

	public static String getSqlInsertStatusNext() {
		return SQL_INSERT_STATUS_NEXT;
	}

	private static DBManager instance;

	/**
	 * Method that discribe Singleton to create  DBManager object.
	 */
	public static synchronized DBManager getInstance() throws DBException {
		if (instance == null) {
			instance = new DBManager();
		}
		return instance;
	}
	
	private DataSource ds;

	/**
	 * Constractor to create  DBManager object.
	 */
	private DBManager() {
	}

	/**
	 * Method that provide finding all users.
	 * @return user's collection
	 */
	public List<User> findUsers() throws DBException {
		List<User> users = null;
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = DBUtils.getInstance().getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL_FIND_ALL_USERS);

			users = new ArrayList<>();
			while (rs.next()) {
				users.add(extractUser(rs));
			}
		} catch (SQLException ex) {
			//ex.printStackTrace();
			throw new DBException("errorMessage.SelectIsImpossible", ex);
		} finally {
			try {
				close(rs);
				close(stmt);
				close(con);
			} catch (SQLException ex) {
				throw new DBException(ex.getMessage(), ex);
			}
		}
		return users;
	}

	/**
	 * Method that provide finding all unsubmitted orders.
	 */
	public List<Order> findUnSubmittedOrder() throws DBException{
		List<Order> orders = null;
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = DBUtils.getInstance().getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL_FIND_UNSUBMITTED_ORDER);

			orders = new ArrayList<>();
			while (rs.next()) {
				orders.add(extractUnSubmittedOrder(rs));
			}
		} catch (SQLException ex) {
			//ex.printStackTrace();
			throw new DBException("errorMessage.SelectIsImpossible", ex);
		} finally {
			try {
				close(rs);
				close(stmt);
				close(con);
			} catch(SQLException ex) {
				throw new DBException(ex.getMessage(), ex);
			}
		}
		return orders;
	}

	/**
	 * Method that provide finding all users by role.
	 * @param role role's name
	 * @return user's collection
	 */
	public List<User> findUsersByRole(String role) throws DBException {
		List<User> users = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DBUtils.getInstance().getConnection();
			pstmt = con.prepareStatement(SQL_FIND_ALL_USER_BY_ROLE);
			int columnIndex = 1;
			pstmt.setString(columnIndex++, role);
			rs = pstmt.executeQuery();

			users = new ArrayList<>();
			while (rs.next()) {
				int id = rs.getInt("user.id");
				String name = rs.getString("user.name");
				double generalActiveMoneyOnAccount = new ServiceCalculation().findGeneralActiveMoneyOnAccount(id);
				User user = new User(id, name, generalActiveMoneyOnAccount);
				users.add(user);
			}
		} catch (SQLException ex) {
			//ex.printStackTrace();
			throw new DBException("errorMessage.SelectIsImpossible", ex);
		} finally {
			try {
				close(rs);
				close(pstmt);
				close(con);
			} catch(SQLException ex) {
				throw new DBException(ex.getMessage(), ex);
			}
		}
		return users;
	}

	/**
	 * Method that provide finding user by login.
	 * @param login user's logib
	 * @return user's object
	 */
	public User findUser(String login) throws DBException {
		User user = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DBUtils.getInstance().getConnection();
			pstmt = con.prepareStatement(SQL_FIND_USER_BY_LOGIN);
			int columnIndex = 1;
			pstmt.setString(columnIndex++, login);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = extractUser(rs);
			}
		} catch (SQLException ex) {
			throw new DBException("errorMessage.SelectIsImpossible", ex);
		} finally {
			try {
				close(rs);
				close(pstmt);
				close(con);
			} catch(SQLException ex) {
				throw new DBException(ex.getMessage(), ex);
			}
		}
		return user;
	}

	/**
	 * Method that provide finding status code by status type.
	 * @param type status type
	 * @return status code
	 */
	public Integer findStatus(String type) throws DBException {
		Integer id = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DBUtils.getInstance().getConnection();
			pstmt = con.prepareStatement(SQL_FIND_STATUS_BY_TYPE);
			int columnIndex = 1;
			pstmt.setString(columnIndex++, type);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				id = rs.getInt("id");
			}
		} catch (SQLException ex) {
			//ex.printStackTrace();
			throw new DBException("errorMessage.SelectIsImpossible", ex);
		} finally {
			try {
				close(rs);
				close(pstmt);
				close(con);
			} catch (SQLException ex) {
				throw new DBException(ex.getMessage(), ex);
			}
		}
		return id;
	}

	/**
	 * Method that provide finding limited accounts collection by user's code.
	 * @param userId user's code
	 * @return limited account's object
	 */
	public List<Account> findAccountByUser(int userId, int rowSinceFind) throws DBException {
		List<Account> accounts = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DBUtils.getInstance().getConnection();
			pstmt = con.prepareStatement(SQL_FIND_ACCOUNT_BY_USER);
			int columnIndex = 1;
			pstmt.setInt(columnIndex++, userId);
			pstmt.setInt(columnIndex++, rowSinceFind);
			pstmt.setInt(columnIndex++, UnChangeData.NUMBER_ACCOUNTS_PER_PAGE);
			rs = pstmt.executeQuery();
			accounts = new ArrayList<>();
			while (rs.next()) {
				Account account = extractAccount(rs);
				accounts.add(account);
			}
		} catch (SQLException ex) {
			//ex.printStackTrace();
			throw new DBException("errorMessage.SelectIsImpossible", ex);
		} finally {
			try {
				close(rs);
				close(pstmt);
				close(con);
			} catch (SQLException ex) {
				throw new DBException(ex.getMessage(), ex);
			}
		}
		return accounts;
	}

	/**
	 * Method that provide finding general rows of account by user's code.
	 * @param userId user's code
	 * @return general rows
	 */
	public int findRowsAccountByUser(int userId) throws DBException {
		int rows = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DBUtils.getInstance().getConnection();
			pstmt = con.prepareStatement(SQL_FIND_ROWS_ACCOUNT_BY_USER);
			int columnIndex = 1;
			pstmt.setInt(columnIndex++, userId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				rows = rs.getInt("accountRows");
			}
		} catch (SQLException ex) {
			//ex.printStackTrace();
			throw new DBException("errorMessage.SelectIsImpossible", ex);
		} finally {
			try {
				close(rs);
				close(pstmt);
				close(con);
			} catch (SQLException ex) {
				throw new DBException(ex.getMessage(), ex);
			}
		}
		return rows;
	}

	/**
	 * Method that provide finding general account by user's code.
	 * @param userId user's code
	 * @return general account
	 */
	public double findSumAccountUser(int userId) throws DBException {
		double sum = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DBUtils.getInstance().getConnection();
			pstmt = con.prepareStatement(SQL_FIND_SUM_ACCOUNT_BY_USER);
			int columnIndex = 1;
			pstmt.setInt(columnIndex++, userId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				sum = rs.getDouble("sumAccount");
			}
		} catch (SQLException ex) {
			//ex.printStackTrace();
			throw new DBException("errorMessage.SelectIsImpossible", ex);
		} finally {
			try {
				close(rs);
				close(pstmt);
				close(con);
			} catch (SQLException ex) {
				throw new DBException(ex.getMessage(), ex);
			}
		}
		return sum;
	}

	/**
	 * Method that provide finding general summa of the paid orders.
	 * @param userId user's code
	 * @return general summa of the paid order
	 */
	public double findSumPaidUser(int userId) throws DBException {
		double sum = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DBUtils.getInstance().getConnection();
			pstmt = con.prepareStatement(SQL_FIND_SUM_PAID_ORDER_BY_USER);
			int columnIndex = 1;
			pstmt.setInt(columnIndex++, userId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				sum = rs.getDouble("sumPaid");
			}
		} catch (SQLException ex) {
			//ex.printStackTrace();
			throw new DBException("errorMessage.SelectIsImpossible", ex);
		} finally {
			try {
				close(rs);
				close(pstmt);
				close(con);
			} catch (SQLException ex) {
				throw new DBException(ex.getMessage(), ex);
			}
		}
		return sum;
	}

	/**
	 * Method that provide finding general summa of the refuse orders.
	 * @param userId user's code
	 * @return general summa of the refuse order
	 */
	public double findSumRefuseUser(int userId) throws DBException {
		double sum = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DBUtils.getInstance().getConnection();
			pstmt = con.prepareStatement(SQL_FIND_SUM_REFUSE_ORDER_BY_USER);
			int columnIndex = 1;
			pstmt.setInt(columnIndex++, userId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				sum = rs.getDouble("sumRefuse");
			}
		} catch (SQLException ex) {
			//ex.printStackTrace();
			throw new DBException("errorMessage.SelectIsImpossible", ex);
		} finally {
			try {
				close(rs);
				close(pstmt);
				close(con);
			} catch (SQLException ex) {
				throw new DBException(ex.getMessage(), ex);
			}
		}
		return sum;
	}

	/**
	 * Method that provide finding general summa of the reserve paid refuse orders.
	 * @param userId user's code
	 * @return general summa of the reserve paid refuse order
	 */
	public double findSumReservePaidRefuseUser(int userId) throws DBException {
		double sum = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DBUtils.getInstance().getConnection();
			pstmt = con.prepareStatement(SQL_FIND_SUM_RESERVE_PAID_REFUSE_ORDER_BY_USER);
			int columnIndex = 1;
			pstmt.setInt(columnIndex++, userId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				sum = rs.getDouble("sumReservePaidRefuse");
			}
		} catch (SQLException ex) {
			//ex.printStackTrace();
			throw new DBException("errorMessage.SelectIsImpossible", ex);
		} finally {
			try {
				close(rs);
				close(pstmt);
				close(con);
			} catch (SQLException ex) {
				throw new DBException(ex.getMessage(), ex);
			}
		}
		return sum;
	}

	/**
	 * Method that provide finding all user's orders.
	 * @param userId user's code
	 * @return collection of the user order
	 */
	public List<Order> findOrderUser(int userId) throws DBException {
		List<Order> orders = null;
		Connection conOuter = null;
		PreparedStatement pstmtOuter = null;
		ResultSet rsOuter = null;

		Connection conInner = null;
		PreparedStatement pstmtInner = null;
		ResultSet rsInner = null;
		try {
			conOuter = DBUtils.getInstance().getConnection();
			pstmtOuter = conOuter.prepareStatement(SQL_FIND_ORDER_BY_USER);
			int columnIndexOuter = 1;
			pstmtOuter.setInt(columnIndexOuter++, userId);
			rsOuter = pstmtOuter.executeQuery();
			orders = new ArrayList<>();
			while (rsOuter.next()) {
				Order order = extractOrderUser(rsOuter);
				conInner = DBUtils.getInstance().getConnection();
				pstmtInner = conInner.prepareStatement(Query.SQL_FIND_ORDER_STATUS_BY_ORDER);
				int columnIndexInner = 1;
				pstmtInner.setInt(columnIndexInner++, order.getId());
				rsInner = pstmtInner.executeQuery();
				List<OrderStatus> orderStatuses = new ArrayList<>();
				while (rsInner.next()) {
					OrderStatus orderStatus = new Extract().extractOrderStatus(rsInner);
					orderStatuses.add(orderStatus);
				}
				order.setOrderStatus(orderStatuses);
				orders.add(order);
			}
		} catch (SQLException ex) {
			throw new DBException("errorMessage.SelectIsImpossible", ex);
		} finally {
			try {
				close(rsInner);
				close(pstmtInner);
				close(conInner);

				close(rsOuter);
				close(pstmtOuter);
				close(conOuter);
			} catch (SQLException ex) {
				throw new DBException(ex.getMessage(), ex);
			}
		}
		return orders;
	}

	/**
	 * Method that provide finding all orders.
	 * @return collection of the order
	 */
	/*public List<Order> findAllOrder() throws DBException {
		List<Order> orders = null;
		Connection conOuter = null;
		Statement stmtOuter = null;
		ResultSet rsOuter = null;

		Connection conInnerOrder = null;
		PreparedStatement pstmtInnerOrder = null;
		ResultSet rsInnerOrder = null;

		Connection conInnerForeman = null;
		PreparedStatement pstmtInnerForeman = null;
		ResultSet rsInnerForeman = null;

		Connection conInnerStatusNext = null;
		PreparedStatement pstmtInnerStatusNext = null;
		ResultSet rsInnerStatusNext = null;
		try {
			conOuter = DBUtils.getInstance().getConnection();
			conInnerForeman = DBUtils.getInstance().getConnection();
			conInnerOrder = DBUtils.getInstance().getConnection();
			conInnerStatusNext = DBUtils.getInstance().getConnection();
			stmtOuter = conOuter.createStatement();
			rsOuter = stmtOuter.executeQuery(SQL_FIND_ALL_ORDER);
			orders = new ArrayList<>();
			while (rsOuter.next()) {
				Order order = extractAllOrder(rsOuter);

				pstmtInnerForeman = conInnerForeman.prepareStatement(SQL_FIND_USER_BY_ID);
				int columnIndexInnerForeman = 1;
				pstmtInnerForeman.setInt(columnIndexInnerForeman++, order.getForemanId());
				rsInnerForeman = pstmtInnerForeman.executeQuery();
				String foremanName = null;
				if (rsInnerForeman.next()) {
					foremanName = rsInnerForeman.getString("name");
				}
				order.setForemanName(foremanName);

				pstmtInnerOrder = conInnerOrder.prepareStatement(SQL_FIND_ORDER_STATUS_BY_ORDER);
				int columnIndexInnerOrder = 1;
				pstmtInnerOrder.setInt(columnIndexInnerOrder++, order.getId());
				rsInnerOrder = pstmtInnerOrder.executeQuery();
				List<OrderStatus> orderStatuses = new ArrayList<>();
				while (rsInnerOrder.next()) {
					OrderStatus orderStatus = extractOrderStatus(rsInnerOrder);
					orderStatuses.add(orderStatus);
				}
				if (orderStatuses.size() != 0) {
					if (!UnChangeData.ORDER_COMPLETED.equals(orderStatuses.get(0).getStatusType()) ||
							!UnChangeData.ORDER_REFUSE.equals(orderStatuses.get(0).getStatusType())) {
						pstmtInnerStatusNext = conInnerStatusNext.prepareStatement(SQL_FIND_STATUS_NEXT);
						int columnIndexInnerStatusNext = 1;
						pstmtInnerStatusNext.setInt(columnIndexInnerStatusNext++, orderStatuses.get(0).getStatusId());
						pstmtInnerStatusNext.setString(columnIndexInnerStatusNext++, UnChangeData.ROLE_MANAGER);
						rsInnerStatusNext = pstmtInnerStatusNext.executeQuery();
						List<Status> statusNext = new ArrayList<>();
						while(rsInnerStatusNext.next()) {
							int id = rsInnerStatusNext.getInt("status_to_id");
							String type = rsInnerStatusNext.getString("type");
							statusNext.add(new Status(id, type));
						}
						order.setStatusNext(statusNext);
					}
				}
				//System.out.println("OrderStatus " + orderStatuses);
				//System.out.println("OrderStatus " + orderStatuses.size());
				order.setOrderStatus(orderStatuses);

				orders.add(order);
			}
		} catch (SQLException ex) {
			//ex.printStackTrace();
			throw new DBException("errorMessage.SelectIsImpossible", ex);
		} finally {
			try {
				close(rsInnerStatusNext);
				close(pstmtInnerStatusNext);
				close(conInnerStatusNext);

				close(rsInnerForeman);
				close(pstmtInnerForeman);
				close(conInnerForeman);

				close(rsInnerOrder);
				close(pstmtInnerOrder);
				close(conInnerOrder);

				close(rsOuter);
				close(stmtOuter);
				close(conOuter);
			} catch (SQLException ex) {
				throw new DBException(ex.getMessage(), ex);
			}
		}
		return orders;
	}*/

	/**
	 * Method that provide finding orders by foreman.
	 * @param userId foreman's code
	 * @return collection of the foreman's order
	 */
	public List<Order> findOrderByForeman(int userId) throws DBException {
		List<Order> orders = null;
		Connection conOuter = null;
		PreparedStatement pstmtOuter = null;
		ResultSet rsOuter = null;

		Connection conInner = null;
		PreparedStatement pstmtInner = null;
		ResultSet rsInner = null;
		try {
			conOuter = DBUtils.getInstance().getConnection();
			pstmtOuter = conOuter.prepareStatement(SQL_FIND_ORDER_UNREFUSED_AND_UNUSED_BY_FOREMAN);
			int columnIndexOuter = 1;
			pstmtOuter.setInt(columnIndexOuter++, userId);
			rsOuter = pstmtOuter.executeQuery();
			orders = new ArrayList<>();
			while (rsOuter.next()) {
				Order order = extractOrderForeman(rsOuter);
				conInner = DBUtils.getInstance().getConnection();
				pstmtInner = conInner.prepareStatement(Query.SQL_FIND_ORDER_STATUS_BY_ORDER);
				int columnIndexInner = 1;
				pstmtInner.setInt(columnIndexInner++, order.getId());
				rsInner = pstmtInner.executeQuery();
				List<OrderStatus> orderStatuses = new ArrayList<>();
				while (rsInner.next()) {
					OrderStatus orderStatus = new Extract().extractOrderStatus(rsInner);
					orderStatuses.add(orderStatus);
				}
				order.setOrderStatus(orderStatuses);
				orders.add(order);
			}
			boolean status = false;
			while (status == false) {
				status = true;
				for (int i = 0; i < orders.size(); i++) {
					for (int j = 0; j < orders.get(i).getOrderStatus().size(); j++) {
						if (UnChangeData.ORDER_WAIT_FOR_PAY.equals(orders.get(i).getOrderStatus().get(j).getStatusType()) ||
								UnChangeData.ORDER_PAID.equals(orders.get(i).getOrderStatus().get(j).getStatusType()) ||
								UnChangeData.ORDER_REFUSE.equals(orders.get(i).getOrderStatus().get(j).getStatusType())) {
							orders.remove(i);
							status = false;
							break;
						}
					}
				}
			}
		} catch (SQLException ex) {
			//ex.printStackTrace();
			throw new DBException("errorMessage.SelectIsImpossible", ex);
		} finally {
			try {
				close(rsInner);
				close(pstmtInner);
				close(conInner);

				close(rsOuter);
				close(pstmtOuter);
				close(conOuter);
			} catch (SQLException ex) {
				throw new DBException(ex.getMessage(), ex);
			}
		}
		return orders;
	}

	/**
	 * Method that provide finding status by status's type.
	 * @param typeId status's code
	 * @return status's object
	 */
	public Status findStatusByType(String typeId) throws DBException {
		Status status = null;;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DBUtils.getInstance().getConnection();
			pstmt = con.prepareStatement(SQL_FIND_STATUS_BY_TYPE);
			int columnIndexOuter = 1;
			pstmt.setString(columnIndexOuter++, typeId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("id");
				String type = rs.getString("type");
				status = new Status(id, type);
			}
		} catch (SQLException ex) {
			//ex.printStackTrace();
			throw new DBException("errorMessage.SelectIsImpossible", ex);
		} finally {
			try {
				close(rs);
				close(pstmt);
				close(con);
			} catch (SQLException ex) {
				throw new DBException(ex.getMessage(), ex);
			}
		}
		System.out.println("Status = " + status);
		return status;
	}

	/**
	 * Method that insert order description by user code.
	 * @param userId user's code
	 * @param description description of the order
	 * @return condition of inserting data
	 */
	public boolean insertOrderByUser(int userId, String description) throws DBException {
		boolean isInsert = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DBUtils.getInstance().getConnection();
			pstmt = con.prepareStatement(SQL_INSERT_ORDER_BY_USER);
			con.setAutoCommit(false);
			int columnIndex = 1;
			pstmt.setInt(columnIndex++, userId);
			pstmt.setString(columnIndex++, description);
			if (pstmt.executeUpdate() > 0) {
				isInsert = true;
			}
			con.commit();
		} catch (SQLException ex) {
			//throwables.printStackTrace();
			rollback(con);
			throw new DBException("errorMessage.AddingDataImpossible", ex);
		} finally {
			commit(con);
			try {
				close(pstmt);
				close(con);
			} catch (SQLException ex) {
				throw new DBException(ex.getMessage(), ex);
			}
		}
		return isInsert;
	}

	/**
	 * Method that insert order status by order and type.
	 * @param orderId order's code
	 * @param type type of the order
	 * @param userId user's code
	 * @return condition of inserting data
	 */
	public boolean insertOrderStatusByOrderAndType(int orderId, String type, int userId) throws DBException {
		boolean isInsert = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DBUtils.getInstance().getConnection();
			pstmt = con.prepareStatement(SQL_INSERT_ORDERSTATUS_BY_ORDER_TYPE);
			con.setAutoCommit(false);
			int columnIndex = 1;
			pstmt.setInt(columnIndex++, orderId);

			Integer statusId = findStatus(type);
			if (statusId == null) {return false;}

			pstmt.setInt(columnIndex++, statusId);
			pstmt.setInt(columnIndex++, userId);
			if (pstmt.executeUpdate() > 0) {
				isInsert = true;
			}
			con.commit();
		} catch (SQLException ex) {
			//throwables.printStackTrace();
			rollback(con);
			throw new DBException("errorMessage.InsertIsImpossible", ex);
		} finally {
			commit(con);
			try {
				close(pstmt);
				close(con);
			} catch (SQLException ex) {
				throw new DBException(ex.getMessage(), ex);
			}
		}
		return isInsert;
	}

	/**
	 * Method that insert funds to an account.
	 * @param managerId manager's code
	 * @param userId user's code
	 * @param sumToAccount funds to an account
	 * @return condition of inserting data
	 */
	public boolean insertSumToAccount(int managerId, int userId, double sumToAccount) throws DBException {
		boolean isInsert = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DBUtils.getInstance().getConnection();
			pstmt = con.prepareStatement(SQL_INSERT_SUM_TO_ACCOUNT_BY_USER_MANAGER);
			con.setAutoCommit(false);
			int columnIndex = 1;
			pstmt.setInt(columnIndex++, userId);
			pstmt.setInt(columnIndex++, managerId);
			pstmt.setDouble(columnIndex++, sumToAccount);

			if (pstmt.executeUpdate() > 0) {
				isInsert = true;
			}
			con.commit();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
			rollback(con);
			throw new DBException("errorMessage.MaxAddSummaToAccountLessThan1000", throwables);
		} finally {
			commit(con);
			try {
				close(pstmt);
				close(con);
			} catch (SQLException ex) {
				throw new DBException(ex.getMessage(), ex);
			}
		}
		return isInsert;
	}

	/**
	 * Method that insert order's status by status manager.
	 * @param orderId order's code
	 * @param statusId status's code
	 * @param managerId manager's code
	 * @return condition of inserting data
	 */
	public boolean insertNextStatusToOrderStatusByOrderStatusManager(int orderId, int statusId, int managerId) throws DBException {
		boolean isInsert = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DBUtils.getInstance().getConnection();
			pstmt = con.prepareStatement(SQL_INSERT_STATUS_NEXT);
			con.setAutoCommit(false);
			int columnIndex = 1;
			pstmt.setInt(columnIndex++, orderId);
			pstmt.setInt(columnIndex++, statusId);
			pstmt.setInt(columnIndex++, managerId);

			if (pstmt.executeUpdate() > 0) {
				isInsert = true;
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException("errorMessage.InsertIsImpossible", ex);
		} catch (Exception ex) {
			throw new DBException("errorMessage.InsertIsImpossible", ex);
		}
		finally {
			commit(con);
			try {
				close(pstmt);
				close(con);
			} catch (SQLException ex) {
				throw new DBException(ex.getMessage(), ex);
			}
		}
		return isInsert;
	}

	/**
	 * Method that adding comment to order.
	 * @param orderId order's code
	 * @param comment information about comment
	 * @return condition of adding data
	 */
	public boolean updateCommentByOrder(int orderId, String comment) throws DBException {
		boolean isUpdate = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DBUtils.getInstance().getConnection();
			pstmt = con.prepareStatement(SQL_UPDATE_COMMENT_BY_ORDER);
			con.setAutoCommit(false);
			int columnIndex = 1;
			pstmt.setString(columnIndex++, comment);
			pstmt.setInt(columnIndex++, orderId);
			if (pstmt.executeUpdate() > 0) {
				isUpdate = true;
			}
			con.commit();
		} catch (SQLException ex) {
			//throwables.printStackTrace();
			rollback(con);
			throw new DBException("errorMessage.updateDataIsImpossible", ex);
		} finally {
			commit(con);
			try {
				close(pstmt);
				close(con);
			} catch (SQLException ex) {
				throw new DBException(ex.getMessage(), ex);
			}
		}
		return isUpdate;
	}

	/**
	 * Method that adding order's cost.
	 * @param managerId manager's code
	 * @param idUnSubmittedOrder order's code
	 * @param cost order's cost
	 * @return condition of adding data
	 */
	public boolean updateCostByOrderId(int managerId, int idUnSubmittedOrder, double cost) throws DBException {
		boolean isUpdate = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DBUtils.getInstance().getConnection();
			pstmt = con.prepareStatement(SQL_UPDATE_ORDER_COST_MANADERID_BY_ORDER);
			con.setAutoCommit(false);
			int columnIndex = 1;
			pstmt.setDouble(columnIndex++, cost);
			pstmt.setInt(columnIndex++, managerId);
			pstmt.setInt(columnIndex++, idUnSubmittedOrder);
			if (pstmt.executeUpdate() > 0) {
				isUpdate = true;
			}
			con.commit();
		} catch (SQLException ex) {
			//throwables.printStackTrace();
			rollback(con);
			throw new DBException("errorMessage.UpdateIsImpossible", ex);
		} finally {
			commit(con);
			try {
				close(pstmt);
				close(con);
			} catch (SQLException ex) {
				throw new DBException(ex.getMessage(), ex);
			}
		}
		return isUpdate;
	}

	/**
	 * Method that adding foreman to all unsubmitted orders.
	 * @param foremanId foreman's code
	 * @param listIdUnSubmittedOrder list unsubmitted orders
	 * @return condition of adding data
	 */
	public boolean updateUnSubmittedOrder(int foremanId, String listIdUnSubmittedOrder) throws DBException {
		boolean isUpdate = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DBUtils.getInstance().getConnection();
			con.setAutoCommit(false);
			String[] idUnSubmittedOrder = listIdUnSubmittedOrder.split("_");
			for (String str : idUnSubmittedOrder) {
				pstmt = con.prepareStatement(SQL_UPDATE_ORDER_FOREMAN_ID_BY_ORDER_ID);
				int columnIndex = 1;
				pstmt.setInt(columnIndex++, foremanId);
				pstmt.setInt(columnIndex++, Integer.parseInt(str));
				pstmt.executeUpdate();
			}
			con.commit();
		} catch (SQLException ex) {
			isUpdate = false;
			rollback(con);
			throw new DBException("errorMessage.UpdateIsImpossible", ex);
		} finally {
			commit(con);
			try {
				close(pstmt);
				close(con);
			} catch (SQLException ex) {
				throw new DBException(ex.getMessage(), ex);
			}
			isUpdate = true;
		}
		return isUpdate;
	}

	private void close(AutoCloseable ac) throws SQLException {
		if (ac != null) {
			try {
				ac.close();
			} catch (Exception ex) {
				throw new SQLException("errorMessage.ErrorDuringCloseConnection", ex);
			}
			ac = null;
		}
	}

	private void commit(Connection con) throws DBException {
		try {
			if (con != null) {
				con.commit();
			}
		} catch (SQLException ex) {
			//ex.printStackTrace();
			throw new DBException("errorMessage.CommitIsImpossible", ex);
		}
	}

	private void rollback(Connection con) throws DBException {
		try {
			if (con != null) {
				con.rollback();
			}
		} catch (SQLException ex) {
			//ex.printStackTrace();
			throw new DBException("errorMessage.CancelTransactionIsImpossible", ex);
		}
	}

	private User extractUser(ResultSet rs) throws SQLException {
		int id = rs.getInt("user.id");
		String login = rs.getString("user.login");
		String password = rs.getString("user.password");
		String name = rs.getString("user.name");
		int role_id = rs.getInt("user.role_id");
		String role = rs.getString("role.role");
		return new User(id, login, password, name, role_id, role);
	}

	private Order extractOrderForeman(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		int foreman_id = rs.getInt("foreman_id");
		String description = rs.getString("description");
		String datetime = rs.getString("datetime");
		return new Order(id, foreman_id, description, datetime);
	}

	private Account extractAccount(ResultSet rs) throws SQLException {
		int id = rs.getInt("account.id");
		int userId = rs.getInt("account.user_id");
		int managerId = rs.getInt("account.manager_id");
		Double paymant = rs.getDouble("account.payment");
		String datetime = rs.getString("account.datetime");
		String userName = rs.getString("user.name");
		return new Account(id, userId, managerId, paymant, datetime, userName);
	}

	private Order extractOrderUser(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		int foremanId = rs.getInt("foreman_id");
		int userId = rs.getInt("user_id");
		int managerId = rs.getInt("manager_id");
		String description = rs.getString("description");
		Double cost = rs.getDouble("cost");
		String comment = rs.getString("comment");
		String datetime = rs.getString("datetime");
		return new Order(id, foremanId, managerId, description, cost, comment, datetime);
	}

	/*private Order extractAllOrder(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		double cost = rs.getDouble("cost");
		String description = rs.getString("description");
		String datetime = rs.getString("datetime");
		int userId = rs.getInt("user_id");
		int foremanId = rs.getInt("foreman_id");
		int managerId = rs.getInt("manager_id");
		return new Order(cost, id, description, datetime, userId, foremanId, managerId);
	}*/

	private Order extractUnSubmittedOrder(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		double cost = rs.getDouble("cost");
		String description = rs.getString("description");
		String datetime = rs.getString("datetime");
		String userName = rs.getString("name");
		int userId = rs.getInt("user_id");
		int foremanId = rs.getInt("foreman_id");
		int managerId = rs.getInt("manager_id");
	    return new Order(id, cost, description, datetime, userName, userId, foremanId, managerId);
	}

	/*private OrderStatus extractOrderStatus(ResultSet rs) throws SQLException {
		int orderId = rs.getInt("order_status.order_id");
		int statusId = rs.getInt("order_status.status_id");
		int managerForemanId = rs.getInt("order_status.manager_foreman_id");
		String datetime = rs.getString("order_status.datetime");
		String type = rs.getString("status.type");
		return new OrderStatus(orderId, statusId, managerForemanId, datetime, type);
	}*/

}
