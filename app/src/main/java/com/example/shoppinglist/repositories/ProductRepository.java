package com.example.shoppinglist.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.shoppinglist.database.ProductDao;
import com.example.shoppinglist.database.ShoppingListDatabase;
import com.example.shoppinglist.models.Product;

import java.util.List;

public class ProductRepository {

    private ProductDao productDao;
    private LiveData<List<Product>> allProduct;

    public ProductRepository(Application application, int shoppingListId) {
        ShoppingListDatabase database = ShoppingListDatabase.getInstance(application);
        productDao = database.productDao();
        allProduct = productDao.getAllProduct(shoppingListId);
    }

    public void insertProduct(Product product) {
        new InsertProductAsyncTask(productDao).execute(product);
    }

    public void updateProduct(Product product) {
        new UpdateProductAsyncTask(productDao).execute(product);
    }

    public void deleteProduct(Product product) {
        new DeleteProductAsyncTask(productDao).execute(product);
    }

    public LiveData<List<Product>> getAllProduct() {
        return allProduct;
    }

    private static class InsertProductAsyncTask extends AsyncTask<Product, Void, Void> {
        private ProductDao productDao;

        private InsertProductAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected Void doInBackground(Product... products) {
            productDao.insertProduct(products[0]);
            return null;
        }
    }

    private static class UpdateProductAsyncTask extends AsyncTask<Product, Void, Void> {
        private ProductDao productDao;

        private UpdateProductAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected Void doInBackground(Product... products) {
            productDao.updateProduct(products[0]);
            return null;
        }
    }

    private static class DeleteProductAsyncTask extends AsyncTask<Product, Void, Void> {
        private ProductDao productDao;

        private DeleteProductAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected Void doInBackground(Product... products) {
            productDao.deleteProduct(products[0]);
            return null;
        }
    }
}
