package com.example.hw01_15puzzle

class Leaderboard {
    var id: Int = 0
    var playerName: String = ""
    var playerMoves: Int = 0
    var playerTime: Int = 0

    constructor(name: String, moves: Int, time: Int) : this(0, name, time, moves) {

    }

    constructor(id: Int, name: String, moves: Int, time: Int) {
        this.id = id
        this.playerName = name
        this.playerMoves = moves
        this.playerTime = time
    }
}