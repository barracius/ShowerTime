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
import com.example.showertime.VolleySingleton.Companion.current_user_email
import kotlinx.android.synthetic.main.activity_create_group.*
import kotlinx.android.synthetic.main.activity_join_group.*
import org.json.JSONException
import org.json.JSONObject

class JoinGroup : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_group)

        val boton = submitButton4

        boton.setOnClickListener {
            joinGroup()
        }
    }

    private fun joinGroup() {
        val passwordedittext = passwordEditText3
        val password = passwordedittext.text.toString()
        val codeeditText = codeEditText
        val codigo = codeeditText.text.toString()

        val stringRequest = object : StringRequest(
            Request.Method.POST, EndPoints.URL_JOIN_GROUP,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)
                    //Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()
                    if(obj.getString("message") == "Successfully joined group"){
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
                params["group_id"] = codigo
                params["user_email"] = current_user_email
                params["password"] = password
                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
}
