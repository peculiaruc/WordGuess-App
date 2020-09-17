package com.pecpaker.wordguess_app.ui.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore: Int) : ViewModel() {

    // Current score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _playAgainBtton = MutableLiveData<Boolean>()
    val payAgainButton: LiveData<Boolean>
        get() = _playAgainBtton

    init {
        Log.i("ScoreViewModel", "Final Score is $finalScore")
        _score.value = finalScore
    }

    fun playAgainButton() {
        _playAgainBtton.value = true
    }

    fun onPlayAgainComplete() {
        _playAgainBtton.value = false
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("ScoreViewModel", "scoreViewModel is called")
    }

}