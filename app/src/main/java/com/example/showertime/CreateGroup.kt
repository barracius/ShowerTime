package com.example.showertime

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.example.showertime.VolleySingleton.Companion.current_user_email
import kotlinx.android.synthetic.main.activity_create_group.*
import kotlinx.android.synthetic.main.loggedin_menu.*
import org.json.JSONException
import org.json.JSONObject

class CreateGroup : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        val button = submitButton3

        button.setOnClickListener {
            if(validations()){
                addGroup()
            }
        }
    }
    private fun validations() : Boolean {
        val groupnameEditText = groupNameEditText
        val grouppwEditText = groupPwEditText
        val name = groupnameEditText.text.toString()
        val password = grouppwEditText.text.toString()
        if(name.length <= 3){
            Toast.makeText(applicationContext, "Group name must be at least 4 characters long", Toast.LENGTH_SHORT).show()
            return false
        }
        if(password.length <= 3){
            Toast.makeText(applicationContext, "Group password must be at least 4 characters long", Toast.LENGTH_SHORT).show()
            return false
        }
        else{
            return true
        }
    }

    private fun addGroup() {
        val grouppwEditText = groupPwEditText
        val groupnameEditText = groupNameEditText
        val password = grouppwEditText.text.toString()
        val name = groupnameEditText.text.toString()

        val stringRequest = object : StringRequest(
            Request.Method.POST, EndPoints.URL_ADD_GROUP,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)
                    //Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()
                    if(obj.getString("message") == "Group created successfully"){
                        val intent = Intent(applicationContext, LoggedinMenu::class.java)
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
                params["admin"] = current_user_email
                params["password"] = password
                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
}
