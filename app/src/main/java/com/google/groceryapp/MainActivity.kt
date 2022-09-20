package com.google.groceryapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() ,GroceryRVAdapter.GroceryItemClickInterface{

    lateinit var itemsRV : RecyclerView
    lateinit var addFAB : FloatingActionButton
    lateinit var list: List<GroceryItems>
    lateinit var groceryRVAdapter: GroceryRVAdapter
    lateinit var groceryViewModel: GroceryViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        itemsRV = findViewById(R.id.idRVItem)
        addFAB = findViewById(R.id.idFABAdd)

        list = ArrayList<GroceryItems>()
        groceryRVAdapter = GroceryRVAdapter(list,this)
        itemsRV.layoutManager= LinearLayoutManager(this)
        itemsRV.adapter = groceryRVAdapter
       val groceryRepository = GroceryRepository(GroceryDatabase(this))
        val factory = GroceryViewModelFactory(groceryRepository)
        groceryViewModel = ViewModelProvider(this,factory).get(GroceryViewModel::class.java)

        groceryViewModel.getAllGroceryItems().observe(this,Observer{
            groceryRVAdapter.list = it
            groceryRVAdapter.notifyDataSetChanged()
        })

        addFAB.setOnClickListener{
            openDialog()
        }



    }

    fun openDialog() {

        val gdialog = Dialog(this)
        gdialog.setContentView(R.layout.grocery_add_dialog)

        val cancelBtn = gdialog.findViewById<Button>(R.id.idBtnCancel)
        val addBtn = gdialog.findViewById<Button>(R.id.idBtnAdd)
        val itemEdt = gdialog.findViewById<EditText>(R.id.idEdItemName)
        val itemPriceEdt = gdialog.findViewById<EditText>(R.id.idEdItemPrice)
        val itemQuantityEdt = gdialog.findViewById<EditText>(R.id.idEdItemQunatity)

        cancelBtn.setOnClickListener {
            gdialog.dismiss()

        }

        addBtn.setOnClickListener {
            val itemName: String = itemEdt.text.toString()
            val itemPrice: String = itemPriceEdt.text.toString()
            val itemQuantity: String = itemQuantityEdt.text.toString()
            val qty: Int = itemQuantity.toInt()
            val pr: Int = itemPrice.toInt()
            if ((itemName.isNotEmpty()) && (itemPrice.isNotEmpty()) && (itemQuantity.isNotEmpty())) {
                val items = GroceryItems(itemName, qty, pr)
                groceryViewModel.insert(items)
                Toast.makeText(applicationContext, "Item inserted.. ", Toast.LENGTH_LONG).show()
                groceryRVAdapter.notifyDataSetChanged()
                gdialog.dismiss()
            } else {
                Toast.makeText(applicationContext, "please enter all data", Toast.LENGTH_LONG).show()
                gdialog.dismiss()

            }

        }
        
gdialog.show()
    }


    override fun onItemClick(groceryItems: GroceryItems) {
       groceryViewModel.delete(groceryItems)
        groceryRVAdapter.notifyDataSetChanged()
        Toast.makeText(applicationContext,"Item Deleted.. ",Toast.LENGTH_LONG).show()
    }
}