package com.example.shoppinglist.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "PRODUCT",
        foreignKeys = @ForeignKey(entity = ShoppingList.class, parentColumns = "ID",
                childColumns = "SHOPPING_LIST_ID", onDelete = CASCADE))
public class Product {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    private int id;

    @ColumnInfo(name = "NAME")
    private String name;

    @ColumnInfo(name = "NUMBER")
    private int number;

    @ColumnInfo(name = "SHOPPING_LIST_ID")
    private int shoppingListId;

    public Product(String name, int number, int shoppingListId) {
        this.name = name;
        this.number = number;
        this.shoppingListId = shoppingListId;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(int shoppingListId) {
        this.shoppingListId = shoppingListId;
    }
}
