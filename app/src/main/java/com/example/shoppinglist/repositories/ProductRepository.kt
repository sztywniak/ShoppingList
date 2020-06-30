package com.example.myapplication22

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.shoppinglist.database.ProductDao
import com.example.shoppinglist.database.ShoppingListDatabase
import com.example.shoppinglist.models.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ProductRepository(application: Application?) {
    private val productDao: ProductDao

    fun insertProduct(product: Product) {
        CoroutineScope(IO).launch {
            insertProductOnBg(productDao, product)
        }
    }

    fun updateProduct(product: Product){
        CoroutineScope(IO).launch {
            updateProductOnBg(productDao, product)
        }
    }

    fun deleteProduct(product: Product){
        CoroutineScope(IO).launch {
            deleteProductOnBg(productDao, product)
        }
    }

    fun getAllProduct(shoppingListId: Int): LiveData<List<Product>> {
        return productDao.getAllProduct(shoppingListId)
    }

    init {
        val database = ShoppingListDatabase.getInstance(application)
        productDao = database.productDao()
    }

    private fun insertProductOnBg(dao: ProductDao, product: Product) {
        dao.insertProduct(product)
    }

    private fun updateProductOnBg(dao: ProductDao, product: Product) {
        dao.updateProduct(product)
    }

    private fun deleteProductOnBg(dao: ProductDao, product: Product) {
        dao.deleteProduct(product)
    }
}
