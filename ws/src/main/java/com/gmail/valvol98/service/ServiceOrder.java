package com.gmail.valvol98.service;

import com.gmail.valvol98.db.DBException;
import com.gmail.valvol98.db.entity.Order;
import com.gmail.valvol98.repositories.AllOrder;

import java.util.ArrayList;
import java.util.List;

public class ServiceOrder {

    private static ServiceOrder instance;

    public static synchronized ServiceOrder getInstance() throws DBException {
        if (instance == null) {
            instance = new ServiceOrder();
        }
        return instance;
    }

    public List<Order> serviceFindAllOrder() throws DBException {
        return new AllOrder().findAllOrder();
    }
}
