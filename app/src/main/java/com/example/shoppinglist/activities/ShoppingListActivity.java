package com.example.shoppinglist.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.shoppinglist.models.ShoppingList;
import com.example.shoppinglist.viewModels.ShoppingListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        shoppingListViewModel = new ViewModelProvider(this).get(ShoppingListViewModel.class);
        //shoppingListViewModel.deleteEmptyShoppingList();
        recycleVieService();
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
        else //TODO tu jest lipa
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }

    public void serviceCurrentShoppingList(final ShoppingListAdapter adapter, RecyclerView recyclerView) {
        addShoppingListButtonService();
        setTitle("Current shopping list");
        shoppingListViewModel.getAllCurrentShoppingList().observe(this, adapter::submitList);
        deleteItem(adapter, recyclerView);
        currentItemOnClickListener(adapter);
    }

    public void addShoppingListButtonService() {
        FloatingActionButton buttonAddShoppingList = findViewById(R.id.button_add_shopping_list);
        buttonAddShoppingList.setVisibility(View.VISIBLE);
        buttonAddShoppingList.setOnClickListener(view -> insertNewShoppingList());
    }

    private void insertNewShoppingList() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        ShoppingList shoppingList = new ShoppingList("", sdf.format(new Date()));
        shoppingListViewModel.getInsertionId().observe(this, aLong -> {
            shoppingList.setId(aLong.intValue());
            String json = new Gson().toJson(shoppingList);
            Intent intent = new Intent(ShoppingListActivity.this, AddEditShoppingListActivity.class);
            intent.putExtra(AddEditShoppingListActivity.SHOPPING_LIST, json);
            startActivityForResult(intent, ADD_LIST_REQUEST);
        });
        shoppingListViewModel.insertShoppingList(shoppingList);
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
        adapter.setOnItemClickListener(shoppingList -> {
            Intent intent = new Intent(ShoppingListActivity.this, AddEditShoppingListActivity.class);
            String json = new Gson().toJson(shoppingList);
            intent.putExtra(AddEditShoppingListActivity.SHOPPING_LIST, json);
            startActivityForResult(intent, EDIT_LIST_REQUEST);
        });
    }

    public void serviceArchivedShoppingList(final ShoppingListAdapter adapter) {
        setTitle("Archived shopping list");
        shoppingListViewModel.getAllArchivedShoppingList().observe(this, adapter::submitList);
        archivedItemOnClickListener(adapter);
    }

    public void archivedItemOnClickListener(ShoppingListAdapter adapter) {
        adapter.setOnItemClickListener(shoppingList -> {
            Intent intent = new Intent(ShoppingListActivity.this, ViewArchivedShoppingListActivity.class);
            String json = new Gson().toJson(shoppingList);
            intent.putExtra(ViewArchivedShoppingListActivity.SHOPPING_LIST, json);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_LIST_REQUEST && resultCode == RESULT_OK && data != null) {
            getDataAndUpdateShoppingList(data);
            Toast.makeText(this, "Shopping list saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_LIST_REQUEST && resultCode == RESULT_OK && data != null) {
            getDataAndUpdateShoppingList(data);
            Toast.makeText(this, "Shopping list updated", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDataAndUpdateShoppingList(@NonNull Intent data) {
        String json = data.getStringExtra(AddEditShoppingListActivity.SHOPPING_LIST);
        ShoppingList shoppingList = new Gson().fromJson(json, ShoppingList.class);
        Log.e("lista", "id " + shoppingList.getId() +" name "+ shoppingList.getName() + " date " + shoppingList.getDate());
        shoppingListViewModel.updateShoppingLis(shoppingList);
    }
}