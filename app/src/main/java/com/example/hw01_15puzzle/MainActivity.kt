package com.example.hw01_15puzzle

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.Chronometer
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
    private var gameState = true
    //    timerState = #0 - is stopped #1 - is working
    private var timerState = 0
    private var timeWhenStopped: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate")

        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return

        val boardJson = sharedPref.getString("state",null)
        if (boardJson != null) {
            logic.restoreBoardFromJson(boardJson)
        }

        val savedMoves = sharedPref.getInt("Moves", 0)
        if (savedMoves != 0) {
            counterForMoves = sharedPref.getInt("Moves", 0)
            textGameStatisticsMoves.text = counterForMoves.toString()
        }

        val savedTime = sharedPref.getLong("Time", 0)
        if (savedTime.toInt() != 0) {
            timeWhenStopped = sharedPref.getLong("Time", 0)
            timerStartAndStop("start")
        }

        updateUi()
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
        timerStartAndStop("")
    }

    private fun timerStartAndStop(state: String) {
        val timer = resources.getIdentifier("textGameStatisticsTime", "id", packageName)
        val clock = findViewById<Chronometer>(timer)
        if (state == "reset") {
            clock.base = SystemClock.elapsedRealtime()
            clock.start()
        }
        if (state == "start") {
            clock.base = SystemClock.elapsedRealtime() + timeWhenStopped
            clock.start()
            timerState = 1
        }
        if (state == "stop") {
            clock.stop()
            timeWhenStopped = clock.base - SystemClock.elapsedRealtime();
            timerState = 0
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
        timerStartAndStop("reset")
        if (!gameState){
            buttonGameStatisticsTimer.text = "PAUSE"
            gameState = true
            timerStartAndStop("reset")
        }

        logic.shuffleBoard()
        logic.getBoard()
        counterForMoves = 0
        textGameStatisticsMoves.text = counterForMoves.toString()

        updateUi()
    }

    fun buttonGameStatisticsTimerClicked(view: android.view.View) {
        if (gameState){
            buttonGameStatisticsTimer.text = "UNPAUSE"
            timerStartAndStop("stop")
            gameState = false
        } else if (!gameState){
            buttonGameStatisticsTimer.text = "PAUSE"
            timerStartAndStop("start")
            gameState = true
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        if (buttonGameStatisticsTimer.text != "UNPAUSE") {
            if (!gameState){
                gameState = true
                timerStartAndStop("start")
            }
        }
    }



    override fun onPause() {
        super.onPause()
        if (buttonGameStatisticsTimer.text != "PAUSE") {
            if (gameState) {
                gameState = false
                timerStartAndStop("stop")
            }
        }
    }


    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")

        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        timerStartAndStop("stop")

        with (sharedPref.edit()) {
            putString("state", logic.getBoardJson())
            putInt("Moves", counterForMoves)
            putLong("Time", timeWhenStopped)
            commit()
        }
    }

}
