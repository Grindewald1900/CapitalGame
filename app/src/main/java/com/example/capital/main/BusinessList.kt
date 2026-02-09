package com.example.capital.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.capital.ui.state.BusinessUiState

@Composable
fun BusinessList(
    businesses: List<BusinessUiState>,
    onUpgrade: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(businesses, key = { it.id }) { biz ->
            BusinessCard(
                business = biz,
                onUpgrade = { onUpgrade(biz.id) }
            )
        }
    }
}