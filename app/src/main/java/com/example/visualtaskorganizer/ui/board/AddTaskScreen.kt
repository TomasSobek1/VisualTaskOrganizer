package com.example.visualtaskorganizer.ui.board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.visualtaskorganizer.model.Task
import com.example.visualtaskorganizer.ui.AppViewModelProvider
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.platform.LocalLocale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    columnId: Int,
    onNavigateBack: () -> Unit,
    viewModel: BoardViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var label by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("Medium") }
    var expandedPriority by remember { mutableStateOf(false) }

    val taskColors = listOf(
        Color(0xFFE3F2FD), Color(0xFFF3E5F5), Color(0xFFE8EAF6),
        Color(0xFFFFEBEE), Color(0xFFFCE4EC), Color(0xFFE0F2F1),
        Color(0xFFFFF3E0), Color(0xFFFFF9C4)
    )
    var selectedColor by remember { mutableStateOf(taskColors[0]) }

    val formatter = SimpleDateFormat("MM/dd/yyyy", LocalLocale.current.platformLocale)
    var startDate by remember { mutableStateOf<Long?>(null) }
    var deadline by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var isSelectingDeadline by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("New Task", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Task Title") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(12.dp)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = startDate?.let { formatter.format(Date(it)) } ?: "mm/dd/yyyy",
                    onValueChange = {},
                    label = { Text("Start Date") },
                    readOnly = true,
                    modifier = Modifier.weight(1f).clickable { isSelectingDeadline = false; showDatePicker = true },
                    trailingIcon = { Icon(Icons.Default.DateRange, null) },
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                OutlinedTextField(
                    value = deadline?.let { formatter.format(Date(it)) } ?: "mm/dd/yyyy",
                    onValueChange = {},
                    label = { Text("Deadline") },
                    readOnly = true,
                    modifier = Modifier.weight(1f).clickable { isSelectingDeadline = true; showDatePicker = true },
                    trailingIcon = { Icon(Icons.Default.DateRange, null) },
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            ExposedDropdownMenuBox(
                expanded = expandedPriority,
                onExpandedChange = { expandedPriority = !expandedPriority }
            ) {
                OutlinedTextField(
                    value = priority,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Priority") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPriority) },
                    modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true).fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expandedPriority,
                    onDismissRequest = { expandedPriority = false }
                ) {
                    listOf("Low", "Medium", "High").forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                priority = selectionOption
                                expandedPriority = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = label,
                onValueChange = { label = it },
                label = { Text("Label") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Task Color", fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    taskColors.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (selectedColor == color) 3.dp else 0.dp,
                                    color = if (selectedColor == color) Color(0xFF6750A4) else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { selectedColor = color }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val priorityInt = when(priority) {
                        "Low" -> 1
                        "High" -> 3
                        else -> 2
                    }
                    viewModel.addTask(
                        Task(
                            columnId = columnId,
                            title = title,
                            description = description,
                            startDate = startDate,
                            deadline = deadline,
                            priority = priorityInt,
                            colorTag = selectedColor.toArgb(),
                            label = label,
                            is_completed = false
                        )
                    )
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4))
            ) {
                Text("Save Task", fontSize = 18.sp, color = Color.White)
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val date = datePickerState.selectedDateMillis
                    if (isSelectingDeadline) deadline = date else startDate = date
                    showDatePicker = false
                }) { Text("Confirm") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}