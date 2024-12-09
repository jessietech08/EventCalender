package com.example.eventcalender

class Events {
    var id: Int = 0
    var eventTitle: String? = null
    var eventDate: String? = null

    constructor(id: Int, eventTitle: String,  eventDate: String) {
        this.id = id
        this.eventTitle = eventTitle
        this.eventDate = eventDate
    }
    constructor(eventTitle: String, eventDate: String) {
        this.eventTitle = eventTitle
        this.eventDate = eventDate
    }
}