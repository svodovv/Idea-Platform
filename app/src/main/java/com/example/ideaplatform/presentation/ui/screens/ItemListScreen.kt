package com.example.ideaplatform.presentation.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ideaplatform.R
import com.example.ideaplatform.domain.model.ItemModel
import com.example.ideaplatform.presentation.ui.theme.IdeaPlatformTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListScreen(
    viewModel: ItemViewModel = hiltViewModel()
) {

    val state = viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.item_list),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            )
        },
    ) { paddingValues ->
        ItemListContent(paddingValues = paddingValues,
            itemState = state.value,
            onDeleteClick = { viewModel.sendIntent(ItemIntent.DeleteItem(it)) },
            onUpdateClick = { itemId, itemAmount ->
                viewModel.sendIntent(
                    ItemIntent.UpdateItem(
                        itemId = itemId, itemAmount = itemAmount
                    )
                )
            },
            onSearchValueChange = { viewModel.sendIntent(ItemIntent.SearchItem(it)) })
    }
}


@Composable
private fun ItemListContent(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    itemState: ItemState,
    onDeleteClick: (itemId: Int) -> Unit,
    onUpdateClick: (itemId: Int, itemAmount: Int) -> Unit,
    onSearchValueChange: (String) -> Unit
) {

    val updateDialogAlertState: MutableState<Triple<Boolean, Int?, Int?>> = remember {
        mutableStateOf(Triple(false, null, null))
    }
    val deleteDialogAlertState: MutableState<Pair<Boolean, Int?>> = remember {
        mutableStateOf(Pair(false, null))
    }



    Column(modifier = modifier) {

        if (updateDialogAlertState.value.first) {
            UpdateDialogAlert(
                modifier = Modifier,
                dialogState = updateDialogAlertState,
                onUpdateClick = onUpdateClick
            )
        }

        if (deleteDialogAlertState.value.first) {
            DeleteDialogAlert(
                modifier = Modifier,
                dialogState = deleteDialogAlertState,
                deleteButtonClick = onDeleteClick
            )
        }

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 8.dp)
        ) {
            item {
                SearchInput(
                    modifier = Modifier.fillMaxWidth(),
                    text = itemState.searchText,
                    onValueChange = onSearchValueChange
                )
            }
            itemState.itemList?.let { list ->
                items(list) { item ->

                    LazyColumnCard(modifier = Modifier, item = item, deleteButtonClick = { itemId ->
                        deleteDialogAlertState.value = Pair(true, itemId)
                    }, updateButtonClick = { itemId, itemAmount ->
                        updateDialogAlertState.value = Triple(true, itemId, itemAmount)
                    })

                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowTagsRow(modifier: Modifier = Modifier, tags: ArrayList<String>) {
    FlowRow(modifier = modifier) {
        tags.forEach { tagName ->
            Card(
                modifier = modifier.clickable {},
                border = BorderStroke(1.dp, Color.Black),
                shape = MaterialTheme.shapes.large

            ) {
                Box(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = tagName,
                        modifier = Modifier,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun LazyColumnCard(
    modifier: Modifier = Modifier,
    item: ItemModel,
    deleteButtonClick: (itemId: Int) -> Unit,
    updateButtonClick: (itemId: Int, itemAmount: Int) -> Unit
) {
    Card(
        modifier = modifier.padding(bottom = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.name, style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { updateButtonClick(item.id, item.amount) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_create_24),
                        contentDescription = stringResource(R.string.update_amount_icon),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                IconButton(onClick = { deleteButtonClick(item.id) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_delete_24),
                        contentDescription = stringResource(R.string.delete_item_icon),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }

            FlowTagsRow(modifier = Modifier.padding(4.dp), tags = item.tags)

            Row {
                Column {
                    Text(
                        text = stringResource(R.string.in_storage),
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = item.amount.toString(), style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Column(modifier = Modifier.padding(end = 36.dp)) {
                    Text(
                        text = stringResource(R.string.date_update),
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = item.time, style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchInput(
    modifier: Modifier = Modifier, text: String, onValueChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Spacer(modifier = Modifier.padding(4.dp))

    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = onValueChange,
        label = { Text(text = stringResource(R.string.searcch_item)) },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_search_24),
                contentDescription = stringResource(R.string.search_icon),
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
        })
    )

    Spacer(modifier = Modifier.padding(16.dp))
}

@Composable
private fun UpdateDialogAlert(
    modifier: Modifier = Modifier,
    dialogState: MutableState<Triple<Boolean, Int?, Int?>>,
    onUpdateClick: (itemId: Int, itemAmount: Int) -> Unit
) {

    val closeDialog = { dialogState.value = Triple(false, null, null) }
    val color = MaterialTheme.colorScheme.onPrimaryContainer

    AlertDialog(modifier = modifier, onDismissRequest = { closeDialog() }, icon = {
        Icon(
            painter = painterResource(id = R.drawable.baseline_settings_24),
            contentDescription = stringResource(R.string.settings_icon),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }, title = {

        Spacer(modifier = Modifier.padding(8.dp))
        Text(text = stringResource(R.string.item_amout))
    }, text = {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                dialogState.value = dialogState.value.copy(
                    third = dialogState.value.third?.minus(1)
                )
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_remove_circle_outline_24),
                    contentDescription = stringResource(R.string.amount_minus),
                    tint = color
                )
            }
            dialogState.value.third?.let { amount ->
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "$amount",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            IconButton(onClick = {
                dialogState.value = dialogState.value.copy(
                    third = dialogState.value.third?.plus(1)
                )
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_add_circle_outline_24),
                    contentDescription = stringResource(R.string.amount_plus),
                    tint = color
                )
            }
        }
    }, confirmButton = {

        Text(
            modifier = Modifier
                .padding(start = 16.dp)
                .clickable {
                    dialogState.value.second?.let { itemId ->
                        dialogState.value.third?.let { itemAmount ->
                            onUpdateClick(itemId, itemAmount)
                            closeDialog()
                        }
                    }
                }, text = stringResource(R.string.submint), color = color
        )


    }, dismissButton = {

        Text(
            modifier = Modifier.clickable { closeDialog() },
            text = stringResource(R.string.dismiss),
            color = color
        )

    })
}

@Composable
private fun DeleteDialogAlert(
    modifier: Modifier = Modifier,
    dialogState: MutableState<Pair<Boolean, Int?>>,
    deleteButtonClick: (itemId: Int) -> Unit,
) {
    val closeDialog = { dialogState.value = Pair(false, null) }
    val color = MaterialTheme.colorScheme.onPrimaryContainer

    AlertDialog(modifier = modifier, onDismissRequest = { closeDialog() }, icon = {
        Icon(
            modifier = Modifier.padding(bottom = 16.dp),
            painter = painterResource(id = R.drawable.round_warning_24),
            contentDescription = stringResource(
                R.string.warning_icon
            )
        )
    }, title = {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.delete_item),
            textAlign = TextAlign.Center
        )

    }, text = { Text(stringResource(R.string.you_really_want_delete_item)) }, confirmButton = {
        Text(
            modifier = Modifier
                .padding(start = 16.dp)
                .clickable {
                    dialogState.value.second?.let { deleteButtonClick(it) }
                    closeDialog()
                }, text = stringResource(R.string.yes), color = color
        )

    }, dismissButton = {
        Text(
            modifier = Modifier.clickable { closeDialog() },
            text = stringResource(R.string.no),
            color = color
        )
    }

    )
}


@Preview
@Composable
fun UpdateDialogAlertPreview() {
    IdeaPlatformTheme {
        val dialogState: MutableState<Triple<Boolean, Int?, Int?>> = remember {
            mutableStateOf(Triple(true, 22, 30))
        }
        UpdateDialogAlert(dialogState = dialogState, onUpdateClick = { _, _ -> })
    }
}

@Preview
@Composable
fun DeleteDialogAlertPreview() {
    IdeaPlatformTheme {
        val dialogState: MutableState<Pair<Boolean, Int?>> = remember {
            mutableStateOf(Pair(true, 22))
        }
        DeleteDialogAlert(dialogState = dialogState, deleteButtonClick = { _ -> })
    }
}