package com.example.showertime

import android.app.Application
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

/**
 * Created by Belal on 5/16/2017.
 */
class VolleySingleton : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    val requestQueue: RequestQueue? = null
        get() {
            if (field == null) {
                return Volley.newRequestQueue(applicationContext)
            }
            return field
        }

    fun <T> addToRequestQueue(request: Request<T>) {
        request.tag = TAG
        requestQueue?.add(request)
    }

    companion object {
        var current_user_email = ""
        var current_user_name = ""
        var current_group_admin = "hola"
        var current_group_name = ""
        var current_group_id = ""
        private val TAG = VolleySingleton::class.java.simpleName
        @get:Synchronized var instance: VolleySingleton? = null
            private set
    }
}