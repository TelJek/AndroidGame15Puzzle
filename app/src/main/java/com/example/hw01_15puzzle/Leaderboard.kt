package com.example.hw01_15puzzle

class Leaderboard constructor(val name: String, private val time: Int, private val moves: Int) {
    val playerName = name
    val playerTime = time
    val playerMoves = moves
}