package com.tuwaiq.owneraccount

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class Register : Fragment() {
    private val db = Firebase.firestore.collection("StoreOwner")
    private lateinit var enterStoreName:TextInputEditText
    private lateinit var enterEmailOwner:TextInputEditText
    private lateinit var enterpass:TextInputEditText
    private lateinit var enterBranchName:TextInputEditText
    private lateinit var enterBranchLocation:EditText
    private lateinit var signUpButton:Button
    private lateinit var haveAccount: TextView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        enterStoreName = view.findViewById(R.id.tiet_store_name_sign_up)
        enterEmailOwner= view.findViewById(R.id.tiet_email_sign_up)
        enterpass= view.findViewById(R.id.tiet_password_sign_up)
        enterBranchName= view.findViewById(R.id.tiet_branch_name_sign_up)
        enterBranchLocation= view.findViewById(R.id.et_branch_location)
        signUpButton= view.findViewById(R.id.btnSignUp)
        haveAccount = view.findViewById(R.id.txt_have_account)

        haveAccount.setOnClickListener {
            findNavController().navigate(RegisterDirections.actionRegisterToSignIn())
        }

        signUpButton.setOnClickListener {
            registerUser()
        }

    }
    // registerUser()
    private fun registerUser() {
        val storeName = enterStoreName.text.toString()
        val email: String = enterEmailOwner.text.toString().trim { it <= ' ' }
        val password = enterpass.text.toString().trim { it <= ' ' }
        //Phone number must be 10
        val branchName = enterBranchName.text.toString()
        val branchLocation = enterBranchLocation.text.toString()
        val account = OwnerData(storeName, email, branchName,branchLocation)

        if (storeName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && branchName.isNotEmpty() && branchLocation.isNotEmpty()) {
            //save to the Authentication
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "You were registered successful", Toast.LENGTH_LONG)
                            .show()
                        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                        saveStore(account)

                    } else {
                        // if the registration is not successful then show error massage
                        Toast.makeText(
                            context, "Please make sure the values are correct, or fill the fields",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
        } else {
            Toast.makeText(context, "please enter all fields", Toast.LENGTH_LONG)
                .show()
        }
    }


    //push to fire store
    private fun saveStore(store: OwnerData) = CoroutineScope(Dispatchers.IO).launch {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        try {
            db.document("$uid").set(store)
            withContext(Dispatchers.Main) {
                findNavController().navigate(RegisterDirections.actionRegisterToMainInterface())
                Toast.makeText(context, "saved data", Toast.LENGTH_LONG).show()
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}