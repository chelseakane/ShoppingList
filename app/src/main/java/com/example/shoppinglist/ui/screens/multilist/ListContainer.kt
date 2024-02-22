package com.example.shoppinglist.ui.screens.multilist

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shoppinglist.R
import com.example.shoppinglist.models.ListItemEntity
import com.example.shoppinglist.models.ListState
import com.example.shoppinglist.models.ListUpdateState
import com.example.shoppinglist.ui.screens.list.ShoppingListViewModel

@Composable
fun ListContainer(
    id: Int,
    modifier: Modifier = Modifier,
    onUpdate: (ListUpdateState) -> Unit
) {
    val shoppingListViewModel: ShoppingListViewModel = hiltViewModel()
    shoppingListViewModel.id = id
    val list = shoppingListViewModel.list.collectAsState()

    when (val state = list.value) {
        is ListState.Loading -> {
            // show loading indicator
        }
        is ListState.Loaded -> {
            ListContainerImpl(
                id = state.data.id,
                title = state.data.title ?: stringResource(id = R.string.default_list_title),
                items = state.data.items,
                onUpdate = onUpdate,
                modifier = modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun ListContainerImpl(
    id: Int,
    title: String,
    items: List<ListItemEntity>,
    modifier: Modifier = Modifier,
    onUpdate: (ListUpdateState) -> Unit
) {
    Box(
        modifier = modifier
            .clickable { onUpdate(ListUpdateState.Details(id)) }
            .border(width = 1.dp, color = Color.Black, shape = MaterialTheme.shapes.medium)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium
            )
            items.take(3).forEach { item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Canvas(
                        modifier = Modifier
                            .padding(start = 8.dp,end = 8.dp)
                            .size(6.dp)
                    ){
                        drawCircle(Color.Black)
                    }
                    Text(text = item.name)
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListContainerImplPreview() {
    val items = listOf(
        ListItemEntity(1, 1, "Apples", null),
        ListItemEntity(2, 1, "Bananas", 3),
        ListItemEntity(3, 1, "Oranges", 5),
    )
    ListContainerImpl(
        id= 1,
        title= "List 1",
        items = items,
        onUpdate = {},
        modifier = Modifier
    )
}