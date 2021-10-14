package com.example.hw01_15puzzle

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        companion object {
            const val DATABASE_NAME = "app.db"
            const val DATABASE_VERSION = 2

            const val PLAYER_TABLE_NAME = "PLAYERS"

            const val PLAYER_ID = "_id"
            const val PLAYER_NAME = "name"
            const val PLAYER_MOVES = "moves"
            const val PLAYER_TIME = "time"

            const val SQL_PLAYER_CREATE_TABLE =
                "create table $PLAYER_TABLE_NAME(" +
                        "$PLAYER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "$PLAYER_NAME TEXT NOT NULL, " +
                        "$PLAYER_MOVES INTEGER NOT NULL, " +
                        "$PLAYER_TIME INTEGER NOT NULL);"

            const val SQL_DELETE_TABLES = "DROP TABLE IF EXISTS $PLAYER_TABLE_NAME"
        }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_PLAYER_CREATE_TABLE)
    }

    fun getData(): Cursor {
        val db = this.writableDatabase
        val query = "SELECT * FROM $PLAYER_TABLE_NAME ORDER BY $PLAYER_TIME ASC;"
        val data = db.rawQuery(query, null)
        return data
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_TABLES)
        onCreate(db)
    }
}