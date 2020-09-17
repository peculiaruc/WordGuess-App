package com.pecpaker.wordguess_app.ui.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import java.lang.IllegalArgumentException

class ScoreViewModelFactory(private val finalSore: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
            return ScoreViewModel(finalSore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
