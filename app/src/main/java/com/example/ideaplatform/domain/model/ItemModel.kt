package com.example.ideaplatform.domain.model


data class ItemModel (
    val id: Int,
    val name: String,
    val time: String,
    val tags: ArrayList<String>,
    val amount: Int
)

