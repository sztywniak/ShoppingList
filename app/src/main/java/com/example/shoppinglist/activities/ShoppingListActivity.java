package com.example.shoppinglist.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.shoppinglist.R;
import com.example.shoppinglist.adapters.ShoppingListAdapter;
import com.example.shoppinglist.models.Product;
import com.example.shoppinglist.models.ShoppingList;
import com.example.shoppinglist.viewModels.ProductViewModel;
import com.example.shoppinglist.viewModels.ShoppingListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {
    public static final String TYPE = "type";
    public static final String CURRENT_SHOPPING_LIST = "current_shopping_list";
    public static final String ARCHIVED_SHOPPING_LIST = "archived_shopping_list";
    public static final int ADD_LIST_REQUEST = 1;
    public static final int EDIT_LIST_REQUEST = 2;

    private ShoppingListViewModel shoppingListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        recycleVieService();
        shoppingListViewModel = new ViewModelProvider(this).get(ShoppingListViewModel.class);
    }

    private void recycleVieService() {
        RecyclerView recyclerView = findViewById(R.id.recycleView_shoppingList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        final ShoppingListAdapter adapter = new ShoppingListAdapter();
        recyclerView.setAdapter(adapter);
        selectList(adapter, recyclerView);
    }

    private void selectList(final ShoppingListAdapter adapter, RecyclerView recyclerView) {
        String typeIntent = getIntent().getStringExtra(TYPE);
        if (typeIntent != null)
            switch (typeIntent) {
                case CURRENT_SHOPPING_LIST:
                    serviceCurrentShoppingList(adapter, recyclerView);
                    break;
                case ARCHIVED_SHOPPING_LIST:
                    serviceArchivedShoppingList(adapter);
                    break;
            }
        else
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }

    public void serviceCurrentShoppingList(final ShoppingListAdapter adapter, RecyclerView recyclerView) {
        viewAddShoppingListButton();
        setTitle("Current shopping list");
        shoppingListViewModel.getAllCurrentShoppingList().observe(this, new Observer<List<ShoppingList>>() {
            @Override
            public void onChanged(List<ShoppingList> shoppingLists) {
                adapter.submitList(shoppingLists);
            }
        });
        deleteItem(adapter, recyclerView);
        currentItemOnClickListener(adapter);
    }

    public void viewAddShoppingListButton() {
        FloatingActionButton buttonAddShoppingList = findViewById(R.id.button_add_shopping_list);
        buttonAddShoppingList.setVisibility(View.VISIBLE);
        buttonAddShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingListActivity.this, AddEditShoppingListActivity.class);
                startActivityForResult(intent, ADD_LIST_REQUEST);
            }
        });
    }

    public void deleteItem(final ShoppingListAdapter adapter, RecyclerView recyclerView) {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                shoppingListViewModel.deleteShoppingList(
                        adapter.getShoppingListAt(viewHolder.getAdapterPosition()));
                Toast.makeText(ShoppingListActivity.this,
                        "Shopping list deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    public void currentItemOnClickListener(ShoppingListAdapter adapter) {
        adapter.setOnItemClickListener(new ShoppingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ShoppingList shoppingList) {
                Intent intent = new Intent(ShoppingListActivity.this, AddEditShoppingListActivity.class);
                intent.putExtra(AddEditShoppingListActivity.ID, shoppingList.getId());
                intent.putExtra(AddEditShoppingListActivity.SHOPPING_LIST_NAME, shoppingList.getName());
                intent.putExtra(AddEditShoppingListActivity.DATE, shoppingList.getDate());

                startActivityForResult(intent, EDIT_LIST_REQUEST);
            }
        });
    }

    public void serviceArchivedShoppingList(final ShoppingListAdapter adapter) {
        setTitle("Archived shopping list");
        shoppingListViewModel.getAllArchivedShoppingList().observe(this, new Observer<List<ShoppingList>>() {
            @Override
            public void onChanged(List<ShoppingList> shoppingLists) {
                adapter.submitList(shoppingLists);
            }
        });
        archivedItemOnClickListener(adapter);
    }

    public void archivedItemOnClickListener(ShoppingListAdapter adapter) {
        adapter.setOnItemClickListener(new ShoppingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ShoppingList shoppingList) {
                Intent intent = new Intent(ShoppingListActivity.this, ViewArchivedShoppingListActivity.class);
                intent.putExtra(ViewArchivedShoppingListActivity.LIST_ID, shoppingList.getId());
                intent.putExtra(ViewArchivedShoppingListActivity.LIST_NAME, shoppingList.getName());
                intent.putExtra(ViewArchivedShoppingListActivity.LIST_DATE, shoppingList.getDate());

                startActivityForResult(intent, EDIT_LIST_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_LIST_REQUEST && resultCode == RESULT_OK && data != null) {
            String name = data.getStringExtra(AddEditShoppingListActivity.SHOPPING_LIST_NAME);
            String date = data.getStringExtra(AddEditShoppingListActivity.DATE);

            ShoppingList shoppingList = new ShoppingList(name, date);
            long id = shoppingListViewModel.insertShoppingList(shoppingList);

            updateProduct(id);
            Toast.makeText(this, "Shopping list saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_LIST_REQUEST && resultCode == RESULT_OK && data != null) {
            int id = data.getIntExtra(AddEditShoppingListActivity.ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Shopping list can't updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = data.getStringExtra(AddEditShoppingListActivity.SHOPPING_LIST_NAME);
            String date = data.getStringExtra(AddEditShoppingListActivity.DATE);

            ShoppingList shoppingList = new ShoppingList(name, date);
            shoppingList.setId(id);
            shoppingListViewModel.updateShoppingLis(shoppingList);

            Toast.makeText(this, "Shopping list updated", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProduct(Long shoppingListId) {
        ProductViewModel productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        LiveData<List<Product>> productList = productViewModel
                .getAllProduct(AddEditShoppingListActivity.NEW_SHOPPING_LIST_ID);
        if (productList.getValue() != null) {
            Log.e("productList", productList.getValue().size() + "");
            for (int i = 0; i < productList.getValue().size(); i++) {
                productList.getValue().get(i).setShoppingListId(shoppingListId.intValue());
                productViewModel.updateProduct(productList.getValue().get(i));
            }
        }
    }
}