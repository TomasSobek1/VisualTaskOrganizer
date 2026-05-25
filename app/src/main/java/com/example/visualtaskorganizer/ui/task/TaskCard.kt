package com.example.visualtaskorganizer.ui.task

import android.content.ClipData
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.visualtaskorganizer.model.Task
import java.text.SimpleDateFormat
import java.util.Date
import androidx.compose.ui.platform.LocalLocale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskCard(task: Task, onDelete: () -> Unit) {
    // Formátovanie dátumu
    val dateFormatter = SimpleDateFormat("MMM dd", LocalLocale.current.platformLocale)
    val startDateStr = task.startDate?.let { dateFormatter.format(Date(it)) } ?: "---"
    val deadlineStr = task.deadline?.let { dateFormatter.format(Date(it)) } ?: "---"

    // Priorita - farby
    val priorityColor = when (task.priority) {
        1 -> Color(0xFF4CAF50)
        2 -> Color(0xFFFFC107)
        3 -> Color(0xFFF44336)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .dragAndDropSource { _ ->
                val clipData = ClipData.newPlainText("taskId", task.taskId.toString())
                DragAndDropTransferData(clipData = clipData)
            },
        colors = CardDefaults.cardColors(
            containerColor = Color(task.colorTag).copy(alpha = 0.15f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {

            // Indikátor priority
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(priorityColor)
                    .align(Alignment.TopEnd)
            )

            Column(modifier = Modifier.padding(end = 16.dp)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                if (task.description.isNotEmpty()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                SuggestionChip(
                    onClick = {},
                    label = { Text(task.label, style = MaterialTheme.typography.labelSmall) },
                    modifier = Modifier.height(30.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    DateColumn(title = "Start", date = startDateStr)
                    DateColumn(title = "Deadline", date = deadlineStr)
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.align(Alignment.End).size(24.dp)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                }
            }
        }
    }
}

@Composable
fun DateColumn(title: String, date: String) {
    Column {
        Text(title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        Text(date, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
    }
}