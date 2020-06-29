package com.example.shoppinglist.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.shoppinglist.R;

public class ViewArchivedShoppingListActivity extends AppCompatActivity {
    public static final String EXTRA_ID = ".EXTRA_ID";
    public static final String EXTRA_NAME = ".EXTRA_NAME";
    public static final String EXTRA_DATE = ".EXTRA_DATE";
    public static final String EXTRA_PRODUCT_LIST = ".EXTRA_PRODUCT_LIST";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_archived_shopping_list);

        TextView textViewName = findViewById(R.id.textView_name);
        TextView textViewDate = findViewById(R.id.textView_date);
        TextView textViewProduct = findViewById(R.id.textView_product);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            textViewName.setText(intent.getStringExtra(EXTRA_NAME));
            textViewDate.setText(intent.getStringExtra(EXTRA_DATE).substring(0, 10));
            textViewProduct.setText(intent.getStringExtra(EXTRA_PRODUCT_LIST));
        }
    }
}
