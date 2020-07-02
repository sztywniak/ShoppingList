package com.example.shoppinglist.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.shoppinglist.R;
import com.example.shoppinglist.adapters.ProductAdapter;
import com.example.shoppinglist.models.ShoppingList;
import com.example.shoppinglist.viewModels.ProductViewModel;
import com.google.gson.Gson;

public class ViewArchivedShoppingListActivity extends AppCompatActivity {
    public static final String SHOPPING_LIST = "SHOPPING_LIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_archived_shopping_list);
        final ProductAdapter adapter = new ProductAdapter();
        recycleViewService(adapter);
        ShoppingList shoppingList = getShoppingList();
        setValueOnView(shoppingList, adapter);
    }

    private void recycleViewService(ProductAdapter adapter) {
        RecyclerView recyclerView = findViewById(R.id.recycleView_product);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private ShoppingList getShoppingList() {
        Intent intent = getIntent();
        String json = intent.getStringExtra(SHOPPING_LIST);
        return new Gson().fromJson(json, ShoppingList.class);
    }

    private void setValueOnView(@NonNull ShoppingList shoppingList, ProductAdapter adapter) {
        TextView textViewName = findViewById(R.id.textView_name);
        TextView textViewDate = findViewById(R.id.textView_date);

        textViewName.setText(shoppingList.getName());
        textViewDate.setText(shoppingList.getDate().substring(0, 10));
        setProductList(shoppingList.getId(), adapter);
    }

    private void setProductList(int shoppingListId, final ProductAdapter adapter) {
        ProductViewModel productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getAllProduct(shoppingListId).observe(this, adapter::submitList);
    }
}
