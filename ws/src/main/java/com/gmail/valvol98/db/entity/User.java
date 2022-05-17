package com.gmail.valvol98.db.entity;

import java.io.Serializable;

/**
 * User type according to database structure.
 *
 * @author V.Volovshchykov
 *
 */
public class User implements Serializable {

    private static final long serialVersionUID = 4026388527989719614L;

    private int id;
    private String login;
    private String password;
    private String name;
    private int role_id;
    private String role;
    private double gerenalActiveMoneyOnAccount;

    public User(int id, String login, String password, String name, int role_id, String role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.role_id = role_id;
        this.role = role;
    }

    public User(int id, String name, double gerenalActiveMoneyOnAccount) {
        this.id = id;
        this.name = name;
        this.gerenalActiveMoneyOnAccount = gerenalActiveMoneyOnAccount;
    }

    public User(User user) {
        this.id = user.id;
        this.login = user.login;
        this.password = null;
        this.name = user.name;
        this.role_id = user.role_id;
        this.role = user.role;
    }

    public User() {}

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public int getRole_id() {
        return role_id;
    }

    public String getRole() {
        return role;
    }

    public double getGerenalActiveMoneyOnAccount() {
        return gerenalActiveMoneyOnAccount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setGerenalActiveMoneyOnAccount(double gerenalActiveMoneyOnAccount) {
        this.gerenalActiveMoneyOnAccount = gerenalActiveMoneyOnAccount;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", role_id=" + role_id +
                ", role='" + role + '\'' +
                '}';
    }
}
