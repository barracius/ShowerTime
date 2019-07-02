package com.example.showertime

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.widget.ListView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.showertime.VolleySingleton.Companion.cantidad_turnos
import com.example.showertime.VolleySingleton.Companion.current_bathroom_id
import com.example.showertime.VolleySingleton.Companion.current_bathroom_name
import com.example.showertime.VolleySingleton.Companion.current_user_email
import kotlinx.android.synthetic.main.activity_bathroom_details.*
import kotlinx.android.synthetic.main.login.*
import org.json.JSONException
import org.json.JSONObject
import kotlin.properties.Delegates

// Notificacion al tomar turno con tiempo estimado.
class BathroomDetailsActivity : AppCompatActivity() {
    private var listView: ListView? = null
    private var turnList: MutableList<Turn>? = null
    var cantElementos: Int? = 0
    var notificationId: Int = 0
    var estaVacio: Boolean = false
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bathroom_details)
        textView2.text = "$current_bathroom_name's current turns"
        listView = turnsListView
        turnList = mutableListOf()
        loadTurns()
        addTurnButton.setOnClickListener {
            addTurn()
        }
        deleteTurnButton.setOnClickListener {
            deleteTurn()
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
                        cantidad_turnos = array.length()
                    } else {
                        if(obj.getString("message") == "This bathroom has no turns"){
                            estaVacio = true
                        }
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


    fun addTurn() {
        val CHANNEL_ID = "hola"
        notificationId+=1
        Toast.makeText(applicationContext, "Cant elementos: ${cantidad_turnos}", Toast.LENGTH_LONG).show()
        val stringRequest = object : StringRequest(
            Request.Method.POST, EndPoints.URL_ADD_TURN,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)
                    //Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()
                    if(obj.getString("message") == "Successfully took turn"){
                        var builder: NotificationCompat.Builder?
                        if (estaVacio){
                            builder = NotificationCompat.Builder(this, CHANNEL_ID)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Shower Time")
                                .setContentText("It's your turn to use $current_bathroom_name")
                                .setStyle(NotificationCompat.BigTextStyle()
                                    .bigText("It's your turn to use $current_bathroom_name"))
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        }
                        else{
                            val tiempo = cantidad_turnos*7
                            builder = NotificationCompat.Builder(this, CHANNEL_ID)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Shower Time")
                                .setContentText("$tiempo minutes remaining to use $current_bathroom_name")
                                .setStyle(NotificationCompat.BigTextStyle()
                                    .bigText("$tiempo minutes remaining to use $current_bathroom_name"))
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        }
                        with(NotificationManagerCompat.from(this)) {
                            // notificationId is a unique int for each notification that you must define
                            notify(notificationId, builder.build())
                        }
                        finish()
                        startActivity(intent)
                        estaVacio = false
                        //enviar notificacion y calcular tiempo estimado
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

    private fun deleteTurn() {

        val stringRequest = object : StringRequest(
            Request.Method.POST, EndPoints.URL_DEL_TURN,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)
                    //Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()
                    if(obj.getString("message") == "Turn deleted successfully"){
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
