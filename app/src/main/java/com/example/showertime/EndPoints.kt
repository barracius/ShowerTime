package com.example.showertime

object EndPoints {
    private val URL_ROOT = "http://shower-time.duckdns.org/showertime/v1/?op="
    //private val URL_ROOT = "http://10.12.253.219/showertime/v1/?op="
    val URL_ADD_USER = URL_ROOT + "adduser"
    val URL_GET_USER = URL_ROOT + "getuser"
    val URL_ADD_GROUP = URL_ROOT + "addhome"
    val URL_JOIN_GROUP = URL_ROOT + "joinhome"
    val URL_MY_GROUPS = URL_ROOT + "mygroups"
    val URL_GET_GROUP_BY_ID = URL_ROOT + "getgroupbyid"
    val URL_ADD_BATHROOM = URL_ROOT + "addbathroom"
    val URL_GET_BATHROOMS = URL_ROOT + "getbathrooms"
    val URL_GET_TURNS = URL_ROOT + "getturns"
    val URL_ADD_TURN = URL_ROOT + "taketurn"
}