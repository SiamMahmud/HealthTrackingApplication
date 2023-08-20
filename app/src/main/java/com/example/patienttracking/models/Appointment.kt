package com.example.patienttracking.models

data class Appointment(
    val userEmail: String = "",
    val appointmentDate: String = "",
    val doctorName: String = "",
    val doctorEmail: String = ""
)
