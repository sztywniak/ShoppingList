package com.example.shoppinglist.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "SHOPPING_LIST")
public class ShoppingList {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    private int id;

    @ColumnInfo(name = "NAME")
    private String name;

    @ColumnInfo(name = "DATE")
    private String date;

    @ColumnInfo(name = "PRODUCT_LIST")
    private String productList;

    public ShoppingList(String name, String date, String productList) {
        this.name = name;
        this.date = date;
        this.productList = productList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProductList() {
        return productList;
    }

    public void setProductList(String productList) {
        this.productList = productList;
    }
}
