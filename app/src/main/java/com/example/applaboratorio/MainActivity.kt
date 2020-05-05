package com.example.applaboratorio

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_purchase_dialog.view.*
import java.io.File

class MainActivity : AppCompatActivity(), productFragment.OnListFragmentInteractionListener {
    var productList:MutableList<String> = ArrayList()
    var totalItemsOnCart=0
    var totalPriceOnCart=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fullText=assets.open("products.txt").bufferedReader().readLines()
        fullText.forEach{
            productList.add(it)
        }
        updateCart()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.recyclerViewLayout,productFragment.newInstance(productList),"productList")
            .commit()
    }

    override fun onListFragmentInteraction(item: String?) {
        val mDialogView =  LayoutInflater.from(this).inflate(R.layout.activity_purchase_dialog,null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        if (item != null) {
            mDialogView.purchaseNameText.text=item.split(",")[0].trim()
            mDialogView.purchasePriceText.text = item.split(",")[1].trim().toString()
        }
        mDialogView.cancelButton.setOnClickListener {
            mAlertDialog.dismiss()
        }
        mDialogView.acceptButton.setOnClickListener{
            mAlertDialog.dismiss()
            if (item != null) {
                val file = File(this.filesDir, "cart.txt")
                file.createNewFile()
                file.appendText(item+"\n")
            }
            updateCart()
        }

    }
    fun refreshList(){
        val actualFragment = supportFragmentManager.findFragmentByTag("productList")
        if (actualFragment != null) {
            supportFragmentManager.beginTransaction().detach(actualFragment).commit()
            supportFragmentManager.beginTransaction().attach(actualFragment).commit()
        }
    }

    fun updateCart(){
        val file = File(this.filesDir, "cart.txt")
        file.createNewFile()
        val cartText = file.bufferedReader().readLines()
        totalItemsOnCart = cartText.size
        totalPriceOnCart = 0
        cartText.forEach{
            totalPriceOnCart+=it.split(",")[1].trim().toInt()
        }
        itemAmountText.text=totalItemsOnCart.toString()
        cartTotalText.text = totalPriceOnCart.toString()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("PRICE",totalPriceOnCart)
        outState.putInt("ITEMS",totalItemsOnCart)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        totalItemsOnCart = savedInstanceState.getInt("ITEMS")
        totalPriceOnCart = savedInstanceState.getInt("PRICE")
        updateCart()
    }
}