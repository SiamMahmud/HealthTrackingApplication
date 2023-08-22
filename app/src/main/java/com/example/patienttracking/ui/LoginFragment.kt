package com.example.patienttracking.ui

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.patienttracking.R
import com.example.patienttracking.utils.FirebaseUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class LoginFragment : Fragment() {

    private lateinit var email: EditText
    private lateinit var pass: EditText
    private lateinit var loginButton: Button
    private lateinit var signUpButton: TextView
    private lateinit var forgotPassword: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        email = view.findViewById(R.id.emailLogin)
        pass = view.findViewById(R.id.userPasswordLogin)
        loginButton = view.findViewById(R.id.loginBtn)
        signUpButton = view.findViewById(R.id.GoSignUpBtn)
        forgotPassword = view.findViewById(R.id.forgetPassword)

        auth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            val emailE = email.text.toString()
            val passP = pass.text.toString()

            if (emailE.isNotBlank() && passP.isNotEmpty()) {
                auth.signInWithEmailAndPassword(emailE, passP)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val currentUser = auth.currentUser
                            currentUser?.let { firebaseUser ->
                                val domain = emailE.substringAfterLast("@").toLowerCase()
                                Log.d("LoginFragment", "Domain: $domain")
                                when (domain) {
                                    "doctor.com" -> {
                                        openFragment(DoctorMainPageFragment())
                                    }
                                    "admin.com" -> {
                                        openFragment(AdminMainPageFragment())
                                    }
                                    "gmail.com" -> {
                                        openFragment(PatientMainPageFragment())
                                    }
                                    else -> {
                                        Log.d("LoginFragment", "User Email not found")
                                        Toast.makeText(
                                            activity,
                                            "User Email not found",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        } else {
                            Log.d("LoginFragment", "signInWithEmailAndPassword:failure", task.exception)
                        }
                    }
            } else {
                if (emailE.isEmpty()) {
                    email.error = "Email Required"
                } else if (passP.isEmpty()) {
                    pass.error = "Password Required"
                } else {

                }
            }
            val loading = LoadingDialog(requireActivity())
            loading.startLoading()
            val handler = Handler()
            handler.postDelayed(object :Runnable{
                override fun run() {
                    loading.isDismiss()
                }

            },2000)


        }

        signUpButton.setOnClickListener {
            val signPages = SignUpFragment()
            openFragment(signPages)
        }

        forgotPassword.setOnClickListener {
            val forgotPasswordFragment = ForgetPasswordFragment()
            openFragment(forgotPasswordFragment)
        }

        return view
    }

    private fun openFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }
}


//    private fun setData(pass: String) {
//        val doctorDatabase = getInstance().getReference("Doctor")
//        val adminDatabase = getInstance().getReference("Admin")
//        val userDatabase = getInstance().getReference("Users")
//
//        val domain = firebaseAuth.currentUser?.email?.substringAfterLast("@")
//        val database: DatabaseReference = when (domain) {
//            "doctor.com" -> doctorDatabase
//            "admin.com" -> adminDatabase
//            "gmail.com" -> userDatabase
//            else -> userDatabase
//        }
//        database.child(pass).get().addOnSuccessListener { snapshot ->
//            if (snapshot.exists()) {
//                SignUp.userEmail = snapshot.child("email").value as String
//                SignUp.userName = snapshot.child("name").value as String
//                SignUp.userPhoneNumber = snapshot.child("phoneNumber").value as String
//            } else {
//                makeText(requireContext(), "User already exists", Toast.LENGTH_LONG).show()
//            }
//        }.addOnFailureListener {
//            makeText(requireContext(), "User not found", Toast.LENGTH_LONG).show()
//        }
//    }












