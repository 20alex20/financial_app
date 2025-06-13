package com.example.financial_app.features.categories.pres

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import com.example.financial_app.features.categories.data.CategoriesRepo
import com.example.financial_app.features.categories.domain.models.Category

class CategoriesViewModel : ViewModel() {
    private val _inputText = mutableStateOf("")
    val inputText: State<String> = _inputText

    private val _categories = mutableStateOf(listOf<Category>())
    val categories: State<List<Category>> = _categories

    fun setInputText(text: String) {
        _inputText.value = text
    }

    private fun loadCategories() {
        _categories.value = CategoriesRepo.getCategories()
    }

    init {
        loadCategories()
    }
}
