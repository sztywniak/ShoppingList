package com.example.shoppinglist.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.shoppinglist.models.Product;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert
    void insertProduct(Product product);

    @Update
    void updateProduct(Product product);

    @Delete
    void deleteProduct(Product product);

    @Query("SELECT * FROM PRODUCT WHERE SHOPPING_LIST_ID = :likeShoppingListId ORDER BY NAME")
    LiveData<List<Product>> getAllProduct(int likeShoppingListId);
}
