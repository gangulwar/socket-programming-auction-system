package com.gangulwar.auction.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gangulwar.auction.BiddingScreen
import com.gangulwar.auction.HomeScreen

@Composable
fun RootNavGraph(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Screens.Home.route
    ) {
        composable(route =Screens.Home.route){
            HomeScreen(navController)
        }
        composable(route =Screens.Bid.route){
            BiddingScreen(navController)
        }

    }
}


object Graph {
    const val ROOT = "ROOT_GRAPH"
}

sealed class Screens(var route:String){
    object Home:Screens("HOME")
    object Bid:Screens("BID")
}