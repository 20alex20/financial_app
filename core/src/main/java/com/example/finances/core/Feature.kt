package com.example.finances.core

import com.example.finances.core.navigation.FeatureNavigation
import com.example.finances.core.utils.viewmodel.ViewModelMapProvider

interface Feature: FeatureNavigation {
    fun getViewModelMapProvider(): ViewModelMapProvider
}
