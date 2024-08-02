package com.example.todolistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolistapp.ui.theme.ToDoListAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    TodoApp()
                }
            }
        }
    }
}

@Composable
fun TodoApp() {
    val todoList = remember { mutableStateListOf<String>() }
    var newTask by remember { mutableStateOf(TextFieldValue("")) }
    var editingTask by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            BasicTextField(
                value = newTask,
                onValueChange = { newTask = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            Button(onClick = {
                if (newTask.text.isNotEmpty()) {
                    if (editingTask != null) {
                        val index = todoList.indexOf(editingTask)
                        if (index != -1) {
                            todoList[index] = newTask.text
                        }
                        editingTask = null
                    } else {
                        todoList.add(newTask.text)
                    }
                    newTask = TextFieldValue("")
                }
            }) {
                Text(if (editingTask != null) "Update" else "Add")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        TodoList(todoList, onEdit = { task ->
            newTask = TextFieldValue(task)
            editingTask = task
        }, onDelete = { task ->
            todoList.remove(task)
        })
    }
}

@Composable
fun TodoList(todoList: List<String>, onEdit: (String) -> Unit, onDelete: (String) -> Unit) {
    Column {
        for (task in todoList) {
            TodoItem(task, onEdit, onDelete)
        }
    }
}

@Composable
fun TodoItem(task: String, onEdit: (String) -> Unit, onDelete: (String) -> Unit) {
    var isCompleted by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = task,
            style = if (isCompleted) {
                MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
            } else {
                MaterialTheme.typography.bodyLarge
            }
        )
        Row {
            Checkbox(
                checked = isCompleted,
                onCheckedChange = { isCompleted = it }
            )
            IconButton(onClick = { onEdit(task) }) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = { onDelete(task) }) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ToDoListAppTheme {
        TodoApp()
    }
}

