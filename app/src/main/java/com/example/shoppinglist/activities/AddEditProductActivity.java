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

public class AddEditProductActivity extends AppCompatActivity {
    public static final String ID = "ID";
    public static final String SHOPPING_LIST_ID = "SHOPPING_LIST_ID";
    public static final String PRODUCT_NAME = "PRODUCT_NAME";
    public static final String PRODUCT_NUMBER = "PRODUCT_NUMBER";

    private EditText editTextName, editTextNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        attributedWidget();
        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent = getIntent();
        intentService(intent);
        if (savedInstanceState != null)
            savedInstanceStateService(savedInstanceState);
    }

    private void attributedWidget() {
        editTextName = findViewById(R.id.editText_productName);
        editTextNumber = findViewById(R.id.editText_productNumber);
    }

    private void intentService(Intent intent) {
        if (intent.hasExtra(ID)) {
            setTitle("Edit product");
            editTextName.setText(intent.getStringExtra(PRODUCT_NAME));
            editTextNumber.setText(String.valueOf(intent.getIntExtra(PRODUCT_NUMBER, -1)));
        } else
            setTitle("Add product");
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
            Toast.makeText(this, "Please add name and number product", Toast.LENGTH_LONG).show();
            return;
        }

        int shoppingListId = getIntent().getIntExtra(SHOPPING_LIST_ID,
                AddEditShoppingListActivity.NEW_SHOPPING_LIST_ID - 1);
        if (shoppingListId != AddEditShoppingListActivity.NEW_SHOPPING_LIST_ID - 1)
            setResult(RESULT_OK, getIntentData(name, Integer.parseInt(number), shoppingListId));
        else
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        finish();
    }

    private Intent getIntentData(String name, int number, int shoppingListId) {
        Intent data = new Intent();
        data.putExtra(PRODUCT_NAME, name);
        data.putExtra(PRODUCT_NUMBER, number);
        data.putExtra(SHOPPING_LIST_ID, shoppingListId);

        int id = getIntent().getIntExtra(ID, -1);
        if (id != -1)
            data.putExtra(ID, id);
        return data;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PRODUCT_NAME, editTextName.getText().toString());
        outState.putString(PRODUCT_NUMBER, editTextNumber.getText().toString());
    }
}