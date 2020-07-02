package com.example.shoppinglist.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.models.ShoppingList
import com.example.shoppinglist.repositories.ShoppingListRepository

class ShoppingListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ShoppingListRepository = ShoppingListRepository(application)
    val allCurrentShoppingList: LiveData<List<ShoppingList>>
    val allArchivedShoppingList: LiveData<List<ShoppingList>>
    var insertionId: MutableLiveData<Long> = repository.getInsertionId()

    fun insertShoppingList(shoppingList: ShoppingList?) {
        repository.insertShoppingList(shoppingList!!)
    }

    fun updateShoppingLis(shoppingList: ShoppingList?) {
        repository.updateShoppingList(shoppingList!!)
    }

    fun deleteShoppingList(shoppingList: ShoppingList?) {
        repository.deleteShoppingList(shoppingList!!)
    }

    fun deleteEmptyShoppingList() {
        repository.deleteEmptyShoppingList()
    }

    init {
        allCurrentShoppingList = repository.getAllCurrentShoppingList()
        allArchivedShoppingList = repository.getAllArchivedShoppingList()
    }
}
