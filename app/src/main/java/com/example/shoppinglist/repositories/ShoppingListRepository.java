package com.example.shoppinglist.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.shoppinglist.database.ShoppingListDao;
import com.example.shoppinglist.database.ShoppingListDatabase;
import com.example.shoppinglist.models.ShoppingList;

import java.util.List;

public class ShoppingListRepository {

    private ShoppingListDao shoppingListDao;
    private LiveData<List<ShoppingList>> allArchivedShoppingList;
    private LiveData<List<ShoppingList>> allCurrentShoppingList;

    public ShoppingListRepository(Application application) {
        ShoppingListDatabase database = ShoppingListDatabase.getInstance(application);
        shoppingListDao = database.shoppingListDao();
        allCurrentShoppingList = shoppingListDao.getAllCurrentShoppingList();
        allArchivedShoppingList = shoppingListDao.getAllArchivedShoppingList();
    }

    public void insertShoppingList(ShoppingList shoppingList) {
        new InsertShoppingListAsyncTask(shoppingListDao).execute(shoppingList);
    }

    public void updateShoppingList(ShoppingList shoppingList) {
        new UpdateShoppingListAsyncTask(shoppingListDao).execute(shoppingList);
    }

    public void deleteShoppingList(ShoppingList shoppingList) {
        new DeleteShoppingListAsyncTask(shoppingListDao).execute(shoppingList);
    }

    public LiveData<List<ShoppingList>> getAllCurrentShoppingList() {
        return allCurrentShoppingList;
    }

    public LiveData<List<ShoppingList>> getAllArchivedShoppingList() {
        return allArchivedShoppingList;
    }

    private static class InsertShoppingListAsyncTask extends AsyncTask<ShoppingList, Void, Void> {
        private ShoppingListDao shoppingListDao;

        private InsertShoppingListAsyncTask(ShoppingListDao shoppingListDao) {
            this.shoppingListDao = shoppingListDao;
        }

        @Override
        protected Void doInBackground(ShoppingList... shoppingLists) {
            shoppingListDao.insertSoppingList(shoppingLists[0]);
            return null;
        }
    }

    private static class UpdateShoppingListAsyncTask extends AsyncTask<ShoppingList, Void, Void> {
        private ShoppingListDao shoppingListDao;

        private UpdateShoppingListAsyncTask(ShoppingListDao shoppingListDao) {
            this.shoppingListDao = shoppingListDao;
        }

        @Override
        protected Void doInBackground(ShoppingList... shoppingLists) {
            shoppingListDao.updateShoppingList(shoppingLists[0]);
            return null;
        }
    }

    private static class DeleteShoppingListAsyncTask extends AsyncTask<ShoppingList, Void, Void> {
        private ShoppingListDao shoppingListDao;

        private DeleteShoppingListAsyncTask(ShoppingListDao shoppingListDao) {
            this.shoppingListDao = shoppingListDao;
        }

        @Override
        protected Void doInBackground(ShoppingList... shoppingLists) {
            shoppingListDao.deleteShoppingList(shoppingLists[0]);
            return null;
        }
    }
}
