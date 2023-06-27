package com.example.current

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
fun MessagesScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        val messages = listOf(
            Message("Hi how have you been", "6/26/23", "me", "1"),
            Message("I have been good", "6/26/23", "other", "1")
        )

        HeaderSection()
        Conversation(messages)
        InputSection()

    }


}

@Composable
fun Conversation(
    messages: List<Message>
) {
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .border(2.dp, Color.Gray)
            ){
        items(messages) { message ->
            when (message.sender) {
                "me" -> {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = message.content,
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .background(Color.Blue, CircleShape)
                                .padding(8.dp)

                        )
                    }
                }
                "admin" -> {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = message.content,
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .background(Color.Yellow, CircleShape)
                                .padding(8.dp)
                        )
                    }
                }
                "other" -> {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = message.content,
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .background(Color.Gray, CircleShape)
                                .padding(8.dp)

                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InputSection() {
//    OutlinedTextField(
//        modifier = Modifier
//            .background(Color.White),
//        value = password,
//        onValueChange = { text ->
//            password = text
//        },
//        label = { Text("Password") }, // Hint for the password field
//    )
}

@Composable
fun HeaderSection() {
    Column() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.testpic),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .padding(8.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .background(Color.Transparent, shape = CircleShape)
                    .border(1.dp, Color.Red, CircleShape)
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "10",
                    color = Color.Red,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

    }


}

@Composable
@Preview
fun MessagesScreenPreview() {
    MessagesScreen(
        navController = rememberNavController()
    )
}