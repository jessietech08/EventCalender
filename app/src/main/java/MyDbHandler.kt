package com.example.eventcalender

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.content.ContentValues
import androidx.annotation.IntegerRes

class MyDbHandler(context: Context, name: String?,
                  factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db : SQLiteDatabase) {
        val CREATE_EVENTS_TABLE = ("CREATE TABLE " +
                TABLE_EVENTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_EVENTTITLE
                + " TEXT," + COLUMN_EVENTDATE + " TEXT" + ")")
        db.execSQL(CREATE_EVENTS_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int,
                           newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS)
        onCreate(db)
    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "eventDB.db"
        val TABLE_EVENTS = "events"
        val COLUMN_ID = "_id"
        val COLUMN_EVENTTITLE = "eventTitle"
        val COLUMN_EVENTDATE = "eventDate"
    }

    fun addEvent(event : Events) {
        val values = ContentValues()
        values.put(COLUMN_EVENTTITLE, event.eventTitle)
        values.put(COLUMN_EVENTDATE, event.eventDate)
        val db = this.writableDatabase
        db.insert(TABLE_EVENTS, null, values)
        db.close()
    }

    fun findEvent(eventDate: String): Events? {
        val query =
            "SELECT * FROM $TABLE_EVENTS WHERE $COLUMN_EVENTDATE = \"$eventDate\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var event: Events? = null
        if (cursor.moveToFirst()) {
            cursor.moveToFirst()
            val id = Integer.parseInt(cursor.getString(0))
            val eventTitle = cursor.getString(1)
            val eventDate = cursor.getString(2)
            event = Events(id, eventTitle, eventDate)
            cursor.close()
        }
        db.close()
        return event
    }

    fun deleteEvent(eventDate: String): Boolean {
        var result = false
        val query =
            "SELECT * FROM $TABLE_EVENTS WHERE $COLUMN_EVENTDATE = \"$eventDate\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            val id = Integer.parseInt(cursor.getString(0))
            db.delete(
                TABLE_EVENTS, COLUMN_ID + " = ?",
                arrayOf(id.toString()))
            cursor.close()
            result = true
        }
        db.close()
        return result
    }

//    fun editEvent(eventDate: String): Events? {
//        val query =
//            "SELECT * FROM $TABLE_EVENTS WHERE $COLUMN_EVENTDATE = \"$eventDate\""
//        val db = this.writableDatabase
//        val cursor = db.rawQuery(query, null)
//    }
}