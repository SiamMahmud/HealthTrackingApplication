package com.example.patienttracking.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentTransaction
import com.example.patienttracking.R
import com.example.patienttracking.models.User
import com.google.firebase.database.*


class AdminRemovePatientDetailsFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var deleteName: TextView
    private lateinit var deleteEmail: TextView
    private lateinit var deletePhoneNumber: TextView
    private lateinit var emailS : EditText
    private lateinit var viewDetailsCard : CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_admin_remove_patient_details, container, false)
        val findDetailsBtn = view.findViewById<Button>(R.id.findPatientDetailsBtn)
        deleteName = view.findViewById(R.id.deleteName)
        deleteEmail = view.findViewById(R.id.deleteEmail)
        deletePhoneNumber = view.findViewById(R.id.deletePhoneNumber)
        emailS = view.findViewById(R.id.emailSearch)
        viewDetailsCard = view.findViewById(R.id.details)
        val deleteBtn = view.findViewById<Button>(R.id.deleteInfoButton)
        val bBtn = view.findViewById<Button>(R.id.adminPatientInfoRemoveBackBtn)

        findDetailsBtn.setOnClickListener {
            val userEmail: String = emailS.text.toString().trim()
            if (userEmail.isNotEmpty()) {
                readData(userEmail)
            } else {
                Toast.makeText(activity, "Please enter the email address", Toast.LENGTH_LONG).show()
            }
        }

        bBtn.setOnClickListener {
            val pMainPage = AdminMainPageFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout, pMainPage)
            transaction.commit()
        }

        deleteBtn.setOnClickListener {
            val userEmail: String = emailS.text.toString().trim()
            if (userEmail.isNotEmpty()) {
                deleteData(userEmail)
            } else {
                Toast.makeText(activity, "Please enter the email address", Toast.LENGTH_LONG).show()
            }
        }

        return view
    }

    private fun deleteData(email: String) {
        database = FirebaseDatabase.getInstance().getReference("Users")
        val query = database.orderByChild("email").equalTo(email)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var userFound = false

                for (userSnapshot in dataSnapshot.children) {
                    userSnapshot.ref.removeValue() // Delete the user from the database
                    userFound = true
                    break
                }

                if (userFound) {
                    deleteName.text = ""
                    deleteEmail.text = ""
                    deletePhoneNumber.text = ""
                    viewDetailsCard.visibility = View.GONE
                    Toast.makeText(activity, "User Deleted Successfully", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(activity, "User Doesn't exist", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(activity, "Failed: ${databaseError.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun readData(email: String) {
        database = FirebaseDatabase.getInstance().getReference("Users")
        val query = database.orderByChild("email").equalTo(email)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var userFound = false

                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    deleteName.text = user?.name
                    deleteEmail.text = user?.email
                    deletePhoneNumber.text = user?.phoneNumber
                    viewDetailsCard.visibility = View.VISIBLE

                    Toast.makeText(activity, "Found the data successfully", Toast.LENGTH_LONG).show()
                    userFound = true
                    break
                }

                if (!userFound) {
                    Toast.makeText(activity, "User Doesn't exist", Toast.LENGTH_LONG).show()
                    viewDetailsCard.visibility = View.GONE
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(activity, "Failed: ${databaseError.message}", Toast.LENGTH_LONG).show()
            }
        })
    }



}

