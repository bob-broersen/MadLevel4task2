package com.broersen.madlevel4task2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


//class Game(move: Int) {
//    companion object{
//        val choiceRock = 0
//        val choicePaper = 1
//        val choiceScissor = 2
//    }
//
//    var result: String = ""
//
//    val moves: Array<Map<String, String>> = arrayOf(
//        mapOf(
//            "move" to "rock",
//            "beats" to "scissors",
//            "losesTo" to "paper"
//        ),
//        mapOf(
//            "move" to "paper",
//            "beats" to "rock",
//            "losesTo" to "scissors"
//        ),
//        mapOf(
//            "move" to "scissors",
//            "beats" to "paper",
//            "losesTo" to "rock"
//        )
//    )
//
//    private val playersMove = moves[move]
//
//
//    fun startGame(){
//        val computersMove = generateComputerMove()
//        if (playersMove["move"]?.equals(computersMove["move"])!!){
//            result = "Draw"
//        }
//        if (playersMove["losesTo"]?.equals(computersMove["move"])!!){
//            result = "Computer wins!"
//        }
//        if (playersMove["beats"]?.equals(computersMove["move"])!!){
//            result = "You win!"
//        }
//    }
//
//    private fun generateComputerMove() : Map<String, String> {
//        val index = (moves.indices).random()
//        return moves[index]
//    }
//
//}

@Entity(tableName = "game_table")
data class Game (
    @ColumnInfo(name="date")
    var date: String,

    @ColumnInfo(name="your_move")
    var playerMove: String,

    @ColumnInfo(name="computer_move")
    var computerMove: String,

    @ColumnInfo(name="result")
    var result: String,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
)