package com.gmail.valvol98.db.entity;

import com.gmail.valvol98.data.UnChangeData;

import java.io.Serializable;
import java.util.List;

/**
 * Order type according to database structure.
 *
 * @author V.Volovshchykov
 *
 */
public class Order implements Serializable {

    private final static long serialVersionUID = -4196939324842801940L;

    private int id;
    private int foremanId;
    private int managerId;
    private String description;
    private double cost;
    private String comment;
    private String datetime;

    private List<OrderStatus> orderStatus;
    private String userName;
    private String foremanName;
    private int userId;
    private boolean isEnoughMoneyOnAccount;
    private List<Status> statusNext;

    public Order(int id, int foremanId, int managerId, String description, double cost, String comment, String datetime) {
        this.id = id;
        this.foremanId = foremanId;
        this.managerId = managerId;
        this.description = description;
        this.cost = cost;
        this.comment = comment;
        this.datetime = datetime;
    }

    public Order(int id, int foremanId, String description, String datetime) {
        this.id = id;
        this.foremanId = foremanId;
        this.description = description;
        this.datetime = datetime;
    }

	//
    public Order(int id, double cost, String description, String datetime, String userName, int userId,
                 int foremanId, int managerId) {
        this.id = id;
        this.cost = cost;
        this.description = description;
        this.datetime = datetime;
        this.userName = userName;
        this.userId = userId;
        this.foremanId = foremanId;
        this.managerId = managerId;
    }

    public Order(double cost, int id, String description, String datetime, int userId,
                 int foremanId, int managerId) {
        this.id = id;
        this.cost = cost;
        this.description = description;
        this.datetime = datetime;
        this.userId = userId;
        this.foremanId = foremanId;
        this.managerId = managerId;
    }

    public int getId() {
        return id;
    }

    public int getForemanId() {
        return foremanId;
    }

    public int getManagerId() {
        return managerId;
    }

    public String getDescription() {
        return description;
    }

    public double getCost() {
        return cost;
    }

    public String getComment() {
        return comment;
    }

    public String getDatetime() {
        return datetime;
    }

    public List<OrderStatus> getOrderStatus() {
        return orderStatus;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserId() {
        return userId;
    }

    public String getForemanName() {
        return foremanName;
    }

    public List<Status> getStatusNext() {
        return statusNext;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setForemanId(int foremanId) {
        this.foremanId = foremanId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setOrderStatus(List<OrderStatus> orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setForemanName(String foremanName) {
        this.foremanName = foremanName;
    }

    public void setStatusNext(List<Status> statusNext) {
        this.statusNext = statusNext;
    }

    public boolean isOrderComplited() {
        boolean isComplete = false;
        for (OrderStatus os : orderStatus) {
            if (os.getStatusType().equals(UnChangeData.ORDER_COMPLETED)) {
                return true;
            }
        }
        return isComplete;
    }

    public boolean isOrderInWorkByForeman() {
        boolean isComplete = false;
        for (OrderStatus os : orderStatus) {
            if (os.getStatusType().equals(UnChangeData.ORDER_COMPLETED)) {
                return true;
            }
        }
        return isComplete;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", foremanId=" + foremanId +
                ", manager_id=" + managerId +
                ", description='" + description + '\'' +
                ", cost=" + cost +
                ", comment='" + comment + '\'' +
                ", datetime='" + datetime + '\'' +
                ", orderStatus=" + orderStatus +
                '}';
    }
}
