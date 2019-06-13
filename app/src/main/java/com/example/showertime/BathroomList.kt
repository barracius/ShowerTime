package com.example.showertime

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

/**
 * Created by Belal on 5/21/2017.
 */
class BathroomList(private val context: Activity, internal var bathrooms: List<Bathroom>) : ArrayAdapter<Bathroom>(context, R.layout.activity_bathroom_list, bathrooms) {


    @SuppressLint("SetTextI18n", "ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.layout_list_group, null, true)

        val textViewName = listViewItem.findViewById(R.id.textViewNameB) as TextView

        val bathroom = bathrooms[position]
        textViewName.text = bathroom.name

        return listViewItem
    }
}