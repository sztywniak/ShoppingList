package com.example.shoppinglist.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.shoppinglist.models.ShoppingList;

import java.util.List;

@Dao
public interface ShoppingListDao {

    @Insert
    long insertShoppingList(ShoppingList shoppingList);

    @Update
    void updateShoppingList(ShoppingList shoppingList);

    @Delete
    void deleteShoppingList(ShoppingList shoppingList);

    @Query("SELECT * FROM SHOPPING_LIST WHERE DATE >= (SELECT datetime('now', 'localtime'))" +
            "AND NAME NOT LIKE '' ORDER BY DATE")
    LiveData<List<ShoppingList>> getAllCurrentShoppingList();

    @Query("SELECT * FROM SHOPPING_LIST WHERE DATE < (SELECT datetime('now', 'localtime'))" +
            "AND NAME NOT LIKE '' ORDER BY DATE DESC")
    LiveData<List<ShoppingList>> getAllArchivedShoppingList();

    @Query("DELETE FROM SHOPPING_LIST WHERE NAME LIKE ''")
    void deleteEmptyShoppingList();
}
