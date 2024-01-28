package com.gangulwar.auction

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class Client(serverIP: String, username: String) {
    private val SERVER_IP: String = serverIP
    private val SERVER_PORT: Int = 12345
    private val writer: PrintWriter
    private val serverReader: BufferedReader

    init {
        val socket = Socket(SERVER_IP, SERVER_PORT)
        serverReader = BufferedReader(InputStreamReader(socket.getInputStream()))
        writer = PrintWriter(socket.getOutputStream(), true)
        writer.println(username)
        Thread { handleServerMessages(serverReader) }.start()
    }

    public suspend fun sendMessagetoServer(message: String) = withContext(Dispatchers.IO) {
        writer.println(message)
    }


    private fun handleServerMessages(serverReader: BufferedReader) {
        try {
            var message: String?
            while (serverReader.readLine().also { message = it } != null) {
                if (checkUserConnected(message)) {
                    val pattern = Regex(":\\s*(.*)")

                    val matchResult = message?.let { pattern.find(it) }

                    val value = matchResult?.groupValues?.get(1)
                    if (value != null) {
                        assignPoints(value)
                    }
                }

                if (checkHighestBid(message)) {
                    val pattern = Regex("HIGHEST_BID: (\\d+) BY: (\\w+)")

                    val matchResult =  pattern.find(message.toString())

                    val bidValue = matchResult?.groupValues?.get(1)?.toIntOrNull()
                    val bidderName = matchResult?.groupValues?.get(2)
//                    if (bidderName == GlobalConstants.USER_NAME) {
//                        if (bidValue != null) {
//                            GlobalConstants.POINTS -= bidValue
//                        }
//                    }
                    GlobalConstants.HIGHEST_BID = "Bid: $bidValue Team Name: $bidderName"
                }

                if (checkWinner(message)) {
                    val pattern = Regex("Team Name: (\\w+) Bid: (\\d+)")

                    val matchResult = pattern.find(message.toString())

                    if (matchResult != null) {
                        val teamName = matchResult.groupValues[1]
                        val bidValue = matchResult.groupValues[2]

                        GlobalConstants.CURRENT_WINNER = Pair(teamName, bidValue)
                        if (GlobalConstants.USER_NAME==teamName){
                            GlobalConstants.POINTS -= bidValue.toIntOrNull() ?: 0
                        }
                        println("Team Name: $teamName")
                        println("Bid Value: $bidValue")

                        GlobalScope.launch {
                            delay(1000) // Adjust the duration as needed
                            // Code to be executed after the delay
                        }
                    } else {
                        println("Pattern not found in the string.")
                    }
                }

                println(message)
                println("User Point ${GlobalConstants.POINTS}")
                println("Highest Bid ${GlobalConstants.HIGHEST_BID}")

            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun checkUserConnected(message: String?): Boolean {
        if (message?.contains("USER_CONNECTED") == true) {
            if (message.contains(GlobalConstants.USER_NAME)) {
                if (!message.contains("NOT_FOUND")){
                    return true
                }
            }
        }
        return false
    }

    private fun assignPoints(points: String) {
        GlobalConstants.POINTS = Integer.parseInt(points)
    }

    private fun checkHighestBid(message: String?): Boolean {
        return message?.contains("HIGHEST_BID") == true
    }

    private fun checkWinner(message: String?): Boolean {
        return message?.contains("WINNER:") == true
    }
}
