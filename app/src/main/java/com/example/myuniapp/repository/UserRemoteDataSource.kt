package com.example.myuniapp.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class UserRemoteDataSource(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    fun createUserProfile(uid: String, email: String) {
        val data = mapOf(
            "email" to email,
            "createdAt" to FieldValue.serverTimestamp()
        )
        firestore.collection("users")
            .document(uid)
            .set(data)
    }
}
