package com.example.shoppinglist.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.shoppinglist.R;

public class MainActivity extends AppCompatActivity {
    public static final int CURRENT_SHOPPING_LIST_REQUEST = 1;
    public static final int ARCHIVED_SHOPPING_LIST_REQUEST = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setOnClickListenerCurrentListButton();
        setOnClickListenerArchivedListButton();
    }

    private void setOnClickListenerCurrentListButton() {
        Button buttonCurrentShoppingList = findViewById(R.id.button_currentShoppingList);
        buttonCurrentShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ShoppingListActivity.class);
                intent.putExtra(ShoppingListActivity.TYPE,
                        ShoppingListActivity.CURRENT_SHOPPING_LIST);

                startActivityForResult(intent, CURRENT_SHOPPING_LIST_REQUEST);
            }
        });
    }

    private void setOnClickListenerArchivedListButton() {
        Button buttonArchivedShoppingList = findViewById(R.id.button_archivedShoppingList);
        buttonArchivedShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ShoppingListActivity.class);
                intent.putExtra(ShoppingListActivity.TYPE,
                        ShoppingListActivity.ARCHIVED_SHOPPING_LIST);

                startActivityForResult(intent, ARCHIVED_SHOPPING_LIST_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}