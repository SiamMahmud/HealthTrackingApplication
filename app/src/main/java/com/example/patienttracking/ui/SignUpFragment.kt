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
import androidx.fragment.app.FragmentTransaction
import com.example.patienttracking.R
import com.example.patienttracking.models.User
import com.example.patienttracking.utils.FirebaseUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_sign_up, container, false)
        var userN = view.findViewById<EditText>(R.id.userName)
        var userE = view.findViewById<EditText>(R.id.userEmail)
        val userPass = view.findViewById<EditText>(R.id.userPassword)
        var userP = view.findViewById<EditText>(R.id.userPhoneNumber)
        val loginBack = view.findViewById<TextView>(R.id.backSignInBtn)
        val signUpButton = view.findViewById<Button>(R.id.SignUpBtn)
        firebaseAuth = FirebaseAuth.getInstance()
        loginBack.setOnClickListener {
            val loginF = LoginFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout, loginF)
            transaction.commit()
        }
        signUpButton.setOnClickListener {
            val name = userN.text.toString()
            val email  = userE.text.toString()
            val pass = userPass.text.toString()
            val pNumber = userP.text.toString()

            if(name.isEmpty()){
                userN.error = "Username Required"
                return@setOnClickListener
            }else if (email.isEmpty()){
                userE.error = "Email Required"
                return@setOnClickListener
            }else if (pass.isEmpty()){
                userPass.error = "Password Required"
                return@setOnClickListener
            }
            else if (pNumber.isEmpty()) {
                userP.error = "Phone Number Required"
                return@setOnClickListener
            } else {
                val loading = LoadingDialog(requireActivity())
                loading.startLoading()
                database = FirebaseDatabase.getInstance().getReference("Users")
                database = FirebaseDatabase.getInstance().getReference("Doctor")
                database = FirebaseDatabase.getInstance().getReference("Admin")
                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {authTask ->
                    loading.isDismiss()
                    if (authTask.isSuccessful) {
                        Toast.makeText(activity, "Sign Up Confirmed", Toast.LENGTH_LONG).show()
                        val uid = authTask.result.user?.uid

                        val domain = email.substringAfterLast("@")
                        val table = when (domain) {
                            "doctor.com" -> "Doctor"
                            "admin.com" -> "Admin"
                            else -> "Users"
                        }
                        uid?.let { uId ->
                            val userRef = FirebaseUtil.firebaseDatabase.getReference(table).child(email.replace('.', '_'))
                            val user = User(email,name, pass, pNumber)

                            userRef.setValue(user).addOnSuccessListener {
                                val loginF = LoginFragment()
                                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                                transaction.replace(R.id.frameLayout, loginF)
                                transaction.commit()
                            }.addOnFailureListener {
                                Toast.makeText(activity, "Failed to save user data.", Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        Toast.makeText(activity, authTask.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }


        }

        return view
    }

}