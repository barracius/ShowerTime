package com.example.showertime

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signinButton = findViewById<Button>(R.id.signinButton)

        loginButton.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        signinButton.setOnClickListener {
            val intent = Intent(this, Signin::class.java)
            startActivity(intent)
        }
    }


}
