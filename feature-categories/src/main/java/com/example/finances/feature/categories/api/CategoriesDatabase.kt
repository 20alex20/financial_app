package com.example.finances.feature.categories.api

import com.example.finances.feature.categories.data.database.CategoriesDao

interface CategoriesDatabase {
    fun categoryDao(): CategoriesDao
}
