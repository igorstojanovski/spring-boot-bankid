package com.auth0.samples.authapi.springbootauthupdated.model;

public class CollectRequest implements BankIdEntity {
    private String orderRef;

    public String getOrderRef() {
        return orderRef;
    }

    public void setOrderRef(String orderRef) {
        this.orderRef = orderRef;
    }
}
