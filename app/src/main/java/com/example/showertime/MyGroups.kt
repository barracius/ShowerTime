package com.example.showertime

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.showertime.VolleySingleton.Companion.current_group_id
import com.example.showertime.VolleySingleton.Companion.current_group_name
import com.example.showertime.VolleySingleton.Companion.current_user_email
import kotlinx.android.synthetic.main.activity_my_groups.*
import org.json.JSONException
import org.json.JSONObject

class MyGroups : AppCompatActivity() {
    private var listView: ListView? = null
    private var groupList: MutableList<Group>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_groups)

        listView = findViewById(R.id.listViewGroups) as ListView
        groupList = mutableListOf<Group>()
        loadGroups()
        setListOnClickListener()

    }

    private fun setListOnClickListener() {
        listViewGroups.setOnItemClickListener { _, _, position, _ ->
            val selectedGroup = (listViewGroups.adapter).getItem(position) as Group
            val intent = Intent(Intent(applicationContext, GroupDetailsActivity::class.java))
            current_group_id = selectedGroup.code
            current_group_name = selectedGroup.name
            startActivity(intent)
        }
    }

    private fun loadGroups() {
        val stringRequest = object : StringRequest(
            Request.Method.POST,
            EndPoints.URL_MY_GROUPS,
            Response.Listener<String> { s ->
                try {
                    val obj = JSONObject(s)
                    if (!obj.getBoolean("error")) {
                        val array = obj.getJSONArray("groups")

                        for (i in 0..array.length() - 1) {
                            val objectArtist = array.getJSONObject(i)
                            val group = Group(
                                objectArtist.getString("group_name"),
                                objectArtist.getString("group_id")
                            )
                            groupList!!.add(group)
                            val adapter = GroupList(this@MyGroups, groupList!!)
                            listView!!.adapter = adapter
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener {
                    volleyError -> Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show()
            }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["user_email"] = current_user_email
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add<String>(stringRequest)
    }
}
