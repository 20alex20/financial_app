package com.example.finances.features.transactions.ui.models

data class CreateUpdateViewModelState(
    val categoryName: String,
    val categoryEmoji: String,
    val amount: String,
    val date: String,
    val time: String,
    val comment: String
)
