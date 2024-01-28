package com.gangulwar.auction

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.gangulwar.auction.navigation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                GlobalConstants.SERVER_IP = address
            }) {
                Text(text = "Connect")
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Check", "On Dispose Called")

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun MainHomeScreen(
    navController: NavController
) {

    var isIpEntered by remember {
        mutableStateOf(false)
    }

    var displayError by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.main_bg), contentDescription = null,
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 25.dp),
                painter = painterResource(id = R.drawable.gdsc_logo),
                contentDescription = null
            )

            Image(
                modifier = Modifier
                    .padding(
                        start = 35.dp, end = 35.dp,
                    )
                    .fillMaxWidth(),
                painter = painterResource(id = R.drawable.auction_text),
                contentDescription = null
            )

            Spacer(
                modifier = Modifier
                    .padding(top = 15.dp, bottom = 10.dp)
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(colorResource(id = R.color.theme_white))
            )
            var ipAddress by remember {
                mutableStateOf("")
            }

            var enterTeamName by remember {
                mutableStateOf("")
            }

            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(), border = BorderStroke(
                    width = 5.dp, colorResource(id = R.color.theme_white)
                ),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = Color.Transparent
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth()
                ) {

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        text = if (isIpEntered) "Enter Team's Name" else "Enter IP address",
                        style = TextStyle(
                            fontFamily = mincraftFont,
                            fontSize = 35.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    )

                    val interactionSource = remember { MutableInteractionSource() }
                    val keyboardController = LocalSoftwareKeyboardController.current

                    TextField(
                        modifier = Modifier
                            .padding(
                                top = 25.dp, start = 25.dp,
                                end = 25.dp, bottom = 10.dp
                            )
                            .fillMaxWidth()
                            .border(
                                width = 5.dp,
                                color =
                                if (interactionSource.collectIsFocusedAsState().value) {
                                    Color.Green
                                } else colorResource(id = R.color.text_field_yellow),
                                shape = RoundedCornerShape(15.dp)
                            ),
                        value = if (isIpEntered) enterTeamName else ipAddress,
                        onValueChange = {
                            if (isIpEntered) {
                                enterTeamName = it
                            } else {
                                ipAddress = it
                            }
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = colorResource(id = R.color.theme_white),
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                        ),
                        shape = RoundedCornerShape(15.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType =
                            if (isIpEntered) {
                                KeyboardType.Text
                            } else {
                                KeyboardType.Decimal
                            }

                        ),keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                            }
                        )
                    )

                    Button(
                        modifier = Modifier
                            .wrapContentWidth()
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 10.dp, bottom = 10.dp),
                        onClick = {
                            if (isIpEntered) {
                                keyboardController?.hide()
                                scope.launch {
                                    GlobalConstants.USER_NAME = enterTeamName
                                    try {
                                        val initializedClient = withContext(Dispatchers.Default) {
                                            Client(
                                                GlobalConstants.SERVER_IP,
                                                GlobalConstants.USER_NAME
                                            )
                                        }
                                        GlobalConstants.CLIENT = initializedClient
                                        delay(100)
                                        if (GlobalConstants.POINTS == 231204) {
                                            Toast.makeText(
                                                context,
                                                "Check Your Team Name Again!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            navController.navigate(Screens.Bid.route)
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            context,
                                            "Check Your IP address!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } else {
                                keyboardController?.hide()
                                GlobalConstants.SERVER_IP = ipAddress
                                isIpEntered = true
                            }

//                            navController.navigate(Screens.Bid.route)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(54, 190, 32)
                        )
                    ) {
                        Text(
                            modifier = Modifier,
                            text = if (isIpEntered) "done" else "connect", style = TextStyle(
                                fontFamily = mincraftFont,
                                fontSize = 35.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }

            Image(
                modifier = Modifier
                    .padding(top = 25.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                painter = painterResource(id = R.drawable.auction_illustration),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
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
//    HomeScreen(rememberNavController())
    MainHomeScreen(rememberNavController())
}