package com.example.showertime

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.example.showertime.VolleySingleton.Companion.current_group_id
import kotlinx.android.synthetic.main.activity_create_bathroom.*
import kotlinx.android.synthetic.main.activity_create_group.*
import org.json.JSONException
import org.json.JSONObject

class CreateBathroom : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_bathroom)
        val button = submitButton5

        button.setOnClickListener {
            if(validations()){
                addBathroom()
            }
        }
    }
    private fun validations() : Boolean {
        val name = bathroomNameEditText.text.toString()
        if(name.length <= 3){
            Toast.makeText(applicationContext, "Bathroom name must be at least 4 characters long", Toast.LENGTH_SHORT).show()
            return false
        }
        else{
            return true
        }
    }

    private fun addBathroom() {
        val name = bathroomNameEditText.text.toString()

        val stringRequest = object : StringRequest(
            Request.Method.POST, EndPoints.URL_ADD_BATHROOM,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)
                    Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()
                    if(obj.getString("message") == "Bathroom added successfully"){
                        val intent = Intent(applicationContext, GroupDetailsActivity::class.java)
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
                params["id"] = "0"
                params["name"] = name
                params["group_id"] = current_group_id
                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
}
