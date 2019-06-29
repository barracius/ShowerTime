package com.example.showertime

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.showertime.VolleySingleton.Companion.current_bathroom_id
import com.example.showertime.VolleySingleton.Companion.current_bathroom_name
import com.example.showertime.VolleySingleton.Companion.current_user_email
import kotlinx.android.synthetic.main.activity_bathroom_details.*
import org.json.JSONException
import org.json.JSONObject

class BathroomDetailsActivity : AppCompatActivity() {
    private var listView: ListView? = null
    private var turnList: MutableList<Turn>? = null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bathroom_details)
        title = "$current_bathroom_name's current turns"
        listView = turnsListView
        turnList = mutableListOf()
        loadTurns()
        addTurnButton.setOnClickListener {
            addTurn()
        }
    }

    private fun loadTurns() {
        val stringRequest = object : StringRequest(
            Request.Method.POST,
            EndPoints.URL_GET_TURNS,
            Response.Listener<String> { s ->
                try {
                    val obj = JSONObject(s)
                    if (!obj.getBoolean("error")) {
                        val array = obj.getJSONArray("turns")

                        for (i in 0..array.length() - 1) {
                            val objectArtist = array.getJSONObject(i)
                            val turn = Turn(
                                objectArtist.getString("username"),
                                objectArtist.getString("datetime")
                            )
                            turnList!!.add(turn)
                            val adapter = TurnList(this@BathroomDetailsActivity, turnList!!)
                            listView!!.adapter = adapter
                        }
                    } else {
                        Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()
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
                params["bathroom_id"] = current_bathroom_id
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add<String>(stringRequest)
    }

    private fun addTurn() {
        val stringRequest = object : StringRequest(
            Request.Method.POST, EndPoints.URL_ADD_TURN,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)
                    Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()
                    if(obj.getString("message") == "Successfully took turn"){
                        finish()
                        startActivity(intent)
                    }
                } catch (e: JSONException){
                    e.printStackTrace()
                }
            },
            object : Response.ErrorListener{
                override fun onErrorResponse(volleyError: VolleyError) {
                    Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["user_email"] = current_user_email
                params["bathroom_id"] = current_bathroom_id
                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
}
