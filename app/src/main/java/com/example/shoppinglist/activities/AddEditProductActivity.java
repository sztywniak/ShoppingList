package com.example.shoppinglist.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shoppinglist.R;
import com.example.shoppinglist.models.Product;
import com.google.gson.Gson;

public class AddEditProductActivity extends AppCompatActivity {
    public static final String PRODUCT = "PRODUCT";

    final String PRODUCT_NAME = "PRODUCT_NAME";
    final String PRODUCT_NUMBER = "PRODUCT_NUMBER";

    private EditText editTextName;
    private EditText editTextNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        attributedWidget();
        setValueOnView(getProduct());
        if (savedInstanceState != null)
            savedInstanceStateService(savedInstanceState);
    }

    private void attributedWidget() {
        editTextName = findViewById(R.id.editText_productName);
        editTextNumber = findViewById(R.id.editText_productNumber);
    }

    private Product getProduct() {
        String json = getIntent().getStringExtra(PRODUCT);
        return new Gson().fromJson(json, Product.class);
    }

    private void setValueOnView(@NonNull Product product) {
        editTextName.setText(product.getName());
        editTextNumber.setText(String.valueOf(product.getNumber()));
    }

    private void savedInstanceStateService(Bundle savedInstanceState) {
        editTextName.setText(savedInstanceState.getString(PRODUCT_NAME));
        editTextNumber.setText(savedInstanceState.getString(PRODUCT_NUMBER));
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
            saveProduct();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveProduct() {
        String name = editTextName.getText().toString();
        String number = editTextNumber.getText().toString();

        if (name.trim().isEmpty() || name.trim().isEmpty()) {
            Toast.makeText(this,
                    "Please add name and number product", Toast.LENGTH_LONG).show();
            return;
        } else if (Integer.parseInt(number) == 0) {
            Toast.makeText(this,
                    "Number product can't be 0", Toast.LENGTH_LONG).show();
            return;
        }

        int shoppingListId = getProduct().getShoppingListId();
        Product product = new Product(name, Integer.parseInt(number), shoppingListId);
        setResult(RESULT_OK, getIntentData(product));
        finish();
    }

    private Intent getIntentData(Product product) {
        String json = new Gson().toJson(product);
        Intent data = new Intent();
        data.putExtra(PRODUCT, json);
        return data;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PRODUCT_NAME, editTextName.getText().toString());
        outState.putString(PRODUCT_NUMBER, editTextNumber.getText().toString());
    }
}