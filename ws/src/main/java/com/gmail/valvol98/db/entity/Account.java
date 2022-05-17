package com.gmail.valvol98.db.entity;

import java.io.Serializable;

/**
 * Account type according to database structure.
 *
 * @author V.Volovshchykov
 *
 */
public class Account implements Serializable {

    private static final long serialVersionUID = 4140891984102475827L;

    private int id;
    private int userId;
    private int managerId;
    private double payment;
    private String datetime;

    private String userName;

    public Account(int id, int userId, int managerId, double payment, String datetime, String userName) {
        this.id = id;
        this.userId = userId;
        this.managerId = managerId;
        this.payment = payment;
        this.datetime = datetime;
        this.userName = userName;
    }

    public Account() {
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getManagerId() {
        return managerId;
    }

    public double getPayment() {
        return payment;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getUserName() {
        return userName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public void setPaymant(double payment) {
        this.payment = payment;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userId=" + userId +
                ", managerId=" + managerId +
                ", paymant=" + payment +
                ", datetime='" + datetime + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
