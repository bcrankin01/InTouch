package com.example.current

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private var firebaseManager = FirebaseManager()
private var currentUser = Firebase.auth.currentUser

@Composable
fun MessagesScreen(
    navController: NavController,
    messageGroupId: String
) {
    var messages by remember {
        mutableStateOf(listOf<Message>())
    }

    firebaseManager.fetchMessagesByGroupId(messageGroupId, object: FirebaseManager.FetchMessagesByGroupIdListener {
        override fun onSuccess(the_messages: List<Message>) {
            messages = the_messages
        }

        override fun onError(errorMessage: String) {
            TODO("Not yet implemented")
        }
    })


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        HeaderSection()
        Divider()
        Spacer(modifier = Modifier.weight(1f))
        Conversation(messages)
        Divider()
        InputSection(messageGroupId) {
            firebaseManager.fetchMessagesByGroupId(messageGroupId, object: FirebaseManager.FetchMessagesByGroupIdListener {
                override fun onSuccess(the_messages: List<Message>) {
                    messages = the_messages
                }

                override fun onError(errorMessage: String) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}

private fun loadMessages(messageGroupId: String): List<Message> {
    var myMessages = emptyList<Message>()

    return myMessages
}



@Composable
fun Conversation(
    messages: List<Message>
) {
    LazyColumn (
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            ){
        items(messages) { message ->
            when (message.userId) {
                "${currentUser?.uid.toString()}" -> {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
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
                else -> {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = message.content,
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .background(Color.Green, CircleShape)
                                .padding(8.dp)

                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InputSection(
    messageGroupId: String,
    reload: () -> Unit
) {
    var msg by remember {
        mutableStateOf("")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Input text field
        TextField(
            value = msg, // Replace with your state for the input text
            onValueChange = { text ->
                msg = text
            },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            placeholder = {
                Text(
                    text = "Type a message",
                    color = Color.White // Set the placeholder color
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent, // Set the input field background color
                cursorColor = Color.White, // Set the cursor color
                focusedIndicatorColor = Color.Transparent, // Set the focused indicator color
                unfocusedIndicatorColor = Color.Transparent // Set the unfocused indicator color
            ),
            textStyle = LocalTextStyle.current.copy(color = Color.White) // Set the input text color
        )

        // Send button
        Button(
            onClick = {
                firebaseManager.sendMessage(msg, messageGroupId)
                reload()
                msg = ""
                      },
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text("Send")
        }
    }
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
        navController = rememberNavController(),
        messageGroupId = ""
    )
}