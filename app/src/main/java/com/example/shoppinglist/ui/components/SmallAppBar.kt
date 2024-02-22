package com.example.shoppinglist.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import com.example.shoppinglist.R
import com.example.shoppinglist.models.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallTopAppBar(
    title: String,
    updateTitle: (String) -> Unit,
    onNavigate: (Destination) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            var isEditMode by remember { mutableStateOf(false) }
            var currentTitle by remember { mutableStateOf(title) }
            val focusRequester = remember { FocusRequester() }
            // Show the cursor at the end of the text field initially AND allow the user to move it
            var textFieldValueState by remember {
                mutableStateOf(
                    TextFieldValue(
                        text = currentTitle,
                        selection = TextRange(
                            index = currentTitle.length
                        )
                    )
                )
            }

            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    if (isEditMode) {
                        TextField(
                            value = textFieldValueState,
                            onValueChange = {
                                currentTitle = it.text
                                textFieldValueState = it
                            },
                            singleLine = true,
                            maxLines = 1,
                            modifier = Modifier.focusRequester(focusRequester = focusRequester),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                        LaunchedEffect(Unit) {
                            focusRequester.requestFocus()
                        }
                    } else {
                        Text(currentTitle, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                },
                actions = {
                    if (isEditMode) {
                        IconButton(
                            onClick = {
                                isEditMode = !isEditMode
                                updateTitle(currentTitle)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = stringResource(R.string.edit_title_confirm_description)
                            )
                        }
                    } else {
                        IconButton(onClick = { isEditMode = !isEditMode }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = stringResource(R.string.edit_title_description)
                            )
                        }
                    }
                },
                // TODO: Add navigation icon once there's content to navigate back to
                navigationIcon = {
                    IconButton(onClick = {
                        onNavigate(Destination.MultiListScreen)
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}