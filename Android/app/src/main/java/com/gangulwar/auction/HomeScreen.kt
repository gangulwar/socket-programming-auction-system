package com.gangulwar.auction

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.gangulwar.auction.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController
) {

    var address by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TextField(
                value = address, onValueChange = {
                    address = it
                }, label = {
                    Text(text = "enter ip address")
                }
            )
            Button(onClick = {
                navController.navigate(Screens.Bid.route)
                User.SERVER_IP=address
            }) {
                Text(text = "Connect")
            }
        }
    }

    DisposableEffect(Unit){
        onDispose {
            Log.d("Check","On Dispose Called")

        }

    }
}


@Preview(
    showSystemUi = true,
    name = "Home Screen Preview",
    showBackground = true
)
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController())
}