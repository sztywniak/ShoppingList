package com.example.shoppinglist.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.shoppinglist.models.ShoppingList;
import com.example.shoppinglist.repositories.ShoppingListRepository;

import java.util.List;

public class ShoppingListViewModel extends AndroidViewModel {
    private ShoppingListRepository repository;
    private LiveData<List<ShoppingList>> allCurrentShoppingList;
    private LiveData<List<ShoppingList>> allArchivedShoppingList;

    public ShoppingListViewModel(@NonNull Application application) {
        super(application);
        repository = new ShoppingListRepository(application);
        allCurrentShoppingList = repository.getAllCurrentShoppingList();
        allArchivedShoppingList = repository.getAllArchivedShoppingList();
    }

    public void insertShoppingList(ShoppingList shoppingList) {
        repository.insertShoppingList(shoppingList);
    }

    public void updateShoppingLis(ShoppingList shoppingList) {
        repository.updateShoppingList(shoppingList);
    }

    public void deleteShoppingList(ShoppingList shoppingList) {
        repository.deleteShoppingList(shoppingList);
    }

    public LiveData<List<ShoppingList>> getAllCurrentShoppingList() {
        return allCurrentShoppingList;
    }

    public LiveData<List<ShoppingList>> getAllArchivedShoppingList() {
        return allArchivedShoppingList;
    }
}