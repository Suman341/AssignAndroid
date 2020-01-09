package com.softwarica.printstation.api.request;

import java.util.List;

public class ProductOrderRequest {
    private List<ProductOrder> orders;

    public ProductOrderRequest() {
    }

    public ProductOrderRequest(List<ProductOrder> orders) {
        this.orders = orders;
    }

    public List<ProductOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<ProductOrder> orders) {
        this.orders = orders;
    }


}
