package co.igorski.model.bankid.collect;

import co.igorski.model.bankid.BankIdEntity;

public class CollectRequest implements BankIdEntity {
    private String orderRef;

    public String getOrderRef() {
        return orderRef;
    }

    public void setOrderRef(String orderRef) {
        this.orderRef = orderRef;
    }
}
