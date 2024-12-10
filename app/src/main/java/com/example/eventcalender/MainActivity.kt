package com.example.eventcalender

import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {

    private lateinit var eventDbHelper: MyDbHandler
    private lateinit var eventAdapter: EventAdapter
    private var selectedDate: String? = null
    private var eventsList: MutableList<Events> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        eventDbHelper = MyDbHandler(this, null, null, 1)

        // Initialize views & buttons
        val calendarView: CalendarView = findViewById(R.id.calendarView);
        val eventTitle: EditText = findViewById(R.id.eventTitle)
        val addButton: Button = findViewById(R.id.addButton)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        // Set up eventAdapter and recyclerView
        // https://developer.android.com/develop/ui/views/layout/recyclerview
        eventAdapter = EventAdapter(eventsList, eventDbHelper)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = eventAdapter

        // Handle date selection
        // https://stackoverflow.com/questions/10339808/how-can-i-extract-date-from-calendarview-and-display-selected-date
        calendarView.setOnDateChangeListener { calenderView, year, month, dayOfMonth ->
            selectedDate = "${month + 1}/$dayOfMonth/$year" // line formats the selected date into a string
            refreshEvent()
        }

        // Retrieves input and date selected. Saves and stores event to database.
        // Displays event on event list
        addButton.setOnClickListener {
            val title = eventTitle.text.toString()
            if (title.isNotEmpty() && selectedDate != null) {
                val event = Events(title, selectedDate!!)
                eventDbHelper.addEvent(event)
                eventTitle.setText("")
                refreshEvent()
            }
        }
    }

    // When an event is added or deleted event list is refreshed to current status
    fun refreshEvent() {
        if (selectedDate != null) {
            eventsList.clear()
            eventsList.addAll(eventDbHelper.findEvent(selectedDate!!))
            // display the updated list of events
            eventAdapter.notifyDataSetChanged()
        }
    }
}
