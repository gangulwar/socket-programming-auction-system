package com.gangulwar.auction.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gangulwar.auction.BiddingScreen
import com.gangulwar.auction.HomeScreen
import com.gangulwar.auction.MainBiddingScreen
import com.gangulwar.auction.MainHomeScreen

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
            MainHomeScreen(navController)
        }
        composable(route =Screens.Bid.route){
            MainBiddingScreen(navController)
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