package com.example.shoppinglist.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.R;
import com.example.shoppinglist.adapters.ProductAdapter;
import com.example.shoppinglist.models.Product;
import com.example.shoppinglist.viewModels.ProductViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddEditShoppingListActivity extends AppCompatActivity {
    public static final String ID = "ID";
    public static final String SHOPPING_LIST_NAME = "SHOPPING_LIST_NAME";
    public static final String DATE = "DATE";
    public static final String IS_SHOWING_DIALOG = "IS_SHOWING_DIALOG";
    public static final int NEW_SHOPPING_LIST_ID = -2;
    public static final int ADD_PRODUCT_REQUEST = 1;
    public static final int EDIT_PRODUCT_REQUEST = 2;

    private EditText editTextListName;
    private TextView textViewDate;
    private RecyclerView recyclerView;
    private ProductViewModel productViewModel;
    private boolean isShowingDialog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_shopping_list);
        attributedWidget();
        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        final ProductAdapter adapter = new ProductAdapter();
        recycleViewService(adapter);

        setOnClickListenerDate();

        Intent intent = getIntent();
        intentService(adapter, intent);
        setOnClickListenerAddProduct(intent);
        if (savedInstanceState != null)
            savedInstanceStateService(savedInstanceState);
    }

    private void attributedWidget() {
        editTextListName = findViewById(R.id.editText_nameList);
        textViewDate = findViewById(R.id.textView_date);
        recyclerView = findViewById(R.id.recycleView_product);
    }

    private void recycleViewService(ProductAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void setOnClickListenerDate() {
        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });
    }

    private void showDateDialog() {
        int day = Integer.parseInt(textViewDate.getText().toString().substring(8, 10));
        int month = Integer.parseInt(textViewDate.getText().toString().substring(5, 7)) - 1;
        int year = Integer.parseInt(textViewDate.getText().toString().substring(0, 4));

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String date;
                if (day < 10 && month + 1 < 10)
                    date = year + "-0" + (month + 1) + "-0" + day;
                else if (day < 10)
                    date = year + "-" + (month + 1) + "-0" + day;
                else if (month + 1 < 10)
                    date = year + "-0" + (month + 1) + "-" + day;
                else
                    date = year + "-" + (month + 1) + "-" + day;
                isShowingDialog = false;
                textViewDate.setText(date.substring(0, 10));
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(
                AddEditShoppingListActivity.this, dateSetListener, year, month, day);
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
        isShowingDialog = true;
    }

    private void intentService(final ProductAdapter adapter, Intent intent) {
        if (intent.hasExtra(ID)) {
            setTitle("Edit shopping list");
            editTextListName.setText(intent.getStringExtra(SHOPPING_LIST_NAME));
            textViewDate.setText(intent.getStringExtra(DATE).substring(0, 10));
            int shoppingListId = intent.getIntExtra(ID, -1);
            if (shoppingListId != -1)
                setProductList(shoppingListId, adapter);
        } else {
            setTitle("Add shopping list");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            textViewDate.setText(sdf.format(new Date()));
            setProductList(NEW_SHOPPING_LIST_ID, adapter);
        }
    }

    private void setProductList(int shoppingListId, final ProductAdapter adapter) {
        productViewModel.getAllProduct(shoppingListId).observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                adapter.submitList(products);
            }
        });
        productItemOnClickListener(adapter);
        deleteProductItem(adapter);
    }

    public void productItemOnClickListener(ProductAdapter adapter) {
        adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                Intent intent = new Intent(AddEditShoppingListActivity.this, AddEditProductActivity.class);
                intent.putExtra(AddEditProductActivity.SHOPPING_LIST_ID, product.getShoppingListId());
                intent.putExtra(AddEditProductActivity.ID, product.getId());
                intent.putExtra(AddEditProductActivity.PRODUCT_NAME, product.getName());
                intent.putExtra(AddEditProductActivity.PRODUCT_NUMBER, product.getNumber());
                startActivityForResult(intent, EDIT_PRODUCT_REQUEST);
            }
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

    private void setOnClickListenerAddProduct(final Intent intent) {
        ImageView imageViewAddProduct = findViewById(R.id.imageView_addProduct);
        imageViewAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int shoppingListId;
                if (intent.hasExtra(ID)) {
                    shoppingListId = intent.getIntExtra(ID, -1);
                    if (shoppingListId == -1) {
                        Toast.makeText(AddEditShoppingListActivity.this,
                                "Error", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else
                    shoppingListId = NEW_SHOPPING_LIST_ID;

                Intent newIntent = new Intent(AddEditShoppingListActivity.this, AddEditProductActivity.class);
                newIntent.putExtra(AddEditProductActivity.SHOPPING_LIST_ID, shoppingListId);
                startActivityForResult(newIntent, ADD_PRODUCT_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_PRODUCT_REQUEST && resultCode == RESULT_OK && data != null) {
            String name = data.getStringExtra(AddEditProductActivity.PRODUCT_NAME);
            int number = data.getIntExtra(AddEditProductActivity.PRODUCT_NUMBER, -1);
            int shoppingListId = data.getIntExtra(AddEditProductActivity.SHOPPING_LIST_ID,
                    NEW_SHOPPING_LIST_ID - 1);

            if (name == null || name.trim().isEmpty() || number == -1 ||
                    shoppingListId == NEW_SHOPPING_LIST_ID - 1) {
                Toast.makeText(this, "Product can't added", Toast.LENGTH_SHORT).show();
                return;
            }

            Product product = new Product(name, number, shoppingListId);
            productViewModel.insertProduct(product);

            Toast.makeText(this, "Product saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_PRODUCT_REQUEST && resultCode == RESULT_OK && data != null) {
            int id = data.getIntExtra(AddEditProductActivity.ID, -1);

            if (id == -1) {
                Toast.makeText(this, "product can't updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = data.getStringExtra(AddEditProductActivity.PRODUCT_NAME);
            int number = data.getIntExtra(AddEditProductActivity.PRODUCT_NUMBER, -1);
            int shoppingListId = data.getIntExtra(AddEditProductActivity.SHOPPING_LIST_ID,
                    NEW_SHOPPING_LIST_ID - 1);

            if (name == null || name.trim().isEmpty() || number == -1 ||
                    shoppingListId == NEW_SHOPPING_LIST_ID - 1) {
                Toast.makeText(this, "Product can't updated", Toast.LENGTH_SHORT).show();
                return;
            }

            Product product = new Product(name, number, shoppingListId);
            product.setId(id);
            productViewModel.insertProduct(product);

            Toast.makeText(this, "Product updated", Toast.LENGTH_SHORT).show();
        }
    }

    private void savedInstanceStateService(Bundle savedInstanceState) {
        editTextListName.setText(savedInstanceState.getString(SHOPPING_LIST_NAME));
        if (savedInstanceState.getBoolean(IS_SHOWING_DIALOG))
            showDateDialog();
        else
            textViewDate.setText(savedInstanceState.getString(DATE).substring(0, 10));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_shopping_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveShoppingList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveShoppingList() {
        String name = editTextListName.getText().toString();
        String date = textViewDate.getText().toString() + " 23:59:59:999";

        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Please insert name list", Toast.LENGTH_LONG).show();
            return;
        }

        setResult(RESULT_OK, getIntentData(name, date));
        finish();
    }

    private Intent getIntentData(String name, String date) {
        Intent data = new Intent();
        data.putExtra(SHOPPING_LIST_NAME, name);
        data.putExtra(DATE, date);

        int id = getIntent().getIntExtra(ID, -1);
        if (id != -1)
            data.putExtra(ID, id);
        return data;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SHOPPING_LIST_NAME, editTextListName.getText().toString());
        outState.putString(DATE, textViewDate.getText().toString());
        outState.putBoolean(IS_SHOWING_DIALOG, isShowingDialog);
    }
}
