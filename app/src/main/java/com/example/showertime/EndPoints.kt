package com.example.showertime

object EndPoints {
    private val URL_ROOT = "http://shower-time.duckdns.org/showertime/v1/?op="
    val URL_ADD_USER = URL_ROOT + "adduser"
    val URL_GET_USER = URL_ROOT + "getuser"
    val URL_ADD_GROUP = URL_ROOT + "addhome"
    val URL_JOIN_GROUP = URL_ROOT + "joinhome"
    val URL_MY_GROUPS = URL_ROOT + "mygroups"
    val URL_GET_GROUP_BY_ID = URL_ROOT + "getgroupbyid"
    val URL_ADD_BATHROOM = URL_ROOT + "addbathroom"
}