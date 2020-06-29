package com.example.shoppinglist.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.shoppinglist.R;
import com.example.shoppinglist.adapters.ShoppingListAdapter;
import com.example.shoppinglist.models.ShoppingList;
import com.example.shoppinglist.viewModels.ShoppingListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {
    public static final String EXTRA_TYPE = ".EXTRA_TYPE";
    public static final String CURRENT_SHOPPING_LIST = "current_shopping_list";
    public static final String ARCHIVED_SHOPPING_LIST = "archived_shopping_list";
    public static final int ADD_LIST_REQUEST = 1;
    public static final int EDIT_LIST_REQUEST = 2;

    private ShoppingListViewModel shoppingListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        RecyclerView recyclerView = findViewById(R.id.recycleView_shoppingList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        final ShoppingListAdapter adapter = new ShoppingListAdapter();
        recyclerView.setAdapter(adapter);

        shoppingListViewModel = new ViewModelProvider(this).get(ShoppingListViewModel.class);

        String typeIntent = getIntent().getStringExtra(EXTRA_TYPE);
        selectList(typeIntent, adapter, recyclerView);
    }

    private void selectList(String typeIntent, final ShoppingListAdapter adapter, RecyclerView recyclerView) {
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
                Intent intent = new Intent(ShoppingListActivity.this, AddEditShoppingList.class);
                startActivityForResult(intent, ADD_LIST_REQUEST);
            }
        });
    }

    public void deleteItem(final ShoppingListAdapter adapter, RecyclerView recyclerView) {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
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
                Intent intent = new Intent(ShoppingListActivity.this, AddEditShoppingList.class);
                intent.putExtra(AddEditShoppingList.EXTRA_ID, shoppingList.getId());
                intent.putExtra(AddEditShoppingList.EXTRA_NAME, shoppingList.getName());
                intent.putExtra(AddEditShoppingList.EXTRA_DATE, shoppingList.getDate());
                intent.putExtra(AddEditShoppingList.EXTRA_PRODUCT_LIST, shoppingList.getProductList());

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
                intent.putExtra(ViewArchivedShoppingListActivity.EXTRA_ID, shoppingList.getId());
                intent.putExtra(ViewArchivedShoppingListActivity.EXTRA_NAME, shoppingList.getName());
                intent.putExtra(ViewArchivedShoppingListActivity.EXTRA_DATE, shoppingList.getDate());
                intent.putExtra(ViewArchivedShoppingListActivity.EXTRA_PRODUCT_LIST, shoppingList.getProductList());

                startActivityForResult(intent, EDIT_LIST_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_LIST_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(AddEditShoppingList.EXTRA_NAME);
            String date = data.getStringExtra(AddEditShoppingList.EXTRA_DATE);
            String product = data.getStringExtra(AddEditShoppingList.EXTRA_PRODUCT_LIST);

            ShoppingList shoppingList = new ShoppingList(name, date, product);
            shoppingListViewModel.insertShoppingList(shoppingList);

            Toast.makeText(this, "Shopping list saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_LIST_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditShoppingList.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Shopping list can't updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = data.getStringExtra(AddEditShoppingList.EXTRA_NAME);
            String date = data.getStringExtra(AddEditShoppingList.EXTRA_DATE);
            String product = data.getStringExtra(AddEditShoppingList.EXTRA_PRODUCT_LIST);

            ShoppingList shoppingList = new ShoppingList(name, date, product);
            shoppingList.setId(id);
            shoppingListViewModel.updateShoppingLis(shoppingList);

            Toast.makeText(this, "Shopping list updated", Toast.LENGTH_SHORT).show();
        }
    }
}