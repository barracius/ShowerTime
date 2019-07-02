package com.example.showertime

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.showertime.VolleySingleton.Companion.current_user_name
import kotlinx.android.synthetic.main.loggedin_menu.*

class LoggedinMenu : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loggedin_menu)
        welcomeTextView.text = "Welcome to Shower Time, $current_user_name!"
        val createGroupbutton = createGroupButton
        val joinGroupbutton = joinGroupButton
        val myGroupsbutton = myGroupsButton


        createGroupbutton.setOnClickListener {
            val intent = Intent(applicationContext, CreateGroup::class.java)
            startActivity(intent)
        }

        joinGroupbutton.setOnClickListener {
            val intent = Intent(applicationContext, JoinGroup::class.java)
            startActivity(intent)
        }

        myGroupsbutton.setOnClickListener {
            val intent = Intent(applicationContext, MyGroups::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onBackPressed() {
    }
}