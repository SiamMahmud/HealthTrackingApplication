package com.example.patienttracking

import android.os.Bundle
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
import com.example.patienttracking.models.DoctorInfo
import com.example.patienttracking.models.User
import com.example.patienttracking.ui.DoctorMainPageFragment
import com.example.patienttracking.ui.LoginFragment
import com.example.patienttracking.ui.PatientProfileEditFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class DoctorProfileFragment : Fragment() {

    private var firebaseDatabase : FirebaseDatabase? = null
    private var databaseReference : DatabaseReference? = null
    private lateinit var user : User
    private lateinit var doctorInfo : DoctorInfo


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.reference
        // Inflate the layout for this fragment
      val view =  inflater.inflate(R.layout.fragment_doctor_profile, container, false)
        val backButtonP = view.findViewById<Button>(R.id.doctorProfileBackBtn)
        val dName = view.findViewById<TextView>(R.id.setDName)
        val dDegreeName = view.findViewById<TextView>(R.id.setDDegree)
        val dDocSpecialization = view.findViewById<TextView>(R.id.setDSpecialization)
        val dEmail = view.findViewById<TextView>(R.id.doctorEmailDetails)
        val dPhoneNumber = view.findViewById<TextView>(R.id.doctorPhoneNumberDetails)
        val dProLogout = view.findViewById<TextView>(R.id.profileLogOutBtn)
        val dEditP = view.findViewById<TextView>(R.id.doctorEditProfile)
        val updateDoctorProfileDetails = view.findViewById<TextView>(R.id.dUpdateP)

        var builder : AlertDialog.Builder
        var con = this.context
        con?.let {
            builder = AlertDialog.Builder(con)
        }
        backButtonP.setOnClickListener {
            val mainPage = DoctorMainPageFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout, mainPage)
            transaction.commit()
        }

        updateDoctorProfileDetails.setOnClickListener {
            val dUpdate = DoctorProfileViewByPatientFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout, dUpdate)
            transaction.commit()
        }

        dEditP.setOnClickListener {
            val dEdit = DoctorEditProfileFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout, dEdit)
            transaction.commit()
        }



        dProLogout.setOnClickListener {
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
            databaseReference?.child("Doctor")?.orderByChild("email")?.equalTo(email)
                ?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // Here, snapshot refers to the user with the specified email
                            for (userSnapshot in snapshot.children) {
                                user = userSnapshot.getValue(User::class.java)!!
                                dName.text = user.name
                                dEmail.text = user.email
                                dPhoneNumber.text = user.phoneNumber
                            }
                        } else {
                            Toast.makeText(activity, "Data not found", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e("ViewProfileFragment", "Data retrieval cancelled: ${databaseError.message}")
                    }
                })

            databaseReference?.child("DoctorDetails")?.orderByChild("email")?.equalTo(email)
                ?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // Here, snapshot refers to the user with the specified email
                            for (userSnapshot in snapshot.children) {
                                doctorInfo = userSnapshot.getValue(DoctorInfo::class.java)!!
                                dDegreeName.text = doctorInfo.degree
                                dDocSpecialization.text = doctorInfo.specialty
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