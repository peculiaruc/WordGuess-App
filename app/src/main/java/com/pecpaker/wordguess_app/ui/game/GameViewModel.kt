package com.pecpaker.wordguess_app.ui.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

class GameViewModel : ViewModel() {

    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    companion object {
        //Set timer to show the length of game time
        //when the game is over
        const val Done = 0L

        //the number of milliseconds
        const val ONE_SECOND = 1000L

        // the total time of the game
        const val COUNTDOWN_TIME = 10000L

        // This is the time when the phone will start buzzing each second
        private const val COUNTDOWN_PANIC_SECONDS = 10L
    }

    private var timer: CountDownTimer

    //TIMER
    private var _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    val currentTimesString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    //    The buzzer
    private var _buzzer = MutableLiveData<BuzzType>()
    val buzzType: LiveData<BuzzType>
        get() = _buzzer

    // The current word
    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word

    // The current score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _eventgameFinished = MutableLiveData<Boolean>()
    val gameFinished: LiveData<Boolean>
        get() = _eventgameFinished


    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    init {
        Log.i("GameViewModel", "GameViewModel created")
        resetList()
        nextWord()
        _score.value = 0
        _word.value = ""
        _eventgameFinished.value = false

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntiFinished: Long) {
                _currentTime.value = millisUntiFinished / ONE_SECOND
                if (millisUntiFinished / ONE_SECOND <= COUNTDOWN_PANIC_SECONDS)
                    _buzzer.value = BuzzType.COUNTDOWN_PANIC
            }

            override fun onFinish() {
                _currentTime.value = Done
                _eventgameFinished.value = true
                _buzzer.value = BuzzType.GAME_OVER
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
            _eventgameFinished.value = true
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
//    fun eventgameFinished(){
//        _eventgameFinished.value = true
//    }

    fun onGameFinishedCompleted() {
        _eventgameFinished.value = false
    }

    fun onBuzzFinished() {
        _buzzer.value = BuzzType.NO_BUZZ
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed")
    }
}