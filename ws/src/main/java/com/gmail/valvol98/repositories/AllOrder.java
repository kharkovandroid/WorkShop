package com.gmail.valvol98.repositories;

import com.gmail.valvol98.data.UnChangeData;
import com.gmail.valvol98.db.*;
import com.gmail.valvol98.db.entity.Order;
import com.gmail.valvol98.db.entity.OrderStatus;
import com.gmail.valvol98.db.entity.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AllOrder {

    /**
     * Method that provide finding all orders.
     * @return collection of the order
     */
    public List<Order> findAllOrder() throws DBException {
        List<Order> orders = null;
        Connection conOuter = null;
        Statement stmtOuter = null;
        ResultSet rsOuter = null;

        try {
            conOuter = DBUtils.getInstance().getConnection();
            stmtOuter = conOuter.createStatement();
            rsOuter = stmtOuter.executeQuery(Query.SQL_FIND_ALL_ORDER);
            orders = new ArrayList<>();
            while (rsOuter.next()) {
                Order order = new Extract().extractAllOrder(rsOuter);

                String foremanName = findForemanNameByForemanId(order.getForemanId());
                order.setForemanName(foremanName);

                List<OrderStatus> orderStatuses = findOrderStatusesByOrderId(order.getId());

                if (orderStatuses.size() != 0) {
                    if (!UnChangeData.ORDER_COMPLETED.equals(orderStatuses.get(0).getStatusType()) ||
                            !UnChangeData.ORDER_REFUSE.equals(orderStatuses.get(0).getStatusType())) {

                        List<Status> statusNext = findStatusNextByCurrentStatusIdByRole(orderStatuses.get(0).getStatusId(), UnChangeData.ROLE_MANAGER);
                        order.setStatusNext(statusNext);
                    }
                }
                order.setOrderStatus(orderStatuses);

                orders.add(order);
            }
        } catch (SQLException ex) {
            throw new DBException("errorMessage.SelectIsImpossible", ex);
        } finally {
            try {
                new CloseConnection().close(rsOuter);
                new CloseConnection().close(stmtOuter);
                new CloseConnection().close(conOuter);
            } catch (SQLException ex) {
                throw new DBException(ex.getMessage(), ex);
            }
        }
        return orders;
    }

    private String findForemanNameByForemanId(int foremanId) throws DBException {
        Connection conInnerForeman = null;
        PreparedStatement pstmtInnerForeman = null;
        ResultSet rsInnerForeman = null;

        try {
            conInnerForeman = DBUtils.getInstance().getConnection();
            pstmtInnerForeman = conInnerForeman.prepareStatement(Query.SQL_FIND_USER_BY_ID);
            int columnIndexInnerForeman = 1;
            pstmtInnerForeman.setInt(columnIndexInnerForeman++, foremanId);
            rsInnerForeman = pstmtInnerForeman.executeQuery();
            String foremanName = null;
            if (rsInnerForeman.next()) {
                foremanName = rsInnerForeman.getString("name");
            }
            return foremanName;
        } catch (SQLException ex) {
            throw new DBException("errorMessage.SelectIsImpossible", ex);
        } finally {
            try {
                new CloseConnection().close(rsInnerForeman);
                new CloseConnection().close(pstmtInnerForeman);
                new CloseConnection().close(conInnerForeman);
            } catch (SQLException ex) {
                throw new DBException(ex.getMessage(), ex);
            }
        }
    }

    private List<OrderStatus> findOrderStatusesByOrderId(int orderId) throws DBException {
        Connection conInnerOrder = null;
        PreparedStatement pstmtInnerOrder = null;
        ResultSet rsInnerOrder = null;
        List<OrderStatus> orderStatuses = null;

        try {
            conInnerOrder = DBUtils.getInstance().getConnection();
            pstmtInnerOrder = conInnerOrder.prepareStatement(Query.SQL_FIND_ORDER_STATUS_BY_ORDER);
            int columnIndexInnerOrder = 1;
            pstmtInnerOrder.setInt(columnIndexInnerOrder++, orderId);
            rsInnerOrder = pstmtInnerOrder.executeQuery();
            orderStatuses = new ArrayList<>();
            while (rsInnerOrder.next()) {
                OrderStatus orderStatus = new Extract().extractOrderStatus(rsInnerOrder);
                orderStatuses.add(orderStatus);
            }
            return orderStatuses;
        } catch (SQLException ex) {
            throw new DBException("errorMessage.SelectIsImpossible", ex);
        } finally {
            try {
                new CloseConnection().close(rsInnerOrder);
                new CloseConnection().close(pstmtInnerOrder);
                new CloseConnection().close(conInnerOrder);
            } catch (SQLException ex) {
                throw new DBException(ex.getMessage(), ex);
            }
        }
    }

    private List<Status> findStatusNextByCurrentStatusIdByRole(int currentStatusId, String role) throws DBException {
        Connection conInnerStatusNext = null;
        PreparedStatement pstmtInnerStatusNext = null;
        ResultSet rsInnerStatusNext = null;
        List<Status> statusNext = null;

        try {
            conInnerStatusNext = DBUtils.getInstance().getConnection();
            pstmtInnerStatusNext = conInnerStatusNext.prepareStatement(Query.SQL_FIND_STATUS_NEXT);
            int columnIndexInnerStatusNext = 1;
            pstmtInnerStatusNext.setInt(columnIndexInnerStatusNext++, currentStatusId);
            pstmtInnerStatusNext.setString(columnIndexInnerStatusNext++, role);
            rsInnerStatusNext = pstmtInnerStatusNext.executeQuery();
            statusNext = new ArrayList<>();
            while (rsInnerStatusNext.next()) {
                int id = rsInnerStatusNext.getInt("status_to_id");
                String type = rsInnerStatusNext.getString("type");
                statusNext.add(new Status(id, type));
            }
        } catch (SQLException ex) {
            throw new DBException("errorMessage.SelectIsImpossible", ex);
        } finally {
            try {
                new CloseConnection().close(rsInnerStatusNext);
                new CloseConnection().close(pstmtInnerStatusNext);
                new CloseConnection().close(conInnerStatusNext);
            } catch (SQLException ex) {
                throw new DBException(ex.getMessage(), ex);
            }
        }
        return statusNext;
    }

}
