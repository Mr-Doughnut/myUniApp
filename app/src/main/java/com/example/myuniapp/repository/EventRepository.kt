package com.example.myuniapp.repository

import com.example.myuniapp.MyUniApp
import com.example.myuniapp.data.local.AppDatabase
import com.example.myuniapp.data.local.EventDao
import com.example.myuniapp.data.local.EventEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class EventRepository(
    private val eventDao: EventDao,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    val events: Flow<List<EventEntity>> = eventDao.getAllEvents()

    suspend fun refreshEvents() {
        val snapshot = firestore.collection("events").get().await()
        val events = snapshot.documents.map { doc ->
            EventEntity(
                id = 0, // локальный автоген
                title = doc.getString("title") ?: "Event",
                time = doc.getString("time") ?: "18:00",
                place = doc.getString("place") ?: "Student Union",
                description = doc.getString("description") ?: "No description"
            )
        }
        eventDao.clearAll()
        eventDao.insertEvents(events)
    }

    companion object {
        fun default(): EventRepository {
            val context = MyUniApp.instance
            val db = AppDatabase.getInstance(context)
            return EventRepository(db.eventDao())
        }
    }
}
