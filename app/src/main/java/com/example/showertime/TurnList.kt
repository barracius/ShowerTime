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
class TurnList(private val context: Activity, internal var turns: List<Turn>) : ArrayAdapter<Turn>(context, R.layout.activity_turn_list, turns) {


    @SuppressLint("SetTextI18n", "ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.activity_turn_list, null, true)

        val textViewName = listViewItem.findViewById(R.id.TextViewNameT) as TextView
        val textViewDate = listViewItem.findViewById(R.id.TextViewDateT) as TextView

        val turn = turns[position]
        textViewName.text = turn.username
        textViewDate.text = turn.date

        return listViewItem
    }
}