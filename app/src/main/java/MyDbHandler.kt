package com.example.eventcalender

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.content.ContentValues

// Nov12 Zoom
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

    fun findEvent(eventDate: String): List<Events> {
        val query = "SELECT * FROM $TABLE_EVENTS WHERE $COLUMN_EVENTDATE = ?"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, arrayOf(eventDate))
        val events = mutableListOf<Events>()

        // Nov19 Zoom
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val id = cursor.getInt(0)
                val eventTitle = cursor.getString(1)
                val eventDateStored = cursor.getString(2)
                events.add(Events(id, eventTitle, eventDateStored))
                cursor.moveToNext()
            }
        }
        cursor.close()
        db.close()
        return events
    }

    fun deleteEvent(id: Int): Boolean {
        val db = this.writableDatabase
        return db.delete(TABLE_EVENTS, "$COLUMN_ID = ?", arrayOf(id.toString())) > 0
    }
}