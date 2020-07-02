package com.example.shoppinglist.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.shoppinglist.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setOnClickListenerCurrentListButton();
        setOnClickListenerArchivedListButton();
    }

    private void setOnClickListenerCurrentListButton() {
        Button buttonCurrentShoppingList = findViewById(R.id.button_currentShoppingList);
        buttonCurrentShoppingList.setOnClickListener(view ->
                createIntentAndStartNewActivity(ShoppingListActivity.CURRENT_SHOPPING_LIST));
    }

    private void setOnClickListenerArchivedListButton() {
        Button buttonArchivedShoppingList = findViewById(R.id.button_archivedShoppingList);
        buttonArchivedShoppingList.setOnClickListener(view ->
                createIntentAndStartNewActivity(ShoppingListActivity.ARCHIVED_SHOPPING_LIST));
    }

    private void createIntentAndStartNewActivity(String typeButton) {
        Intent intent = new Intent(MainActivity.this, ShoppingListActivity.class);
        intent.putExtra(ShoppingListActivity.TYPE, typeButton);
        startActivity(intent);
    }
}