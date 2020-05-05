package com.example.applaboratorio

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.contentValuesOf


import com.example.applaboratorio.OnCartItemFragment.OnListFragmentInteractionListener2
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_purchase_dialog.view.*

import kotlinx.android.synthetic.main.fragment_on_cart_item.view.*
import java.io.File

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyOnCartItemRecyclerViewAdapter(
    private val mValues: List<String>,
    private val mCountOfItems:List<Int>,
    private val mListener: OnListFragmentInteractionListener2?,
    private val mContext:Context
) : RecyclerView.Adapter<MyOnCartItemRecyclerViewAdapter.ViewHolder>() {
    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as String
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction2(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_on_cart_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        val cant = mCountOfItems[position]
        holder.mPrice.text = "$"+item.split(",")[1].trim()
        holder.mName.text = item.split(",")[0].trim()
        holder.mCant.text = cant.toString()
        holder.mTotal.text = (cant*item.split(",")[1].trim().toInt()).toString()
        holder.mEditCantImage.setOnClickListener {
            val mDialogView =  LayoutInflater.from(mContext).inflate(R.layout.activity_purchase_dialog,null)
            val mBuilder = AlertDialog.Builder(mContext).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            mDialogView.purchaseNameText.text=holder.mName.text.toString()
            mDialogView.dialogTextView.text = "Editar producto"
            mDialogView.purchasePriceText.text = item.split(",")[1].trim()
            Picasso.with(mContext).load(item.split(",")[2].trim())
                .resize(150,150)
                .into(mDialogView.purchaseImage)
            mDialogView.purchaseImage
            val arrayToTen = ArrayList<Int>()
            for (i in 0..50) arrayToTen.add(i)
            val spinner: Spinner = mDialogView.cantSpinner
            val spinnerAdapter = ArrayAdapter<Int>(mContext,R.layout.support_simple_spinner_dropdown_item,arrayToTen)
            spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            spinner.adapter=spinnerAdapter
            spinner.setSelection(cant)
            mDialogView.cancelButton.setOnClickListener {
                mAlertDialog.dismiss()
            }
            mDialogView.acceptButton.setOnClickListener{
                mAlertDialog.dismiss()
                var spinnerValue:Int = spinner.selectedItemPosition
                var file = File(mContext.filesDir, "cart.txt")
                file.delete()
                file = File(mContext.filesDir, "cart.txt")
                for(i in 0..(mValues.size-1)){
                    if (mValues[i]!=item){
                        for (j in 0..mCountOfItems[i]-1){
                            file.appendText(mValues[i]+"\n")
                        }
                    }
                    else{
                        for (j in 0..spinnerValue-1){
                            file.appendText(mValues[i]+"\n")
                        }
                    }
                }
                mListener?.reattachOrder()
            }
        }

        Picasso.with(mContext)
            .load(item.split(",")[2].trim()).resize(150,150)
            .into(holder.mImage)
        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mPrice: TextView = mView.productOnCartPrice
        val mName: TextView = mView.productOnCartName
        val mImage: ImageView = mView.onCartImage
        val mCant: TextView = mView.groupCantText
        val mTotal: TextView = mView.itemTotalPriceText
        val mEditCantImage: ImageView = mView.editCantImage

        override fun toString(): String {
            return super.toString() + " '" + mName.text + "'"
        }
    }
}
