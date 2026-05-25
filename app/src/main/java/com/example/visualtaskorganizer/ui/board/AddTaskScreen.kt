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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.visualtaskorganizer.R
import com.example.visualtaskorganizer.model.Task
import com.example.visualtaskorganizer.ui.AppViewModelProvider
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    columnId: Int,
    onNavigateBack: () -> Unit,
    viewModel: BoardViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // State pre formulár
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var label by rememberSaveable { mutableStateOf("") }
    var priority by rememberSaveable { mutableStateOf("Medium") }
    var expandedPriority by rememberSaveable { mutableStateOf(false) }

    val taskColors = listOf(
        Color(0xFFE57373), Color(0xFF81C784), Color(0xFF64B5F6),
        Color(0xFFBA68C8), Color(0xFFFFB74D), Color(0xFF4DB6AC),
        Color(0xFFFFF176), Color(0xFF9575CD)
    )
    var selectedColor by rememberSaveable { mutableStateOf(taskColors[0].toArgb()) }

    val formatter = remember { SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()) }
    var startDate by rememberSaveable { mutableStateOf<Long?>(null) }
    var deadline by rememberSaveable { mutableStateOf<Long?>(null) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var isSelectingDeadline by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.new_task), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
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
                label = { Text(stringResource(R.string.task_title)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.description)) },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(12.dp)
            )

            // Dátumy
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DateInputField(
                    label = stringResource(R.string.start_date),
                    date = startDate,
                    formatter = formatter,
                    modifier = Modifier.weight(1f),
                    onClick = { isSelectingDeadline = false; showDatePicker = true }
                )
                DateInputField(
                    label = stringResource(R.string.deadline),
                    date = deadline,
                    formatter = formatter,
                    modifier = Modifier.weight(1f),
                    onClick = { isSelectingDeadline = true; showDatePicker = true }
                )
            }

            PriorityDropdown(
                selectedPriority = priority,
                expanded = expandedPriority,
                onExpandedChange = { expandedPriority = it },
                onPrioritySelected = { priority = it }
            )

            OutlinedTextField(
                value = label,
                onValueChange = { label = it },
                label = { Text(stringResource(R.string.label)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            ColorPickerSection(
                colors = taskColors,
                selectedColorArgb = selectedColor,
                onColorSelected = { selectedColor = it.toArgb() }
            )

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
                            colorTag = selectedColor,
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
                Text(stringResource(R.string.save_task), fontSize = 18.sp, color = Color.White)
            }
        }
    }

    // Date picker dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val date = datePickerState.selectedDateMillis
                    if (isSelectingDeadline) deadline = date else startDate = date
                    showDatePicker = false
                }) { Text(stringResource(R.string.confirm)) }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

// Komponent pre dátumové polia
@Composable
fun DateInputField(label: String, date: Long?, formatter: SimpleDateFormat, modifier: Modifier, onClick: () -> Unit) {
    OutlinedTextField(
        value = date?.let { formatter.format(Date(it)) } ?: "mm/dd/yyyy",
        onValueChange = {},
        label = { Text(label) },
        readOnly = true,
        modifier = modifier.clickable { onClick() },
        trailingIcon = { Icon(Icons.Default.DateRange, null) },
        enabled = false,
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

// Komponent pre výber priority
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityDropdown(selectedPriority: String, expanded: Boolean, onExpandedChange: (Boolean) -> Unit, onPrioritySelected: (String) -> Unit) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        OutlinedTextField(
            value = selectedPriority,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.priority)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true).fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            listOf("Low", "Medium", "High").forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onPrioritySelected(option)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}

// Komponent pre výber farby
@Composable
fun ColorPickerSection(colors: List<Color>, selectedColorArgb: Int, onColorSelected: (Color) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.task_color), fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            colors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = if (selectedColorArgb == color.toArgb()) 3.dp else 0.dp,
                            color = if (selectedColorArgb == color.toArgb()) Color(0xFF6750A4) else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable { onColorSelected(color) }
                )
            }
        }
    }
}