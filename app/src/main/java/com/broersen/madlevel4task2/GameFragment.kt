package com.broersen.madlevel4task2

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class GameFragment : Fragment() {
    companion object{
        val choiceRock = 0
        val choicePaper = 1
        val choiceScissor = 2

        val moves: Array<Map<String, String>> = arrayOf(
            mapOf(
                "move" to "rock",
                "beats" to "scissors",
                "losesTo" to "paper"
            ),
            mapOf(
                "move" to "paper",
                "beats" to "rock",
                "losesTo" to "scissors"
            ),
            mapOf(
                "move" to "scissors",
                "beats" to "paper",
                "losesTo" to "rock"
            )
        )
    }


    private lateinit var gameRepository: GameRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameRepository = GameRepository(requireContext())

        setupButtons()

        getGamesFromDatabase()
    }

    fun getImageOfMove(move: String) : Drawable?{
        when (move) {
            moves[choiceRock]["move"] -> {
                return ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.rock
                )
            }
            moves[choiceScissor]["move"] -> {
                return ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.scissors
                )
            }
            moves[choicePaper]["move"] -> {
                return ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.paper
                )
            }
            else -> {
                return null
            }
        }
    }

    private fun setupButtons() {
        btn_paper.setOnClickListener {
            startGame(choicePaper)
        }

        btn_rock.setOnClickListener {
            startGame(choiceRock)
        }

        btn_scissors.setOnClickListener {
            startGame(choiceScissor)
        }
    }

    fun startGame(move : Int){
        val computersMove = generateComputerMove()
        val playersMove = moves[move]
        if (playersMove["move"]?.equals(computersMove["move"])!!){
            tv_resultLabel.text = getString(R.string.draw)
        }
        if (playersMove["losesTo"]?.equals(computersMove["move"])!!){
            tv_resultLabel.text = getString(R.string.lose)
        }
        if (playersMove["beats"]?.equals(computersMove["move"])!!){
            tv_resultLabel.text = getString(R.string.win)
        }

        iv_computer_move.setImageDrawable(getImageOfMove(computersMove["move"] ?: error("")))
        iv_your_move.setImageDrawable(getImageOfMove(playersMove["move"] ?: error("")))

        mainScope.launch {
            withContext(Dispatchers.IO) {
                val simpleDateFormat = SimpleDateFormat.getDateTimeInstance()

                gameRepository.insertGame(
                    Game(
                        simpleDateFormat.format(Date()),
                        playersMove["move"] ?: error("Empty string"),
                        computersMove["move"] ?: error("Empty string"),
                        tv_resultLabel.text.toString()
                    )
                )
            }
        }
        getGamesFromDatabase()
    }

    private fun getGamesFromDatabase() {
        mainScope.launch {
            val games = withContext(Dispatchers.IO) {
                gameRepository.getAllGames()
            }

            var win = 0
            var lose = 0
            var draw = 0

            for (game in games) {
                when (game.result) {
                    getString(R.string.win) -> {
                        win++
                    }
                    getString(R.string.lose) -> {
                        lose++
                    }
                    getString(R.string.draw) -> {
                        draw++
                    }
                }
            }

            tv_statisticsLabelResult.text = getString(R.string.statistics_result , win, draw, lose)
        }
    }

    private fun generateComputerMove() : Map<String, String> {
        val index = (moves.indices).random()
        return moves[index]
    }
}