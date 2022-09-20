package com.google.groceryapp

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GroceryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insert(item : GroceryItems)

    @Delete
 suspend fun delete(item: GroceryItems)


    @Query("SELECT * fROM grocery_items")
   fun getAllGroceryItems() : LiveData<List<GroceryItems>>

}