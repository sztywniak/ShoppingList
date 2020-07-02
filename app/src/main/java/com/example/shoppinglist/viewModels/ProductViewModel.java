package com.example.shoppinglist.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.shoppinglist.repositories.ProductRepository;
import com.example.shoppinglist.models.Product;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    private ProductRepository repository;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        repository = new ProductRepository(application);
    }

    public void insertProduct(Product product) {
        repository.insertProduct(product);
    }

    public void updateProduct(Product product) {
        repository.updateProduct(product);
    }

    public void deleteProduct(Product product) {
        repository.deleteProduct(product);
    }

    public LiveData<List<Product>> getAllProduct(int shoppingListId) {
        return repository.getAllProduct(shoppingListId);
    }
}
