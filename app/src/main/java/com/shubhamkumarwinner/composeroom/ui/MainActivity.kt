package com.shubhamkumarwinner.composeroom.ui

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.shubhamkumarwinner.composeroom.todo.data.Todo
import com.shubhamkumarwinner.composeroom.ui.theme.ComposeRoomTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val todoViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeRoomTheme {
                Surface(color = MaterialTheme.colors.background) {
                    AddToolbar()
                }
            }
        }
    }

    @Composable
    fun AddToolbar() {
        Scaffold(topBar = {
            TopAppBar(title = {
                Text(text = "Todo App")
            })
        },
            floatingActionButton = {
                val openDialog = remember{ mutableStateOf(false) }
                FloatingActionButton(onClick = { openDialog.value = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                    AddDialogBox(openDialog = openDialog)
                }
            }
        ) {
            RecyclerView(viewModel = todoViewModel)
        }
    }

    @Composable
    fun AddDialogBox(openDialog: MutableState<Boolean>) {
        val title = remember{ mutableStateOf("")}
        val description = remember{ mutableStateOf("")}
        if (openDialog.value){
            AlertDialog(
                onDismissRequest = { openDialog.value = false },
                title = { Text(text = "Todo") },
                text = {
                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                    ) {
                        OutlinedTextField(
                            value = title.value,
                            onValueChange = {
                                title.value = it
                            },
                            placeholder = {
                                Text(text = "Enter title")
                            },
                            label = {
                                Text(text = "Title")
                            },
                            modifier = Modifier.padding(10.dp)
                        )

                        OutlinedTextField(
                            value = description.value,
                            onValueChange = {
                                description.value = it
                            },
                            placeholder = {
                                Text(text = "Enter description")
                            },
                            label = {
                                Text(text = "Description")
                            },
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                },
                confirmButton = {
                    OutlinedButton(onClick = {
                        insert(title, description)
                        title.value = ""
                        description.value =""
                        openDialog.value = false
                    }) {
                        Text(text = "Save")
                    }
                }
            )
        }
    }

    private fun insert(title: MutableState<String>, description: MutableState<String>){
        lifecycleScope.launchWhenStarted {
            if (!TextUtils.isEmpty(title.value) && !TextUtils.isEmpty(description.value)){
                todoViewModel.insert(Todo(title = title.value, description = description.value))
                Toast.makeText(this@MainActivity, "Inserted...", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@MainActivity, "Empty...", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun RecyclerView(viewModel: MainViewModel) {
    LazyColumn(modifier = Modifier.padding(vertical = 10.dp)) {
        items(viewModel.response.value){ todo ->
            TodoItem(todo = todo)
        }
    }
}

@Composable
fun TodoItem(todo: Todo) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth(),
        elevation = 4.dp,
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = todo.title, fontWeight = FontWeight.ExtraBold)
            Text(text = todo.description, fontStyle = FontStyle.Italic)
        }
    }
}
