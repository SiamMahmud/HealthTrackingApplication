package com.example.patienttracking.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object FirebaseUtil {
    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseCurrentUser = firebaseAuth.currentUser
    val firebaseDatabase = FirebaseDatabase.getInstance()
}