package com.example.showertime

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.example.showertime.VolleySingleton.Companion.current_user_email
import com.example.showertime.VolleySingleton.Companion.current_user_name
import org.json.JSONException
import org.json.JSONObject

class Signin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin)

        val button : Button = findViewById(R.id.submitButton)
        button.setOnClickListener {
            if(validations()){
                addUser()
            }
        }

    }

    private fun validations() : Boolean {
        val nameEditText: EditText = findViewById(R.id.usernameEditText)
        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        if(name.length <= 2){
            Toast.makeText(applicationContext, "Your username must be at least 3 characters long", Toast.LENGTH_SHORT).show()
            return false
        }
        if(password.length <= 5){
            Toast.makeText(applicationContext, "Your password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
            return false
        }
        if(!isEmailValid(email) || email.isEmpty()){
            Toast.makeText(applicationContext, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            return false
        }
        else{
            return true
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun addUser() {
        val nameEditText: EditText = findViewById(R.id.usernameEditText)
        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        val stringRequest = object : StringRequest(Request.Method.POST, EndPoints.URL_ADD_USER,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)
                    //Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()
                    if(obj.getString("message") == "User added successfully"){
                        val intent = Intent(applicationContext, LoggedinMenu::class.java)
                        current_user_name = name
                        current_user_email = email
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
                params["email"] = email
                params["password"] = password
                params["username"] = name
                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
}