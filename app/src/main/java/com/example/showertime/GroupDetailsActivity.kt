package com.example.showertime

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.example.showertime.VolleySingleton.Companion.current_group_admin
import com.example.showertime.VolleySingleton.Companion.current_group_id
import com.example.showertime.VolleySingleton.Companion.current_group_name
import com.example.showertime.VolleySingleton.Companion.current_user_email
import kotlinx.android.synthetic.main.activity_group_details.*
import org.json.JSONException
import org.json.JSONObject

class GroupDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_details)
        getGroupInfo()
        createBathroomButton.setOnClickListener {
            val intent = Intent(applicationContext, CreateBathroom::class.java)
            startActivity(intent)
        }
    }

    private fun changeBathroomVisibility() {
        if(current_user_email == current_group_admin){
            createBathroomButton.visibility = View.VISIBLE
        }else{
            createBathroomButton.visibility = View.INVISIBLE
        }

    }

    private fun getGroupInfo() {

        val stringRequest = object : StringRequest(
            Request.Method.POST, EndPoints.URL_GET_GROUP_BY_ID,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)
                    if(obj.getString("message") == "Group data extracted successfully"){
                        current_group_name = obj.getString("group_name")
                        current_group_admin = obj.getString("group_admin")
                        changeBathroomVisibility()
                    }
                } catch (e: JSONException){
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { volleyError -> Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show() }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["id"] = current_group_id
                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
}
