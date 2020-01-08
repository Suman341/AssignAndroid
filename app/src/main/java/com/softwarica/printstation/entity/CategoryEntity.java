package com.softwarica.printstation.entity;

import com.google.gson.annotations.Expose;


public class CategoryEntity {

    @Expose
    private String _id;
    @Expose
    private String category;
    @Expose
    private String createdAt;



    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
