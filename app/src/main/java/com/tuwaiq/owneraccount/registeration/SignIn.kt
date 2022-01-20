package com.tuwaiq.owneraccount.registeration

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tuwaiq.owneraccount.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SignIn : Fragment() {
    private lateinit var dontHaveAccount:TextView
    private lateinit var forgetPassword:TextView
    private lateinit var signInButton:Button
    private lateinit var enterTheEmail: TextInputEditText
    private lateinit var enterThePass: TextInputEditText
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferences2: SharedPreferences
    private lateinit var rememberMe: CheckBox
    private lateinit var progressBar: ProgressBar
    var checkBoxValue = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        enterTheEmail = view.findViewById(R.id.tiet_email_sign_in)
        enterThePass = view.findViewById(R.id.tiet_password_sign_in)
        dontHaveAccount = view.findViewById(R.id.txt_dont_have_account)
        signInButton = view.findViewById(R.id.btnSignIn)
        progressBar = view.findViewById(R.id.progressBarSingIn)

        forgetPassword = view.findViewById(R.id.txt_forget_password)
        forgetPassword.setOnClickListener {
            findNavController().navigate(SignInDirections.actionSignInToForgetPassword())
        }

        dontHaveAccount.setOnClickListener {
            findNavController().navigate(SignInDirections.actionSignInToRegister())
        }

        signInButton = view.findViewById(R.id.btnSignIn)
        signInButton.setOnClickListener {
            signInButton.isClickable = false
            progressBar.isVisible = true
            signIn()
        }

        //sharedPreferences
        sharedPreferences2 = this.requireActivity().getSharedPreferences("OwnerProfile", Context.MODE_PRIVATE)
        rememberMe = view.findViewById(R.id.cbRememberMe)
        sharedPreferences = this.requireActivity().getSharedPreferences("OwnerShared",Context.MODE_PRIVATE)
        checkBoxValue = sharedPreferences.getBoolean("CHECKBOX", false)
        if (checkBoxValue){
            findNavController().navigate(SignInDirections.actionSignInToMainInterface())
        }
    }

    private fun  signIn(){
        val email = enterTheEmail.text.toString().trim{it <= ' '}
        val password = enterThePass.text.toString().trim{it <= ' '}
        if (email.isNotEmpty() && password.isNotEmpty()){
            //get the email and the pass from Auth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {task ->
                    if (task.isSuccessful){
                        checkInTheFireStore()
                    }else{
                        // if the registration is not successful then show error massage
                        Toast.makeText(context, "Please make sure the values are correct, or fill the fields",
                            Toast.LENGTH_LONG).show()
                        signInButton.isClickable = true
                        progressBar.isVisible = false
                    }
                }
        }else{
            Toast.makeText(context, "please enter all fields", Toast.LENGTH_LONG)
                .show()
            signInButton.isClickable = true
            progressBar.isVisible = false
        }
    }

    //check In The Fire Store
    private fun checkInTheFireStore(){
        val uId =FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        db.collection("StoreOwner").document("$uId").get()
            .addOnCompleteListener {
                if (it.result?.exists()!!){
                    getStoreInfo()
                    findNavController().navigate(SignInDirections.actionSignInToMainInterface())
                    checkBox()
                }else{
                    Toast.makeText(context, "Please make sure the values are correct",
                        Toast.LENGTH_LONG).show()
                    signInButton.isClickable = true
                    progressBar.isVisible = false
                }
            }
    }

    private fun checkBox(){
        val emailPreference: String = enterTheEmail.text.toString()
        val passwordPreference: String = enterThePass.text.toString()
        val checked: Boolean = rememberMe.isChecked
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("EMAIL", emailPreference)
        editor.putString("PASSWORD", passwordPreference)
        editor.putBoolean("CHECKBOX", checked)
        editor.apply()
    }

    private fun getStoreInfo() = CoroutineScope(Dispatchers.IO).launch {
        val uId =FirebaseAuth.getInstance().currentUser?.uid
        try {
            val db = FirebaseFirestore.getInstance()
            db.collection("StoreOwner").document("$uId")
                .get().addOnCompleteListener {

                    if (it.result?.exists()!!) {
                        //+++++++++++++++++++++++++++++++++++++++++
                        val name = it.result!!.getString("storeName")
                        val ownerEmail = it.result!!.getString("storeOwnerEmail")
                        val bName = it.result!!.getString("branchName")
                        val bLocation = it.result!!.getString("branchLocation")
                        val max = it.result!!.get("maxPeople")

                        //to save the info in the sp
                        sharedPreferences2.edit()
                        .putString("spStoreName",name.toString())
                        .putString("spEmail",ownerEmail.toString())
                        .putString("spBranchName",bName.toString())
                        .putString("spBranchLocation",bLocation.toString())
                        .putString("spMax",max.toString())
                        .apply()
                    } else {
                        Log.e("error", "getStoreInfo")
                    }
                }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                // Toast.makeText(coroutineContext,0,0, e.message, Toast.LENGTH_LONG).show()
                Log.e("FUNCTION createUserFirestore", "${e.message}")
            }
        }
    }
}