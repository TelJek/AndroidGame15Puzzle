package com.example.hw01_15puzzle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import kotlinx.android.synthetic.main.game_statistics.*


class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = this::class.java.declaringClass.simpleName
    }

    private val logic = GameLogic()
    private var firstButtonX = 99
    private var firstButtonY = 99
    private var counterForClicks = 0
    private var counterForMoves = 0
    private var gameState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate")
//        updateUi()
    }

    private fun updateUi() {
        for (x in logic.getBoard().indices) {
            for (y in logic.getBoard()[x].indices) {
                val id = resources.getIdentifier("buttonGameBoard$x$y", "id", packageName)
                val button = findViewById<Button>(id)
                if (logic.getBoard()[y][x].toString() == "16") {
                    button.text = null
                } else button.text = logic.getBoard()[y][x].toString()
            }
        }
    }

    fun buttonGameBoardClicked(view: android.view.View) {
        if (gameState) {
            val idStr = resources.getResourceEntryName(view.id)
            Log.d("buttonGameBoardClicked", logic.getBoard()[idStr[16].toString().toInt()][idStr[15].toString().toInt()].toString())
            if (counterForClicks == 0) {
                firstButtonX = idStr[15].toString().toInt()
                firstButtonY = idStr[16].toString().toInt()
                counterForClicks++
            } else if (counterForClicks == 1) {
                if (firstButtonX != idStr[15].toString().toInt() || firstButtonY != idStr[16]
                        .toString().toInt()) {
                    val x = idStr[15].toString().toInt()
                    val y = idStr[16].toString().toInt()
                    if (logic.getBoard()[y][x].toString() == "16" || logic.getBoard()[firstButtonY][firstButtonX].toString() == "16") {
                        if (logic.canIMove(firstButtonX, firstButtonY, x, y)) {
                            logic.makeMove(firstButtonX, firstButtonY, x, y)
                            counterForClicks--
                            counterForMoves++
                            textGameStatisticsMoves.text = counterForMoves.toString()
                        }
                    } else {
                        counterForClicks--
                    }
                    updateUi()
                }
            }
        }
    }

    fun buttonGameStatisticsShuffleClicked(view: android.view.View) {
        logic.resetBoard()
        counterForMoves = 0
        textGameStatisticsMoves.text = counterForMoves.toString()
        buttonGameStatisticsTimer.text = "UNPAUSE"
        gameState = false
        updateUi()
    }

    fun buttonGameStatisticsTimerClicked(view: android.view.View) {
        if (buttonGameStatisticsTimer.text == "PAUSE"){
            buttonGameStatisticsTimer.text = "UNPAUSE"
            gameState = false
        } else if (buttonGameStatisticsTimer.text == "UNPAUSE"){
            buttonGameStatisticsTimer.text = "PAUSE"
            gameState = true
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }
}