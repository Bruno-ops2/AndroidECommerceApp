package com.example.applaboratorio

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_on_cart_item_list.view.*
import java.io.File

class OnCartItemFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1
    private var mainContext:Context? =context
    private var itemsOnCart:MutableList<String> = ArrayList()
    private var uniqueItems:MutableList<String> = ArrayList()
    private var uniqueItemsCant:MutableList<Int> = ArrayList()
    private var listener: OnListFragmentInteractionListener2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cartAsText = File(context?.filesDir, "cart.txt").bufferedReader().readLines()
        cartAsText.forEach{
            itemsOnCart.add(it)
        }

        itemsOnCart.forEach{
            if (it !in uniqueItems){
                uniqueItems.add(it)
                uniqueItemsCant.add(1)
            }
            else{
                uniqueItemsCant[uniqueItems.indexOf(it)]+=1
            }
        }

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_on_cart_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyOnCartItemRecyclerViewAdapter(uniqueItems,uniqueItemsCant, listener,context)
            }
        }
        return view
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener2) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener2 {
        // TODO: Update argument type and name
        fun onListFragmentInteraction2(item: String?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(inContext: Context) =
            OnCartItemFragment().apply {
                mainContext=inContext
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
