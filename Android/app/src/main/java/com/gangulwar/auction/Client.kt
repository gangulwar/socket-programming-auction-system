package com.gangulwar.auction

import kotlinx.coroutines.Dispatchers
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
                println(message)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
