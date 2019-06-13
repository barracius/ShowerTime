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
class GroupList(private val context: Activity, internal var groups: List<Group>) : ArrayAdapter<Group>(context, R.layout.layout_list_group, groups) {


    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.layout_list_group, null, true)

        val textViewName = listViewItem.findViewById(R.id.textViewName) as TextView
        val textViewCode = listViewItem.findViewById(R.id.textViewCode) as TextView

        val group = groups[position]
        textViewName.text = group.name
        textViewCode.text = "Group code: " + group.code

        return listViewItem
    }
}