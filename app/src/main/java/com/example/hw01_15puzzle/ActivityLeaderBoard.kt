package com.example.hw01_15puzzle

import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_leader_board.*
import java.io.File
import java.util.ArrayList

class ActivityLeaderBoard : AppCompatActivity() {

    lateinit var playerRepository: PlayerRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leader_board)

        playerRepository = PlayerRepository(applicationContext)
        fillLeaderboardText()
    }

    fun fillLeaderboardText() {
        Log.d("ActivityLeaderBoard", "ActivityLeaderBoard: Displaying data in the TextView")

        var data = playerRepository.getRepoData()
        var listData = ArrayList<String>()
        while (data.moveToNext()) {
            listData.add("Player: ${data.getString(1)} - Moves: ${data.getString(2)} - Time: ${data.getString(3)} \n")
        }
        textViewLeaderBoardContent.text = ""
        for (str in listData) {
            textViewLeaderBoardContent.text = textViewLeaderBoardContent.text.toString() + str + "\n"
        }
    }

}