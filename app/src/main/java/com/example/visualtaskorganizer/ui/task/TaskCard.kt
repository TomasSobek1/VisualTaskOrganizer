package com.example.visualtaskorganizer.ui.task

import android.content.ClipData
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.visualtaskorganizer.model.Task

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskCard(task: Task) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .dragAndDropSource { offset ->
                val clipData = ClipData.newPlainText("taskId", task.taskId.toString())
                DragAndDropTransferData(clipData = clipData)
            },
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = task.title, style = MaterialTheme.typography.titleMedium)

            SuggestionChip(onClick = {}, label = { Text(task.label) })

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column {
                    Text("Start", style = MaterialTheme.typography.labelSmall)
                    Text("Apr 12", style = MaterialTheme.typography.bodySmall)
                }
                Column {
                    Text("Deadline", style = MaterialTheme.typography.labelSmall)
                    Text("Apr 18", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}