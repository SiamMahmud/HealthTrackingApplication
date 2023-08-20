package com.example.patienttracking.ui

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentTransaction
import com.example.patienttracking.R
import com.example.patienttracking.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ViewProfileFragment : Fragment() {

    private var firebaseDatabase : FirebaseDatabase? = null
    private var databaseReference : DatabaseReference? = null
    private lateinit var user : User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.reference
        val view =  inflater.inflate(R.layout.fragment_view_profile, container, false)
        var nameTextView = view.findViewById<TextView>(R.id.userNameDetails)
        var emailTextView = view.findViewById<TextView>(R.id.userEmailDetails)
        var numberTextView = view.findViewById<TextView>(R.id.userPhoneNumberDetails)
        val homeBackBtn = view.findViewById<Button>(R.id.profileBackBtn)
        val logOutBtn = view.findViewById<TextView>(R.id.profileLogOutBtn)
        val pEdit = view.findViewById<Button>(R.id.editProfile)

        var builder : AlertDialog.Builder
        var con = this.context
        con?.let {
            builder = AlertDialog.Builder(con)
        }

        homeBackBtn.setOnClickListener {
            val patientMainPage = PatientMainPageFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout, patientMainPage)
            transaction.commit()
        }
        pEdit.setOnClickListener {
            val updateProfile = PatientProfileEditFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout, updateProfile)
            transaction.commit()
        }

        logOutBtn.setOnClickListener {
            con?.let {
                builder = AlertDialog.Builder(con)
                builder.setTitle("Alert!")

                    .setMessage("Do you want to log Out?")
                    .setCancelable(true)
                    .setPositiveButton("Yes"){dialogInterface,it->
                        val loginPage = LoginFragment()
                        val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
                        transaction.replace(R.id.frameLayout,loginPage)
                        transaction.commit()
                    }
                    .setNegativeButton("No"){dialogInterface,it->
                        dialogInterface.cancel()
                    }
                    .show()
            }
        }

        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
        currentUserEmail?.let { email ->
            databaseReference?.child("Users")?.orderByChild("email")?.equalTo(email)
                ?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // Here, snapshot refers to the user with the specified email
                            for (userSnapshot in snapshot.children) {
                                user = userSnapshot.getValue(User::class.java)!!
                                nameTextView.text = user.name
                                emailTextView.text = user.email
                                numberTextView.text = user.phoneNumber
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



        return view
    }
}








