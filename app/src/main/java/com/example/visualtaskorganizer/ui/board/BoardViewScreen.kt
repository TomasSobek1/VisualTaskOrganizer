package com.example.visualtaskorganizer.ui.board

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.visualtaskorganizer.ui.AppViewModelProvider
import com.example.visualtaskorganizer.model.Column as EntityColumn
import androidx.compose.foundation.layout.Column as ComposeColumn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardViewScreen(
    boardId: Int,
    viewModel: BoardViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onAddTaskClick: () -> Unit
) {
    val columns by viewModel.getColumnsForBoard(boardId).collectAsState(initial = emptyList<EntityColumn>())

    Scaffold(
        topBar = { TopAppBar(title = { Text("Board Name") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTaskClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { padding ->
        Text("Počet stĺpcov: ${columns.size}", modifier = Modifier.padding(padding))
        LazyRow(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = columns, key = { it.columnId }) { column ->
                ColumnComponent(column = column)
            }
        }
    }
}

@Composable
fun ColumnComponent(column: EntityColumn) {
    Card(
        modifier = Modifier.fillMaxHeight().width(280.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        ComposeColumn(modifier = Modifier.padding(8.dp)) {
            Text(
                text = column.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}