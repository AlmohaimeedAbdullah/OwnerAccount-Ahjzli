package com.tuwaiq.owneraccount.registeration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.tuwaiq.owneraccount.R


class ForgetPassword : Fragment() {
    private lateinit var enterYourEmail: TextInputEditText
    private lateinit var sendThePassButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_forget_password, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        enterYourEmail = view.findViewById(R.id.tiet_email_forgotPass)
        sendThePassButton = view.findViewById(R.id.btnSendThePass)


        sendThePassButton.setOnClickListener {
            sendTheEmail()
        }
    }

    //send email massage to reset your password
    private fun sendTheEmail() {
        val email = enterYourEmail.text.toString().trim { it <= ' ' }

        if (email.isEmpty()) {
            Toast.makeText(context, "Please enter your E-mail", Toast.LENGTH_SHORT).show()
        } else {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "E-mail send successful to reset your password",
                            Toast.LENGTH_LONG).show()
                        findNavController().navigate(ForgetPasswordDirections.actionForgetPasswordToSignIn())
                    } else {
                        Toast.makeText(context, "The email wasn't correct",
                            Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}