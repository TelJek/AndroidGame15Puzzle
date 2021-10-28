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

    fun getRepoData(): List<Leaderboard> {
        val result = ArrayList<Leaderboard>()
        dbHelper = DbHelper(context)
        val cursor = dbHelper.getData()
        while (cursor.moveToNext()) {
            result.add(
                Leaderboard(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3)
                )
            )
        }
        return result
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

    fun delete(leaderboard: Leaderboard) {
        delete(leaderboard.id)
    }

    fun delete(id: Int) {
        open()
        db.delete(DbHelper.PLAYER_TABLE_NAME, "_id=?", arrayOf(id.toString()))
    }

    fun update(leaderboard: Leaderboard) {

    }
}