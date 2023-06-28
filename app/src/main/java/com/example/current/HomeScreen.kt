package com.example.current

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.DatabaseError

private var firebaseManager = FirebaseManager()

@Composable
fun HomeScreen(
    navController: NavController
) {

    val showConnectionPopup = remember { mutableStateOf(false) }
    val alpha = animateFloatAsState(
        targetValue = if (showConnectionPopup.value) 1f else 0f,
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
                .align(Alignment.TopCenter)
        ) {
            TopBar { showConnectionPopup.value = !(showConnectionPopup.value)}
            HomeNav()
            ConnectionsFeed()
        }

        if (showConnectionPopup.value) {
            ConnectionPopup(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .alpha(alpha.value),
                onCloseClick = {
                    showConnectionPopup.value = !(showConnectionPopup.value)
                }
            )
        }

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
fun TopBar(onAddClick: () -> Unit) {


    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        Icon(
            Icons.Filled.Add,
            contentDescription = "Add",
            tint = Color.White,
            modifier = Modifier
                .padding(8.dp)
                .size(30.dp)
                .clickable { onAddClick() }
        )
        Image(
            painter = painterResource(R.drawable.testpic),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .padding(8.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }

}

@Composable
fun ConnectionPopup(modifier: Modifier? = null, onCloseClick: () -> Unit) {
    var searchText by remember { mutableStateOf("") }
    var userList by remember { mutableStateOf(listOf<Pair<String,String>>())}
    var includedUsers by remember { mutableStateOf(listOf<Pair<String, String>>())}

    firebaseManager.fetchUsersBySearchText(searchText, object: FirebaseManager.FetchUsersBySearchTextListener {
        override fun onSuccess(users: List<Pair<String, String>>) {
            userList = users
        }

        override fun onError(databaseError: DatabaseError) {
            TODO("Not yet implemented")
        }
    })

    Card(
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(15.dp)
    ) {
        Column(
            // Column and other modifiers
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
            ) {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text(text = "Search usernames", color = Color.White) },
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .background(Color.Transparent)
                        .fillMaxWidth()
                )
            }

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
            ) {
                includedUsers.forEach { user ->
                    Text(
                        text = user.second,
                        color = Color.White
                    )
                }
            }



            LazyColumn (
                modifier = Modifier.background(Color.Black)
                    ) {
                items(userList) { user ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(
                            text = user.second,
                            color = Color.White
                        )

                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Add",
                            tint = Color.White,
                            modifier = Modifier
                                .padding(8.dp)
                                .size(20.dp)
                                .clickable { includedUsers += user}

                        )
                    }
                    Divider(color = Color.White)
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
            ) {
                Button(onClick = { firebaseManager.createNewConnection(includedUsers) }) {
                    Text(text = "Create")
                }
                Button(onClick = { onCloseClick() }) {
                    Text(text = "Cancel")
                }
            }

        }
    }
}





@Composable
@Preview
fun HomeScreenPreview() {
    HomeScreen(
        navController = rememberNavController()
    )
}