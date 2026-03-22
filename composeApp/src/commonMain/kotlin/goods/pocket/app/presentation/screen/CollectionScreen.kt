package goods.pocket.app.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import goods.pocket.app.domain.model.Item
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.*

@Composable
fun CollectionScreen(
    items: List<Item>,
    query: String,
    onQueryChange: (String) -> Unit,
    onItemClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(tr(Res.string.collection_search_label)) },
                placeholder = { Text(tr(Res.string.collection_search_placeholder)) },
            )
        }
        if (items.isEmpty()) {
            item {
                Text(
                    text = tr(Res.string.collection_empty_search),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        items(items, key = Item::id) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(item.id) },
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(text = item.name, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = tr(
                            Res.string.collection_series_category,
                            item.seriesName ?: tr(Res.string.common_unknown),
                            item.category,
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = tr(
                            Res.string.collection_status_qty,
                            item.status.localizedLabel(),
                            item.quantity.toString(),
                        ),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}
