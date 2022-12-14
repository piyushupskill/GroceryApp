package com.example.grocerrryapp

import android.annotation.SuppressLint
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

class MainActivity : AppCompatActivity() , GroceryRVAdpater.GroceryItemClickInterface{
    lateinit var itemsRV: RecyclerView
    lateinit var addFAB:  FloatingActionButton
    lateinit var  list:List<GroceryItems>
    lateinit var groceryRVAdpater: GroceryRVAdpater
    lateinit var groceryViewModel: GroceryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        itemsRV = findViewById(R.id.idRVItems)
        addFAB = findViewById(R.id.idFABAdd)
        list =ArrayList<GroceryItems>()
        groceryRVAdpater = GroceryRVAdpater(list ,this)
        itemsRV.layoutManager =LinearLayoutManager(this)
        itemsRV.adapter= groceryRVAdpater
        val groceryRepository = GroceryRepository(GroceryDatabase(this))
        val factory = GroceryViewModelFactory(groceryRepository)
        groceryViewModel =ViewModelProvider(this , factory).get(GroceryViewModel::class.java)
        groceryViewModel.allGroceryItems().observe(this ,Observer{
            groceryRVAdpater.list =it
            groceryRVAdpater.notifyDataSetChanged()
        })
        addFAB.setOnClickListener{
            openDialog()
        }
    }
fun  openDialog()
{
    val dialog = Dialog(this)
    dialog.setContentView(R.layout.grocerry_add_dialog)
    val cancelBtn= dialog.findViewById<Button>(R.id.idBtnCancel)
    val addBtn= dialog.findViewById<Button>(R.id.idBtnAdd)
    val itemEdt = dialog.findViewById<EditText>(R.id.idEdtItemName)
    val itemPriceEdt = dialog.findViewById<EditText>(R.id.idEdtItemPrice)
    val itemQuantityEdt = dialog.findViewById<EditText>(R.id.idEdtItemQuantity)
    cancelBtn.setOnClickListener{
        dialog.dismiss()
    }
    addBtn.setOnClickListener{
        val itemName : String = itemEdt.text.toString()
        val itemPrice: String = itemPriceEdt.text.toString()
        val itemQuantity : String = itemQuantityEdt.text.toString()
        val qty : Int =itemQuantity.toInt()
        val pr : Int =itemPrice.toInt()
        if(itemName.isNotEmpty() && itemPrice.isNotEmpty() && itemQuantity.isNotEmpty())
        {
            val items =GroceryItems(itemName,qty,pr)
            groceryViewModel.insert(items)
            Toast.makeText(applicationContext,"Item Inserted....",Toast.LENGTH_SHORT).show()
            groceryRVAdpater.notifyDataSetChanged()
            dialog.dismiss()

        }
        else
        {
            Toast.makeText(applicationContext,"Please enter all the data....",Toast.LENGTH_SHORT).show()

        }
    }
    dialog.show()
    
}
    override fun onItemClick(grocerItems: GroceryItems) {
        groceryViewModel.delete(grocerItems)
        groceryRVAdpater.notifyDataSetChanged()
        Toast.makeText(applicationContext,"Item Deleted Successfully ...",Toast.LENGTH_SHORT).show()
    }
}