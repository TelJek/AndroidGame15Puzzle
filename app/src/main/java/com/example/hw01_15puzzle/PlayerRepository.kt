package com.example.hw01_15puzzle

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class PlayerRepository(val context: Context) {

    private lateinit var dbHelper: DbHelper
    private lateinit var db: SQLiteDatabase

    fun open(): PlayerRepository {
        dbHelper = DbHelper(context)
        db = dbHelper.writableDatabase
        return this
    }

    fun read(): PlayerRepository {
        dbHelper = DbHelper(context)
        db = dbHelper.readableDatabase
        return this
    }

    fun getRepoData(): Cursor {
        dbHelper = DbHelper(context)
        return dbHelper.getData()
    }

    fun close() {
        dbHelper.close()
    }

    fun add(player: Leaderboard) {
        val contentValue = ContentValues()
        contentValue.put(DbHelper.PLAYER_NAME, player.playerName)
        contentValue.put(DbHelper.PLAYER_MOVES, player.playerMoves)
        contentValue.put(DbHelper.PLAYER_TIME, player.playerTime)
        db.insert(DbHelper.PLAYER_TABLE_NAME, null, contentValue)
    }
}