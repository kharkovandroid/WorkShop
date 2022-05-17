package com.gmail.valvol98.db.entity;

import java.io.Serializable;

/**
 * Status type according to database structure.
 *
 * @author V.Volovshchykov
 *
 */
public class Status implements Serializable {

    private static long serialVersionUID = 2438156187413058787L;

    private int id;
    private String type;

    public Status(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Status{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
