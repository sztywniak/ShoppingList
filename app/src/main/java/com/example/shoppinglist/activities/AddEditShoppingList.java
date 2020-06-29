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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppinglist.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddEditShoppingList extends AppCompatActivity {
    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_NAME = "EXTRA_NAME";
    public static final String EXTRA_DATE = "EXTRA_DATE";
    public static final String EXTRA_PRODUCT_LIST = "EXTRA_PRODUCT_LIST";
    public static final String EXTRA_IS_SHOWING_DIALOG = "EXTRA_IS_SHOWING_DIALOG";

    private EditText editTextName, editTextProduct;
    private TextView textViewDate;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private boolean isShowingDialog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_shopping_list);
        attributedWidget();
        setOnClickListenerDate();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        intentService();
        if (savedInstanceState != null)
            savedInstanceStateService(savedInstanceState);
    }

    private void attributedWidget() {
        editTextName = findViewById(R.id.editText_name);
        textViewDate = findViewById(R.id.textView_date);
        editTextProduct = findViewById(R.id.editText_product);
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

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
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
                AddEditShoppingList.this, dateSetListener, year, month, day);
        dialog.show();
        isShowingDialog = true;
    }

    private void intentService() {
        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit shopping list");
            editTextName.setText(intent.getStringExtra(EXTRA_NAME));
            textViewDate.setText(intent.getStringExtra(EXTRA_DATE).substring(0, 10));
            editTextProduct.setText(intent.getStringExtra(EXTRA_PRODUCT_LIST));
        } else {
            setTitle("Add shopping list");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            textViewDate.setText(sdf.format(new Date()));
        }
    }

    private void savedInstanceStateService(Bundle savedInstanceState) {
        editTextName.setText(savedInstanceState.getString(EXTRA_NAME));
        editTextProduct.setText(savedInstanceState.getString(EXTRA_PRODUCT_LIST));
        if (savedInstanceState.getBoolean(EXTRA_IS_SHOWING_DIALOG))
            showDateDialog();
        else
            textViewDate.setText(savedInstanceState.getString(EXTRA_DATE).substring(0, 10));
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
            case R.id.save_shopping_list:
                saveShoppingList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveShoppingList() {
        String name = editTextName.getText().toString();
        String date = textViewDate.getText().toString() + " 23:59:59:999";
        String product = editTextProduct.getText().toString();

        if (name.trim().isEmpty() && product.trim().isEmpty()) {
            Toast.makeText(this, "Please insert name list and add product", Toast.LENGTH_LONG).show();
            return;
        } else if (name.trim().isEmpty()) {
            Toast.makeText(this, "Please insert name list", Toast.LENGTH_LONG).show();
            return;
        } else if (product.trim().isEmpty()) {
            Toast.makeText(this, "Please add product", Toast.LENGTH_LONG).show();
            return;
        }

        setResult(RESULT_OK, getIntentData(name, date, product));
        finish();
    }

    private Intent getIntentData(String name, String date, String product) {
        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, name);
        data.putExtra(EXTRA_DATE, date);
        data.putExtra(EXTRA_PRODUCT_LIST, product);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1)
            data.putExtra(EXTRA_ID, id);
        return data;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_NAME, editTextName.getText().toString());
        outState.putString(EXTRA_DATE, textViewDate.getText().toString());
        outState.putString(EXTRA_PRODUCT_LIST, editTextProduct.getText().toString());
        outState.putBoolean(EXTRA_IS_SHOWING_DIALOG, isShowingDialog);
    }
}
