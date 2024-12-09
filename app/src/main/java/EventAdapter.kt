package com.example.eventcalender

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button

// https://developer.android.com/develop/ui/views/layout/recyclerview
class EventAdapter(private val eventList: MutableList<Events>, private val dbHandler: MyDbHandler) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.textView.text = event.eventTitle
        holder.deleteButton.setOnClickListener {
            deleteEvent(event, position)
        }
    }

    override fun getItemCount() = eventList.size

    // Function to delete an event
    private fun deleteEvent(event: Events, position: Int) {
        val success = dbHandler.deleteEvent(event.id)
        if (success) {
            eventList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
