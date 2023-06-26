package com.example.current

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreen(
    navController: NavController
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        HomeNav()
        ConnectionsFeed()
    }
}

@Composable
fun TitleText(text: String) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 16.sp
    )
}

@Composable
fun HomeNav() {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(top = 10.dp)
    ) {
        TitleText(text = "Connections")
        Divider(
            color = Color.White,
            modifier = Modifier
                .fillMaxHeight()  //fill the max height
                .width(1.dp)
        )
        TitleText(text = "Memories")
    }
}

@Composable
fun ConnectionsFeed() {

    var connections by remember {
        mutableStateOf(listOf<String>())
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(20.dp)
            .border(2.dp, Color.White)
    ) {
        ConnectionCard(R.drawable.testpic, "How have you been?", "9:15pm", "10")
    }


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ConnectionCard(
    profilePic: Int,
    recentMsg: String,
    recentTime: String,
    streak: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color.Black),

        ) {
            Image(
                painter = painterResource(profilePic),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .padding(8.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
            ) {
                Text(
                    text = recentMsg,
                    color = Color.White,
                    fontSize = 14.sp
                )
                Text(
                    text = recentTime,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Box(
                modifier = Modifier
                    .background(Color.Transparent, shape = CircleShape)
                    .border(1.dp, Color.Red, CircleShape)
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = streak,
                    color = Color.Red,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

    }

}

@Composable
fun TopBar() {
    
}



@Composable
@Preview
fun HomeScreenPreview() {
    HomeScreen(
        navController = rememberNavController()
    )
}