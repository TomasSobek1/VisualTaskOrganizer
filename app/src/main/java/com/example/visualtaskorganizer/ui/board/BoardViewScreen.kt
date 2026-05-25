package com.example.visualtaskorganizer.ui.board

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column as ColumnLayout
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.visualtaskorganizer.R
import com.example.visualtaskorganizer.model.Column as EntityColumn
import com.example.visualtaskorganizer.ui.AppViewModelProvider
import com.example.visualtaskorganizer.ui.task.TaskCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardViewScreen(
    boardId: Int,
    viewModel: BoardViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onAddTaskClick: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    // State
    val board by viewModel.getBoard(boardId).collectAsState(initial = null)
    val columns by viewModel.getColumnsForBoard(boardId).collectAsState(initial = emptyList())

    var showDialog by rememberSaveable { mutableStateOf(false) }
    var columnName by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(board?.title ?: stringResource(R.string.loading)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        }
    ) { padding ->
        LazyRow(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(columns, key = { it.columnId }) { column ->
                ColumnComponent(
                    column = column,
                    onAddTaskClick = onAddTaskClick
                )
            }
            // Tlačidlo na pridanie stĺpca
            item {
                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier.height(60.dp)
                ) {
                    Text(stringResource(R.string.add_column))
                }
            }
        }
    }

    // Dialóg pre pridanie stĺpca
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.new_column)) },
            text = {
                OutlinedTextField(
                    value = columnName,
                    onValueChange = { columnName = it },
                    singleLine = true,
                    label = { Text("Názov stĺpca") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (columnName.isNotBlank()) {
                        viewModel.addColumn(boardId, columnName)
                    }
                    columnName = ""
                    showDialog = false
                }) { Text(stringResource(R.string.add)) }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text(stringResource(R.string.cancel)) }
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColumnComponent(
    column: EntityColumn,
    onAddTaskClick: (Int) -> Unit,
    viewModel: BoardViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val tasks by viewModel.getTasksForColumn(column.columnId).collectAsState(initial = emptyList())
    var isHovering by remember { mutableStateOf(false) }

    // Drag and Drop logika implementovana cez AI
    val dndTarget = remember {
        object : DragAndDropTarget {
            override fun onDrop(event: DragAndDropEvent): Boolean {
                val clipData = event.toAndroidDragEvent().clipData
                val taskId = clipData.getItemAt(0).text.toString().toIntOrNull()

                if (taskId != null) {
                    viewModel.updateTaskColumn(taskId, column.columnId, column.boardId)
                    return true
                }
                return false
            }

            override fun onStarted(event: DragAndDropEvent) { isHovering = true }
            override fun onEntered(event: DragAndDropEvent) { isHovering = true }
            override fun onExited(event: DragAndDropEvent) { isHovering = false }
            override fun onEnded(event: DragAndDropEvent) { isHovering = false }
        }
    }

    Card(
        modifier = Modifier
            .width(280.dp)
            .fillMaxHeight()
            .padding(8.dp)
            .dragAndDropTarget(shouldStartDragAndDrop = { true }, target = dndTarget),
        colors = CardDefaults.cardColors(
            containerColor = if (isHovering) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        ColumnLayout(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)) {

            FloatingActionButton(
                onClick = { onAddTaskClick(column.columnId) },
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(Icons.Default.Add, stringResource(R.string.add_task))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = column.title, style = MaterialTheme.typography.titleLarge)
                IconButton(onClick = { viewModel.deleteColumn(column) }) {
                    Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete_column))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(items = tasks, key = { it.taskId }) { task ->
                    TaskCard(
                        task = task,
                        onDelete = { viewModel.deleteTask(task) }
                    )
                }
            }
        }
    }
}