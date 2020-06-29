package com.example.shoppinglist.repositories;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.shoppinglist.database.ShoppingListDao;
import com.example.shoppinglist.database.ShoppingListDatabase;
import com.example.shoppinglist.models.ShoppingList;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ShoppingListRepository {

    private ShoppingListDao shoppingListDao;
    private LiveData<List<ShoppingList>> allArchivedShoppingList;
    private LiveData<List<ShoppingList>> allCurrentShoppingList;
    private long id;

    public ShoppingListRepository(Application application) {
        ShoppingListDatabase database = ShoppingListDatabase.getInstance(application);
        shoppingListDao = database.shoppingListDao();
        allCurrentShoppingList = shoppingListDao.getAllCurrentShoppingList();
        allArchivedShoppingList = shoppingListDao.getAllArchivedShoppingList();
    }

    public long insertShoppingList(ShoppingList shoppingList) throws ExecutionException, InterruptedException {
        new InsertShoppingListAsyncTask(shoppingListDao,
                new InsertShoppingListAsyncTask.AsyncResponse() {
                    @Override
                    public void processFinish(long output) {
                        id = output;
                        Log.e("idi", id +"");
                    }
                }).execute(shoppingList).get();
        //TODO poprawić
        Log.e("ididid", id +"");
        return id;
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

    private static class InsertShoppingListAsyncTask extends AsyncTask<ShoppingList, Void, Long> {
        private ShoppingListDao shoppingListDao;
        public AsyncResponse delegate;

        private InsertShoppingListAsyncTask(ShoppingListDao shoppingListDao, AsyncResponse delegate) {
            this.shoppingListDao = shoppingListDao;
            this.delegate = delegate;
        }

        @Override
        protected Long doInBackground(ShoppingList... shoppingLists) {
            return shoppingListDao.insertShoppingList(shoppingLists[0]);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            delegate.processFinish(aLong);
        }

        public interface AsyncResponse {
            void processFinish(long output);
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
