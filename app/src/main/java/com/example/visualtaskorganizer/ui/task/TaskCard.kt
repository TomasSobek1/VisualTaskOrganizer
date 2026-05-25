package com.example.visualtaskorganizer.ui.task

import android.content.ClipData
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
fun TaskCard(task: Task) {
    val dateFormatter = SimpleDateFormat("MMM dd", LocalLocale.current.platformLocale)
    val startDateStr = task.startDate?.let { dateFormatter.format(Date(it)) } ?: "---"
    val deadlineStr = task.deadline?.let { dateFormatter.format(Date(it)) } ?: "---"

    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .dragAndDropSource { _ ->
                val clipData = ClipData.newPlainText("taskId", task.taskId.toString())
                DragAndDropTransferData(clipData = clipData)
            },
        colors = CardDefaults.cardColors(
            containerColor = Color(task.colorTag).copy(alpha = 0.1f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                )
            }

            SuggestionChip(
                onClick = {},
                label = { Text(task.label) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Start", style = MaterialTheme.typography.labelSmall)
                    Text(startDateStr, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                }
                Column {
                    Text("Deadline", style = MaterialTheme.typography.labelSmall)
                    Text(deadlineStr, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}