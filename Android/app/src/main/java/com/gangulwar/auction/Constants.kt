package com.gangulwar.auction

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object GlobalConstants{
    var USER_NAME:String="team2"
    var POINTS:Int=231204
    var SERVER_IP:String=""
    lateinit var CLIENT:Client
    var HIGHEST_BID by mutableStateOf("Bidding Yet To Start")
    var CURRENT_WINNER by mutableStateOf(
        Pair("Team1","2312")
    )
}