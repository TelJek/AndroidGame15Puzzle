package com.example.hw01_15puzzle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.TextWatcherAdapter

class ActivityLeaderBoard : AppCompatActivity() {

    private lateinit var playerRepository: PlayerRepository
    private lateinit var recyclerViewLeaderboardList: RecyclerView
    private lateinit var adapter: DataRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leader_board)

        playerRepository = PlayerRepository(applicationContext)

        recyclerViewLeaderboardList = findViewById<RecyclerView>(R.id.recyclerViewLeaderboardList)

        recyclerViewLeaderboardList.layoutManager = LinearLayoutManager(this)

        adapter = DataRecyclerViewAdapter(this, playerRepository)

        recyclerViewLeaderboardList.adapter = adapter


    }

    private fun fillLeaderboardText() {
        Log.d("ActivityLeaderBoard", "ActivityLeaderBoard: Displaying data in the TextView")

//        val data = playerRepository.getRepoData()
//        val listData = ArrayList<String>()

//        while (data.moveToNext()) {
//            listData.add("Id: ${data.getString(0)}  Player: ${data.getString(1)} - Moves: ${data.getString(2)} - Time: ${data.getString(3)} \n")
//        }

//        textViewLeaderBoardContent.text = ""
//        for (str in listData) {
//            textViewLeaderBoardContent.text = textViewLeaderBoardContent.text.toString() + str + "\n"
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playerRepository.close()
    }

}