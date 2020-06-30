package com.example.shoppinglist.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.shoppinglist.R;
import com.example.shoppinglist.adapters.ProductAdapter;
import com.example.shoppinglist.models.Product;
import com.example.shoppinglist.viewModels.ProductViewModel;

import java.util.List;

public class ViewArchivedShoppingListActivity extends AppCompatActivity {
    public static final String LIST_ID = "LIST_ID";
    public static final String LIST_NAME = ".LIST_NAME";
    public static final String LIST_DATE = "LIST_DATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_archived_shopping_list);
        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        final ProductAdapter adapter = new ProductAdapter();
        recycleViewService(adapter);
        Intent intent = getIntent();
        intentService(intent, adapter);
    }

    private void recycleViewService(ProductAdapter adapter) {
        RecyclerView recyclerView = findViewById(R.id.recycleView_product);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void intentService(@NonNull Intent intent, ProductAdapter adapter) {
        TextView textViewName = findViewById(R.id.textView_name);
        TextView textViewDate = findViewById(R.id.textView_date);

        textViewName.setText(intent.getStringExtra(LIST_NAME));
        textViewDate.setText(intent.getStringExtra(LIST_DATE).substring(0, 10));
        int shoppingListId = intent.getIntExtra(LIST_ID, -1);
        if (shoppingListId != -1)
            setProductList(shoppingListId, adapter);
    }

    private void setProductList(int shoppingListId, final ProductAdapter adapter) {
        ProductViewModel productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getAllProduct(shoppingListId).observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                adapter.submitList(products);
            }
        });
    }
}
