package com.example.shoppinglist.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.R;
import com.example.shoppinglist.adapters.ProductAdapter;
import com.example.shoppinglist.models.Product;
import com.example.shoppinglist.models.ShoppingList;
import com.example.shoppinglist.viewModels.ProductViewModel;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddEditShoppingListActivity extends AppCompatActivity {
    public static final String SHOPPING_LIST = "SHOPPING_LIST";
    public static final String IS_SHOWING_DIALOG = "IS_SHOWING_DIALOG";
    public static final int ADD_PRODUCT_REQUEST = 1;
    public static final int EDIT_PRODUCT_REQUEST = 2;

    final String SHOPPING_LIST_NAME = "SHOPPING_LIST_NAME";
    final String SHOPPING_DATE = "SHOPPING_DATE";

    private ProductViewModel productViewModel;
    private boolean isShowingDialog = false;
    private EditText editTextShoppingListName;
    private TextView textViewShoppingDate;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_shopping_list);
        attributedWidget();
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        final ProductAdapter adapter = new ProductAdapter();
        recycleViewService(adapter);
        ShoppingList shoppingList = getShoppingList();
        if (shoppingList == null)
            finish();
        else {
            setValueOnView(shoppingList, adapter);
            setOnClickListenerDate();
            setOnClickListenerAddProduct(shoppingList.getId());
            if (savedInstanceState != null)
                savedInstanceStateService(savedInstanceState);
        }
    }

    private void attributedWidget() {
        editTextShoppingListName = findViewById(R.id.editText_nameList);
        textViewShoppingDate = findViewById(R.id.textView_date);
        recyclerView = findViewById(R.id.recycleView_product);
    }

    private void recycleViewService(ProductAdapter adapter) {
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
        editTextShoppingListName.setText(shoppingList.getName());
        textViewShoppingDate.setText(shoppingList.getDate().substring(0, 10));
        setProductList(shoppingList.getId(), adapter);
    }

    private void setProductList(int shoppingListId, final ProductAdapter adapter) {
        productViewModel.getAllProduct(shoppingListId).observe(this, adapter::submitList);
        productItemOnClickListener(adapter);
        deleteProductItem(adapter);
    }

    public void productItemOnClickListener(ProductAdapter adapter) {
        adapter.setOnItemClickListener(product -> {
            Intent intent = new Intent(AddEditShoppingListActivity.this,
                    AddEditProductActivity.class);
            String json = new Gson().toJson(product);
            intent.putExtra(AddEditProductActivity.PRODUCT, json);
            startActivityForResult(intent, EDIT_PRODUCT_REQUEST);
        });
    }

    public void deleteProductItem(final ProductAdapter adapter) {
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
                productViewModel.deleteProduct(adapter.getProductAt(viewHolder.getAdapterPosition()));
                Toast.makeText(AddEditShoppingListActivity.this,
                        "Product deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void setOnClickListenerDate() {
        textViewShoppingDate.setOnClickListener(view -> showDateDialog());
    }

    private void showDateDialog() {
        String date = textViewShoppingDate.getText().toString();
        int day = Integer.parseInt(date.substring(8, 10));
        int month = Integer.parseInt(date.substring(5, 7)) - 1;
        int year = Integer.parseInt(date.substring(0, 4));

        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year1, month1, day1) -> {
            String selectedDate;
            if (day1 < 10 && month1 + 1 < 10)
                selectedDate = year1 + "-0" + (month1 + 1) + "-0" + day1;
            else if (day1 < 10)
                selectedDate = year1 + "-" + (month1 + 1) + "-0" + day1;
            else if (month1 + 1 < 10)
                selectedDate = year1 + "-0" + (month1 + 1) + "-" + day1;
            else
                selectedDate = year1 + "-" + (month1 + 1) + "-" + day1;
            isShowingDialog = false;
            textViewShoppingDate.setText(selectedDate.substring(0, 10));
        };

        DatePickerDialog dialog = new DatePickerDialog(
                AddEditShoppingListActivity.this, dateSetListener, year, month, day);
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
        isShowingDialog = true;
    }

    private void setOnClickListenerAddProduct(int shoppingListId) {
        ImageView imageViewAddProduct = findViewById(R.id.imageView_addProduct);
        imageViewAddProduct.setOnClickListener(view -> transitionToAddProduct(shoppingListId));
    }

    private void transitionToAddProduct(int shoppingListId) {
        Intent intent = new Intent(AddEditShoppingListActivity.this,
                AddEditProductActivity.class);
        Product product = new Product("", 0, shoppingListId);
        String json = new Gson().toJson(product);
        intent.putExtra(AddEditProductActivity.PRODUCT, json);
        startActivityForResult(intent, ADD_PRODUCT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_PRODUCT_REQUEST && resultCode == RESULT_OK && data != null) {
            productViewModel.insertProduct(getProduct(data));
            Toast.makeText(this, "Product saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_PRODUCT_REQUEST && resultCode == RESULT_OK && data != null) {
            productViewModel.updateProduct(getProduct(data));
            Toast.makeText(this, "Product updated", Toast.LENGTH_SHORT).show();
        }
    }

    private Product getProduct(@NonNull Intent data) {
        String json = data.getStringExtra(AddEditProductActivity.PRODUCT);
        return new Gson().fromJson(json, Product.class);
    }

    private void savedInstanceStateService(Bundle savedInstanceState) {
        editTextShoppingListName.setText(savedInstanceState.getString(SHOPPING_LIST_NAME));
        textViewShoppingDate.setText(savedInstanceState.getString(SHOPPING_DATE));
        if (savedInstanceState.getBoolean(IS_SHOWING_DIALOG))
            showDateDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_shopping_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save) {
            saveShoppingList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveShoppingList() {
        String name = editTextShoppingListName.getText().toString();
        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Please insert name list", Toast.LENGTH_LONG).show();
            return;
        }
        String date = textViewShoppingDate.getText().toString() + " 23:59:59:999";
        setResult(RESULT_OK, getIntentData(new ShoppingList(name, date)));
        finish();
    }

    private Intent getIntentData(ShoppingList shoppingList) {
        shoppingList.setId(getShoppingList().getId());
        String json = new Gson().toJson(shoppingList);
        Intent data = new Intent();
        data.putExtra(SHOPPING_LIST, json);
        return data;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SHOPPING_LIST_NAME, editTextShoppingListName.getText().toString());
        outState.putString(SHOPPING_DATE, textViewShoppingDate.getText().toString());
        outState.putBoolean(IS_SHOWING_DIALOG, isShowingDialog);
    }
}
