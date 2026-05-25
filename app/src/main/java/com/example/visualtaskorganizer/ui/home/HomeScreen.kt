package com.example.visualtaskorganizer.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.visualtaskorganizer.R
import com.example.visualtaskorganizer.model.Board
import com.example.visualtaskorganizer.ui.AppViewModelProvider
import com.example.visualtaskorganizer.ui.board.BoardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onBoardClick: (Int) -> Unit,
    viewModel: BoardViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // State
    val boards by viewModel.allBoards.collectAsState(initial = emptyList())
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var boardName by rememberSaveable { mutableStateOf("") }
    var selectedColor by rememberSaveable { mutableLongStateOf(0xFF6750A4) }

    val colorOptions = listOf(0xFF6750A4, 0xFFF44336, 0xFF4CAF50, 0xFF2196F3, 0xFFFF9800, 0xFF9C27B0)

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.my_plans)) }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_board))
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(boards) { board ->
                BoardCard(
                    board = board,
                    onClick = { onBoardClick(board.boardId) },
                    onDelete = { viewModel.deleteBoard(board) }
                )
            }
        }
    }

    // Dialóg pre pridanie boardu
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.new_board)) },
            text = {
                Column {
                    OutlinedTextField(
                        value = boardName,
                        onValueChange = { boardName = it },
                        label = { Text(stringResource(R.string.project_name)) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(stringResource(R.string.select_color))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        colorOptions.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(color))
                                    .border(
                                        width = if (selectedColor == color) 3.dp else 0.dp,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        shape = CircleShape
                                    )
                                    .clickable { selectedColor = color }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (boardName.isNotBlank()) {
                        viewModel.addBoard(boardName, selectedColor.toInt())
                        boardName = ""
                        showDialog = false
                    }
                }) { Text(stringResource(R.string.create)) }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text(stringResource(R.string.cancel)) }
            }
        )
    }
}

@Composable
fun BoardCard(
    board: Board,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.height(70.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Farebný prúžok
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .fillMaxHeight()
                    .background(Color(board.colorTheme.takeIf { it != 0 } ?: 0xFF6750A4.toInt()))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = board.title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete_board))
            }
        }
    }
}