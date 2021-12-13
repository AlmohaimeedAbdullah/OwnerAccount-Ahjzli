package com.tuwaiq.owneraccount

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController


class SignIn : Fragment() {
    private lateinit var dontHaveAccount:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        dontHaveAccount = view.findViewById(R.id.txt_dont_have_account)
        dontHaveAccount.setOnClickListener {
            findNavController().navigate(SignInDirections.actionSignInToRegister())
        }

    }
}