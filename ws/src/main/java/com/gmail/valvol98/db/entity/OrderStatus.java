package com.gmail.valvol98.db.entity;

import java.io.Serializable;

/**
 * Order status type according to database structure.
 *
 * @author V.Volovshchykov
 *
 */
public class OrderStatus implements Serializable {

    private final static long serialVersionUID = -6286251466659777485L;

    private int orderId;
    private int statusId;
    private int managerForemanId;
    private String datetime;

    private String statusType;

    public OrderStatus(int orderId, int statusId, int managerForemanId, String datetime, String statusType) {
        this.orderId = orderId;
        this.statusId = statusId;
        this.managerForemanId = managerForemanId;
        this.datetime = datetime;
        this.statusType = statusType;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getStatusId() {
        return statusId;
    }

    public int getManagerForemanId() {
        return managerForemanId;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public void setManagerForemanId(int managerForemanId) {
        this.managerForemanId = managerForemanId;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    @Override
    public String toString() {
        return "OrderStatus{" +
                "orderId=" + orderId +
                ", statusId=" + statusId +
                ", managerForemanId=" + managerForemanId +
                ", datetime='" + datetime + '\'' +
                ", statusType='" + statusType + '\'' +
                '}';
    }
}
