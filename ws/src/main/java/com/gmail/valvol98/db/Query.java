package com.gmail.valvol98.db;

public class Query {

    public static final String SQL_FIND_ALL_ORDER =
            "SELECT workshop.order.id, workshop.order.foreman_id, " +
                    "workshop.order.user_id, " +
                    "workshop.order.manager_id, workshop.order.description, " +
                    "workshop.order.cost, workshop.order.datetime " +
                    "FROM workshop.order";

    public static final String SQL_FIND_USER_BY_ID =
            "SELECT name FROM USER WHERE id = ?";

    public static final String SQL_FIND_ORDER_STATUS_BY_ORDER =
            "SELECT order_status.order_id, order_status.status_id, order_status.manager_foreman_id, order_status.datetime, status.type " +
                    "FROM order_status, status WHERE " +
                    "order_status.status_id = status.id AND order_status.order_id = ? " +
                    "ORDER BY order_status.datetime DESC";

    public static final String SQL_FIND_STATUS_NEXT =
            "SELECT list_status.status_to_id, status.type FROM " +
                    "list_status, status WHERE status_from_id = ? " +
                    "AND status_to_id = status.id AND " +
                    "role_id IN (SELECT id FROM role WHERE role = ?)";

}
