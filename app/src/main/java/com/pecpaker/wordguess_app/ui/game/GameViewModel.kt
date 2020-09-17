package com.pecpaker.wordguess_app.ui.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    companion object {
        //Set timer to show the length of game time

        //when the game is over
        private const val Done = 0L

        //the number of milliseconds
        private const val ONE_SECOND = 1000L

        // the total time of the game
        private const val COUNTDOWN_TIME = 10000L
    }

    private var timer: CountDownTimer

    //TIMER

    val currentTime: LiveData<Long>
        get() = _currentTime

    private var _currentTime = MutableLiveData<Long>()
    val currentTimes = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    // The current word
    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word

    // The current score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _gameFinished = MutableLiveData<Boolean>()
    val gameFinished: LiveData<Boolean>
        get() = _gameFinished


    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    init {
        Log.i("GameViewModel", "GameViewModel created")
        resetList()
        nextWord()
        _score.value = 0
        _word.value = ""
        _gameFinished.value = false

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntiFinished: Long) {
                _currentTime.value = millisUntiFinished / ONE_SECOND
                //     if (millisUntiFinished / ONE_SECOND <= COUNTDOWN_PANIC_SECOND)
            }

            override fun onFinish() {
                _currentTime.value = Done
                _gameFinished.value = true
            }
        }
        timer.start()
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
            "queen", "hospital", "basketball", "cat", "change", "snail", "soup",
            "calendar", "sad", "desk", "guitar", "home", "railway", "zebra", "jelly",
            "car", "crow", "trade", "bag", "roll", "bubble", "Kotlin", "Android", "LiveData",
            "great", "amazing", "you're smart"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
            _gameFinished.value = true
        }
        _word.value = wordList.removeAt(0)
    }

    //  ** Methods for buttons presses **/

    fun onSkip() {
        _score.value = score.value?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = score.value?.plus(1)
        nextWord()
    }

    fun onGameFinishedCompleted() {
        _gameFinished.value = false
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed")
    }
}