package com.example.shoppinglist.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.shoppinglist.database.ShoppingListDao
import com.example.shoppinglist.database.ShoppingListDatabase
import com.example.shoppinglist.models.ShoppingList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ShoppingListRepository1(application: Application?) {
    private val shoppingListDao: ShoppingListDao
    private val allArchivedShoppingList: LiveData<List<ShoppingList>>
    private val allCurrentShoppingList: LiveData<List<ShoppingList>>
    private var id: Long = 0

    fun updateShoppingList(shoppingList: ShoppingList){
        CoroutineScope(IO).launch {
            updateShoppingListOnBg(shoppingListDao, shoppingList)
        }
    }

    fun deleteShoppingList(shoppingList: ShoppingList){
        CoroutineScope(IO).launch {
            deleteShoppingListOnBg(shoppingListDao, shoppingList)
        }
    }

    fun getAllCurrentShoppingList(): LiveData<List<ShoppingList>> {
        return allCurrentShoppingList
    }

    fun getAllArchivedShoppingList(): LiveData<List<ShoppingList>> {
        return allArchivedShoppingList
    }

    private fun updateShoppingListOnBg(dao: ShoppingListDao, shoppingList: ShoppingList){
        dao.updateShoppingList(shoppingList)
    }

    private fun deleteShoppingListOnBg(dao: ShoppingListDao, shoppingList: ShoppingList){
        dao.deleteShoppingList(shoppingList)
    }

    init {
        val database = ShoppingListDatabase.getInstance(application)
        shoppingListDao = database.shoppingListDao()
        allCurrentShoppingList = shoppingListDao.allCurrentShoppingList
        allArchivedShoppingList = shoppingListDao.allArchivedShoppingList
    }
}