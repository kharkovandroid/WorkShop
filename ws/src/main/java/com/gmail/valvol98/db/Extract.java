package com.gmail.valvol98.db;

import com.gmail.valvol98.db.entity.Order;
import com.gmail.valvol98.db.entity.OrderStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Extract {

    public Order extractAllOrder(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        double cost = rs.getDouble("cost");
        String description = rs.getString("description");
        String datetime = rs.getString("datetime");
        int userId = rs.getInt("user_id");
        int foremanId = rs.getInt("foreman_id");
        int managerId = rs.getInt("manager_id");
        return new Order(cost, id, description, datetime, userId, foremanId, managerId);
    }

    public OrderStatus extractOrderStatus(ResultSet rs) throws SQLException {
        int orderId = rs.getInt("order_status.order_id");
        int statusId = rs.getInt("order_status.status_id");
        int managerForemanId = rs.getInt("order_status.manager_foreman_id");
        String datetime = rs.getString("order_status.datetime");
        String type = rs.getString("status.type");
        return new OrderStatus(orderId, statusId, managerForemanId, datetime, type);
    }


}
