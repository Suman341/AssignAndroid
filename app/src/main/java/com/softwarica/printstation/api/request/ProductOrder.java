package com.softwarica.printstation.api.request;

public class ProductOrder {
    private long quantity;
    private String productId;

    public ProductOrder() {
    }

    public ProductOrder(int quantity, String productId) {
        this.quantity = quantity;
        this.productId = productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}