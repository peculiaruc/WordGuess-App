package com.pecpaker.wordguess_app.ui.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.pecpaker.wordguess_app.R
import com.pecpaker.wordguess_app.databinding.FragmentGame2Binding
import java.util.EnumSet.of
import java.util.List.of


/**
 * A simple [Fragment] subclass.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameFragment : Fragment() {


    private lateinit var viewModel: GameViewModel
    private lateinit var binding: FragmentGame2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game2, container, false)

        viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        binding.gameViewModel = viewModel

        binding.correctButton.setOnClickListener { viewModel.onCorrect() }

        binding.skipButton.setOnClickListener { viewModel.onSkip() }

        //Observe the LiveData that is set up LiveData Observation relationship

        viewModel.score.observe(viewLifecycleOwner, Observer { newScore ->
            binding.scoreText.text = newScore.toString()
        })

        viewModel.word.observe(viewLifecycleOwner, Observer { newWord ->
            binding.wordText.text = newWord
        })

        viewModel.gameFinished.observe(viewLifecycleOwner, Observer { hasFinished ->
            if (hasFinished) {
                gameFinished()
                viewModel.onGameFinishedCompleted()
            }
        })

        viewModel.currentTime.observe(viewLifecycleOwner, Observer { currentTime ->
        })
        return binding.root
    }

    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
        val action = GameFragmentDirections.actionGameToScore()
        val currentScore = viewModel.score.value ?: 0
        action.setScore(currentScore)
        findNavController().navigate(action)

    }

}