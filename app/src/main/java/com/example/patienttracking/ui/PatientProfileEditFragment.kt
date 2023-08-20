package com.example.patienttracking.ui

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.patienttracking.R
import com.example.patienttracking.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import android.util.Base64

class PatientProfileEditFragment : Fragment() {

    private var firebaseDatabase : FirebaseDatabase? = null
    private var databaseReference : DatabaseReference? = null
    private lateinit var user : User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.reference
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_patient_profile_edit, container, false)
        val updateNameBtn = view.findViewById<EditText>(R.id.updateName)
        val updateEmailBtn = view.findViewById<EditText>(R.id.updateEmail)
        val updatePhoneNumberBtn = view.findViewById<EditText>(R.id.updatePhoneNumber)
        val backProfileButton = view.findViewById<TextView>(R.id.pBtn)
        val updateProfileButton = view.findViewById<Button>(R.id.upDateBtn)


        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
        currentUserEmail?.let { email ->
            databaseReference?.child("Users")?.orderByChild("email")?.equalTo(email)
                ?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // Here, snapshot refers to the user with the specified email
                            for (userSnapshot in snapshot.children) {
                                user = userSnapshot.getValue(User::class.java)!!
                                updateNameBtn.text = Editable.Factory.getInstance().newEditable(user.name)
                                updateEmailBtn.text = Editable.Factory.getInstance().newEditable(user.email)
                                updatePhoneNumberBtn.text = Editable.Factory.getInstance().newEditable(user.phoneNumber)
                            }
                        } else {
                            Toast.makeText(activity, "Data not found", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e("ViewProfileFragment", "Data retrieval cancelled: ${databaseError.message}")
                    }
                })
        }

        backProfileButton.setOnClickListener {
                val patientProfile = ViewProfileFragment()
                val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.replace(R.id.frameLayout, patientProfile)
                transaction.commit()

        }
        // ...
        updateProfileButton.setOnClickListener {
            val newName = updateNameBtn.text.toString()
            val newEmail = updateEmailBtn.text.toString()
            val newPhoneNumber = updatePhoneNumberBtn.text.toString()

            if (newName.isEmpty() || newEmail.isEmpty() || newPhoneNumber.isEmpty()) {
                Toast.makeText(activity, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Update the data in the Firebase Realtime Database
            val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
            currentUserEmail?.let { email ->
                databaseReference?.child("Users")?.orderByChild("email")?.equalTo(email)
                    ?.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                // Here, snapshot refers to the user with the specified email
                                for (userSnapshot in snapshot.children) {
                                    // Update the user object with the new data
                                    val user = userSnapshot.getValue(User::class.java)
                                    user?.let {
                                        it.name = newName
                                        it.email = newEmail
                                        it.phoneNumber = newPhoneNumber

                                        // Get the reference to the specific user in the "Users" node based on their email
                                        val userId = userSnapshot.key
                                        userId?.let { uid ->
                                            databaseReference?.child("Users")?.child(uid)?.setValue(it)
                                                ?.addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        Toast.makeText(activity, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                                                    } else {
                                                        Toast.makeText(activity, "Failed to update profile", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(activity, "Data not found", Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.e("ViewProfileFragment", "Data retrieval cancelled: ${databaseError.message}")
                        }
                    })
            }
        }
// ...






        return view
    }

}