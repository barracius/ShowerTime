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
import com.example.showertime.VolleySingleton.Companion.current_bathroom_name
import com.example.showertime.VolleySingleton.Companion.current_group_id
import com.example.showertime.VolleySingleton.Companion.current_user_email
import kotlinx.android.synthetic.main.activity_groups_bathroom.*
import kotlinx.android.synthetic.main.activity_my_groups.*
import org.json.JSONException
import org.json.JSONObject

class GroupsBathroom : AppCompatActivity() {
    private var listView: ListView? = null
    private var bathroomList: MutableList<Bathroom>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups_bathroom)

        listView = findViewById(R.id.listViewBathrooms) as ListView
        bathroomList = mutableListOf()
        loadBathrooms()
        setListOnClickListener()

    }

    private fun setListOnClickListener() {
        listViewBathrooms.setOnItemClickListener { _, _, position, _ ->
            val selectedBathroom = (listViewBathrooms.adapter).getItem(position) as Bathroom
            val intent = Intent(Intent(applicationContext, BathroomDetailsActivity::class.java))
            current_bathroom_name = selectedBathroom.name
            startActivity(intent)
        }
    }

    private fun loadBathrooms() {
        val stringRequest = object : StringRequest(
            Request.Method.POST,
            EndPoints.URL_GET_BATHROOMS,
            Response.Listener<String> { s ->
                try {
                    val obj = JSONObject(s)
                    if (!obj.getBoolean("error")) {
                        val array = obj.getJSONArray("groups")

                        for (i in 0..array.length() - 1) {
                            val objectArtist = array.getJSONObject(i)
                            val bathroom = Bathroom(
                                objectArtist.getString("name")
                            )
                            bathroomList!!.add(bathroom)
                            val adapter = BathroomList(this@GroupsBathroom, bathroomList!!)
                            listView!!.adapter = adapter
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { volleyError ->
                Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["group_id"] = current_group_id
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add<String>(stringRequest)
    }
}