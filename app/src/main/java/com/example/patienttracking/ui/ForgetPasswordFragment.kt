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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentTransaction
import com.example.patienttracking.R
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordFragment : Fragment() {
    private lateinit var auth: FirebaseAuth



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_forget_password, container, false)
        var etPassword = view.findViewById<EditText>(R.id.resetEmail)
        var passChangeBtn = view.findViewById<Button>(R.id.passwordChangeBtn)
        val bcBtn = view.findViewById<TextView>(R.id.bLBtn)
        auth = FirebaseAuth.getInstance()

        var builder : AlertDialog.Builder
        var con = this.context
        con?.let {
            builder = AlertDialog.Builder(con)
        }
        passChangeBtn.setOnClickListener {
            con?.let {
              val sPassword =   etPassword.text.toString()
                auth.sendPasswordResetEmail(sPassword)
                    .addOnSuccessListener {
                        builder = AlertDialog.Builder(con)
                        builder.setTitle("Alert!")

                            .setMessage("A link has been sent to your email")
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
                    }.addOnFailureListener {
                        Toast.makeText(activity, it.toString(), Toast.LENGTH_LONG).show()
                    }
            }
        }

        bcBtn.setOnClickListener {
            val logF = LoginFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout, logF)
            transaction.commit()
        }

        return view
    }
}