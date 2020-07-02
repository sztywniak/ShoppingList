package com.example.shoppinglist.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.database.ShoppingListDao
import com.example.shoppinglist.database.ShoppingListDatabase
import com.example.shoppinglist.models.ShoppingList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ShoppingListRepository(application: Application?) {
    private val shoppingListDao: ShoppingListDao
    private val allArchivedShoppingList: LiveData<List<ShoppingList>>
    private val allCurrentShoppingList: LiveData<List<ShoppingList>>
    private var insertionId = MutableLiveData<Long>()

    fun insertShoppingList(shoppingList: ShoppingList) {
        CoroutineScope(IO).launch {
            val result = insertShoppingListOnBg(shoppingListDao, shoppingList)
            insertionId.postValue(result)
        }
    }

    fun updateShoppingList(shoppingList: ShoppingList) {
        CoroutineScope(IO).launch {
            updateShoppingListOnBg(shoppingListDao, shoppingList)
        }
    }

    fun deleteShoppingList(shoppingList: ShoppingList) {
        CoroutineScope(IO).launch {
            deleteShoppingListOnBg(shoppingListDao, shoppingList)
        }
    }

    fun deleteEmptyShoppingList(){
        CoroutineScope(IO).launch {
            deleteEmptyShoppingListOnBg(shoppingListDao)
        }
    }

    fun getAllCurrentShoppingList(): LiveData<List<ShoppingList>> {
        return allCurrentShoppingList
    }

    fun getAllArchivedShoppingList(): LiveData<List<ShoppingList>> {
        return allArchivedShoppingList
    }

    fun getInsertionId():MutableLiveData<Long>{
        return insertionId
    }

    private fun insertShoppingListOnBg(dao: ShoppingListDao, shoppingList: ShoppingList): Long {
        return dao.insertShoppingList(shoppingList)
    }

    private fun updateShoppingListOnBg(dao: ShoppingListDao, shoppingList: ShoppingList) {
        dao.updateShoppingList(shoppingList)
    }

    private fun deleteShoppingListOnBg(dao: ShoppingListDao, shoppingList: ShoppingList) {
        dao.deleteShoppingList(shoppingList)
    }

    private fun deleteEmptyShoppingListOnBg(dao: ShoppingListDao){
        dao.deleteEmptyShoppingList()
    }

    init {
        val database = ShoppingListDatabase.getInstance(application)
        shoppingListDao = database.shoppingListDao()
        allCurrentShoppingList = shoppingListDao.allCurrentShoppingList
        allArchivedShoppingList = shoppingListDao.allArchivedShoppingList
    }
}