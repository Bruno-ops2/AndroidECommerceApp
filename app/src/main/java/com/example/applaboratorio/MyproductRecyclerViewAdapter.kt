package com.example.applaboratorio

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView


import com.example.applaboratorio.productFragment.OnListFragmentInteractionListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_on_cart_item.view.*

import kotlinx.android.synthetic.main.fragment_product.view.*
import kotlin.concurrent.fixedRateTimer
class MyproductRecyclerViewAdapter(
    private val mValues: List<String>,
    private val mListener: OnListFragmentInteractionListener?,
    private val mContext: Context
) : RecyclerView.Adapter<MyproductRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as String
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mNameView.text = item.split(",")[0]
        holder.mPriceView.text = "$"+item.split(",")[1]
        Picasso.with(mContext).load(item.split(",")[2].trim())
            .resize(100,100)
            .into(holder.mImageView)
        holder.mDownArrowDetail.setOnClickListener {
            if (holder.mDetailView.text==""){
                holder.mDetailView.text=item.split(",")[3].trim()
                holder.mDownArrowDetail.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp)
            }
            else{
                holder.mDetailView.text=""
                holder.mDownArrowDetail.setImageResource(R.drawable.downarrow_white_50)

            }

        }
        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mNameView: TextView = mView.itemName
        val mPriceView: TextView = mView.itemPrice
        val mImageView: ImageView = mView.productImageView
        val mDownArrowDetail: ImageView = mView.detailArrowImage
        val mDetailView:TextView = mView.productDetailsText

        override fun toString(): String {
            return super.toString() + " '" + mNameView.text + "'"
        }
    }
}
