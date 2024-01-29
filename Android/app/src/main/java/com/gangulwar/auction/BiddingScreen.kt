package com.gangulwar.auction

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiddingScreen(
    navController: NavController
) {
    var message by remember { mutableStateOf("") }
    var client: Client? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        // Launch a coroutine to asynchronously initialize the client
        val initializedClient = withContext(Dispatchers.Default) {
            Client(GlobalConstants.SERVER_IP, GlobalConstants.USER_NAME)
        }
        client = initializedClient
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = message, onValueChange = {
                    message = it
                }, keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            Button(onClick = {
                scope.launch {
                    client?.sendMessagetoServer(message)
                }

            }) {
                Text(text = "Send")
            }
        }
        Text(text = "Bidding Started")
    }
}

var lauchedForFirstTime = 0

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainBiddingScreen(
    navController: NavController
) {
    var displayWinner by remember {
        mutableStateOf(false)
    }

    val temp by rememberUpdatedState(GlobalConstants.CURRENT_WINNER)

    LaunchedEffect(temp) {
        if (lauchedForFirstTime != 0) {
            displayWinner = true
            delay(10000)
            displayWinner = false
        }
        lauchedForFirstTime = 1
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bidding_bg),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )

        if (!displayWinner) {
            MainView()
        } else {
            DisplayWinner()
        }
    }
}


@Composable
fun DisplayWinner() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.dialogbox), contentDescription = null
            )
            Column(
                modifier = Modifier
                    .offset(x = 0.dp, y = (-15).dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    text = "winner", style = TextStyle(
                        fontFamily = mincraftFont,
                        fontSize = 35.sp,
                        color = Color.Green,
                        textAlign = TextAlign.Center
                    )
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    text = "bid: ${GlobalConstants.CURRENT_WINNER.second}", style = TextStyle(
                        fontFamily = mincraftFont,
                        fontSize = 35.sp,
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    text = "Team: ${GlobalConstants.CURRENT_WINNER.first}", style = TextStyle(
                        fontFamily = mincraftFont,
                        fontSize = 35.sp,
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun MainView() {
    val scope = rememberCoroutineScope()
    val temp by rememberUpdatedState(GlobalConstants.HIGHEST_BID)
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
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
            painter = painterResource(id = R.drawable.dexters_laboratory),
            contentDescription = null
        )

        Spacer(
            modifier = Modifier
                .padding(top = 15.dp, bottom = 10.dp)
                .fillMaxWidth()
                .height(10.dp)
                .background(colorResource(id = R.color.theme_white))
        )

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
                    text = "HIGHEST bid", style = TextStyle(
                        fontFamily = mincraftFont,
                        fontSize = 35.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                )

                val interactionSource = remember { MutableInteractionSource() }

                TextField(
                    modifier = Modifier
                        .padding(
                            top = 10.dp, start = 25.dp,
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

                    value = addNewlineAfterNumber(),
                    onValueChange = {

                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = colorResource(id = R.color.theme_white),
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                    ),
                    shape = RoundedCornerShape(15.dp),
                    readOnly = true,

                    )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    text = "Place you bid", style = TextStyle(
                        fontFamily = mincraftFont,
                        fontSize = 35.sp,
                        color = Color(255, 229, 0),
                        textAlign = TextAlign.Center
                    )
                )

                val source = remember { MutableInteractionSource() }
                var userBid by remember {
                    mutableStateOf("")
                }
                val keyboardController = LocalSoftwareKeyboardController.current

                TextField(
                    modifier = Modifier
                        .padding(
                            top = 10.dp, start = 25.dp,
                            end = 25.dp, bottom = 10.dp
                        )
                        .fillMaxWidth()
                        .border(
                            width = 5.dp,
                            color =
                            if (source.collectIsFocusedAsState().value) {
                                Color.Green
                            } else colorResource(id = R.color.text_field_yellow),
                            shape = RoundedCornerShape(15.dp)
                        ),

                    value = userBid,
                    onValueChange = {
                        userBid = it
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = colorResource(id = R.color.theme_white),
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                    ),
                    shape = RoundedCornerShape(15.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ), keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }, onGo = {
                            keyboardController?.hide()
                        }, onNext = {
                            keyboardController?.hide()
                        }, onPrevious = {
                            keyboardController?.hide()
                        }, onSearch = {
                            keyboardController?.hide()
                        },
                        onSend = {
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
                        scope.launch {
                            if (userBid.toIntOrNull() == null) {
                                Toast.makeText(
                                    context,
                                    "Enter a valid bid",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                if (Integer.parseInt(userBid) > GlobalConstants.POINTS) {
                                    Toast.makeText(
                                        context,
                                        "Your Bid Greater than your points",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    GlobalConstants.CLIENT.sendMessagetoServer(userBid)
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(54, 190, 32)
                    ),
                ) {
                    Text(
                        modifier = Modifier,
                        text = "bid now", style = TextStyle(
                            fontFamily = mincraftFont,
                            fontSize = 35.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    )
                }

            }
        }

        /*TextField(
            modifier = Modifier
                .padding(
                    top = 10.dp, start = 25.dp,
                    end = 25.dp, bottom = 10.dp
                )
                .fillMaxWidth()
                .border(
                    width = 5.dp,
                    color =
                     colorResource(id = R.color.text_field_yellow),
                    shape = RoundedCornerShape(15.dp)
                ),

            value = User.POINTS,
            onValueChange = {},
            colors = TextFieldDefaults.textFieldColors(
                containerColor = colorResource(id = R.color.theme_white),
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(15.dp), readOnly = true
        )*/

        Box(
            modifier = Modifier
                .padding(25.dp)
                .wrapContentWidth()
                .background(
                    colorResource(id = R.color.theme_white),
                    shape = RoundedCornerShape(15.dp)
                )
                .border(
                    width = 5.dp,
                    color =
                    colorResource(id = R.color.text_field_yellow),
                    shape = RoundedCornerShape(15.dp)
                )
                .padding(10.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
//                    modifier = Modifier.fillMaxWidth(),
                text = "balance : ${GlobalConstants.POINTS}", style = TextStyle(
                    fontFamily = mincraftFont,
                    fontSize = 35.sp,
                    color = Color(85, 84, 84),
                ), textAlign = TextAlign.Center
            )
        }
    }
}

fun addNewlineAfterNumber(): String {
    var input=GlobalConstants.HIGHEST_BID
    val regex = Regex("\\d+")
    if (input.contains("Bid:")){
        val matchResult = regex.find(input)

        return if (matchResult != null) {
            val numericPart = matchResult.value
            val index = input.indexOf(numericPart) + numericPart.length
            input.substring(0, index).trim() + "\n" + input.substring(index).trimStart()
        } else {
            input
        }
    }else{
         return input
    }

}

@Preview(
    showSystemUi = true,
    name = "Bidding Screen Preview",
    showBackground = true
)
@Composable
fun BiddingScreenPreview() {
//    BiddingScreen(rememberNavController())
    MainBiddingScreen(rememberNavController())
}