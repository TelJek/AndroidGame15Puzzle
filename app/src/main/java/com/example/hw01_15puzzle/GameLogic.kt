package com.example.hw01_15puzzle

import android.util.JsonWriter
import kotlinx.android.synthetic.main.activity_leader_board.*
import org.json.JSONArray
import java.io.File
import java.util.*
import kotlin.coroutines.coroutineContext

class GameLogic() {

    private var _board = arrayOf(
        arrayOf(1, 2, 3, 4),
        arrayOf(5, 6, 7, 8),
        arrayOf(9, 10, 11, 12),
        arrayOf(13, 14, 15, 16)
    )

    private var _boardForWin = arrayOf(
        arrayOf(1, 2, 3, 4),
        arrayOf(5, 6, 7, 8),
        arrayOf(9, 10, 11, 12),
        arrayOf(13, 14, 15, 16)
    )

    private val leaderboard = ArrayList<Leaderboard>()

    fun addWinner(winner: Leaderboard) {
        leaderboard.plus(winner)
    }

    fun getLeaderBoard(): ArrayList<Leaderboard> {
        return leaderboard
    }

    fun getBoard(): Array<Array<Int>> {
        return _board
    }

    fun getWinnerBoard(): Array<Array<Int>> {
        return _boardForWin
    }

    fun makeMove(aX: Int, aY: Int, bX: Int, bY: Int) {
        val temp = _board[aY][aX]
        _board[aY][aX] = _board[bY][bX]
        _board[bY][bX] = temp;

    }

    fun resetBoard() {
        var counter = 1
        for (x in _board.indices) {
            for (y in _board[x].indices) {
                _board[x][y] = counter;
                counter++;
            }
        }
    }

    fun shuffleBoard() {
        resetBoard()
        val copyOfBoard = _board.copyOf()
        var x = 0
        var rndAX = 3
        var rndAY = 3
        while (x != 200) {
            val rndBX = (0..3).random()
            val rndBY = (0..3).random()
            if (canIMove(rndAX, rndAY, rndBX, rndBY)) {
                makeMove(rndAX, rndAY, rndBX, rndBY)
                rndAX = rndBX
                rndAY = rndBY
            }
            x++
        }
        _board = copyOfBoard
    }

    fun checkWin(): Boolean {
        var counter = 0;
        for (x in _board.indices) {
            if (_board[x].contentEquals(_boardForWin[x])) {
                counter++
            }
        }
        if (counter == 4) {
            return true
        }
        return false
    }

    fun canIMove(aX: Int, aY: Int, bX: Int, bY: Int): Boolean {
//        (aX: 0, aY: 1, bX: 3, bY: 2)
        val xSide = aY - bY
        val ySide = aX - bX
        return if (xSide == 0) {
            (ySide == 0 || ySide == 1 || ySide == -1)
        } else if (xSide == 1 || xSide == -1) {
            (ySide == 0)
        } else false;
    }

    fun getBoardJson(): String {
        val jsonArray = JSONArray(_board);
        return jsonArray.toString();
    }

    fun restoreBoardFromJson(boardJson: String) {
        val jsonArray = JSONArray(boardJson)
        for (x in 0 until jsonArray.length()) {
            for (y in 0 until (jsonArray[x] as JSONArray).length()) {
                _board[x][y] = (jsonArray[x] as JSONArray)[y] as Int
            }
        }
    }

}

