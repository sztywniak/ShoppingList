package com.example.shoppinglist.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.shoppinglist.models.Product;
import com.example.shoppinglist.models.ShoppingList;

@Database(entities = {Product.class, ShoppingList.class}, version = 1)
public abstract class ShoppingListDatabase extends RoomDatabase {

    private static ShoppingListDatabase instance;

    public abstract ProductDao productDao();
    public abstract ShoppingListDao shoppingListDao();

    public static synchronized ShoppingListDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ShoppingListDatabase.class, "SHOPPING_LIST_DATABASE")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
