package com.example.showertime

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.example.showertime.VolleySingleton.Companion.current_user_email
import com.example.showertime.VolleySingleton.Companion.current_user_name
import kotlinx.android.synthetic.main.login.*
import org.json.JSONException
import org.json.JSONObject

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val boton = submitButton2

        boton.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val email = emailEditText2.text.toString()
        val password = passwordEditText2.text.toString()
        var name = ""

        val stringRequest = object : StringRequest(
            Request.Method.POST, EndPoints.URL_GET_USER,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)
                    //Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()
                    if(obj.getString("message") == "Logged in"){
                        name = obj.getString("name")
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