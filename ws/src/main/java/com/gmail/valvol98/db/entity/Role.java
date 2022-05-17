package com.gmail.valvol98.db.entity;

import java.io.Serializable;

/**
 * Role type according to database structure.
 *
 * @author V.Volovshchykov
 *
 */
public class Role implements Serializable {

    private static final long serialVersionUID = 4140891984102475827L;

    private int id;
    private String role;

    public Role(int id, String role) {
        this.id = id;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role='" + role + '\'' +
                '}';
    }
}
