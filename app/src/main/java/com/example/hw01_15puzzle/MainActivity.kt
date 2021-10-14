package com.example.hw01_15puzzle

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.Chronometer
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_leader_board.*
import kotlinx.android.synthetic.main.activity_leader_board.view.*
import kotlinx.android.synthetic.main.game_statistics.*
import kotlin.math.log


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
    private var alreadyWon = false
    private var allowToAddWinner = false
    private var dialogText = ""
    private lateinit var playerRepository: PlayerRepository
    var timer: Int = 0
    lateinit var clock: Chronometer
    var elapsedMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate")

        timer = resources.getIdentifier("textGameStatisticsTime", "id", packageName)
        clock = findViewById<Chronometer>(timer)

        playerRepository = PlayerRepository(applicationContext)

        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return

        val boardJson = sharedPref.getString("state", null)
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

    private fun openWinDialog() {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Congratulations!")

        val input = EditText(this)

        input.hint = "Now you have to enter your name"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)


        builder.setPositiveButton("OK") { dialog, which ->
            dialogText = input.text.toString()
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

        builder.show()
    }

    private fun timerStartAndStop(state: String) {
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
            Log.d("buttonGameBoardClicked",
                logic.getBoard()[idStr[16].toString().toInt()][idStr[15].toString()
                    .toInt()].toString()
            )
            if (counterForClicks == 0) {
                firstButtonX = idStr[15].toString().toInt()
                firstButtonY = idStr[16].toString().toInt()
                counterForClicks++
            } else if (counterForClicks == 1) {
                if (firstButtonX != idStr[15].toString().toInt() || firstButtonY != idStr[16]
                        .toString().toInt()
                ) {
                    val x = idStr[15].toString().toInt()
                    val y = idStr[16].toString().toInt()
                    if (logic.getBoard()[y][x].toString() == "16" || logic
                            .getBoard()[firstButtonY][firstButtonX].toString() == "16") {
                        if (logic.canIMove(firstButtonX, firstButtonY, x, y)) {
                            logic.makeMove(firstButtonX, firstButtonY, x, y)
                            counterForClicks--
                            counterForMoves++
                            textGameStatisticsMoves.text = counterForMoves.toString()
                        }
                    } else {
                        counterForClicks--
                    }
                    if (!alreadyWon) {
                        if (logic.checkWin()) {
                            timerStartAndStop("stop")
                            elapsedMillis = SystemClock.elapsedRealtime() - clock.base
                            openWinDialog()
                            alreadyWon = true
                            allowToAddWinner = true
                            println("dialogText: $dialogText")
                            logic.resetBoard()
                        }
                    }
                    updateUi()
                }
            }
        }
    }

    fun buttonGameStatisticsLB(view: android.view.View) {
        val intent = Intent(this, ActivityLeaderBoard::class.java)
        if (allowToAddWinner) {
            println("dialogText: $dialogText")
            logic.addWinner((Leaderboard(dialogText, ((elapsedMillis / 1000) - 1).toInt(), counterForMoves)))
            val db = playerRepository.open()
            db.add(Leaderboard(dialogText, ((elapsedMillis / 1000) - 1).toInt(), counterForMoves))
            playerRepository.close()
            allowToAddWinner = false
        }
        startActivity(intent)
    }

    fun buttonGameStatisticsShuffleClicked(view: android.view.View) {
        timerStartAndStop("reset")
        alreadyWon = false
        if (!gameState) {
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
        if (gameState) {
            buttonGameStatisticsTimer.text = "UNPAUSE"
            timerStartAndStop("stop")
            gameState = false
        } else if (!gameState) {
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
            if (!gameState) {
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

        with(sharedPref.edit()) {
            putString("state", logic.getBoardJson())
            putInt("Moves", counterForMoves)
            putLong("Time", timeWhenStopped)
            commit()
        }
    }
}
