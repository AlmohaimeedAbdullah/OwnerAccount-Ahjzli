package com.tuwaiq.owneraccount

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MyStore : Fragment() {

    private lateinit var editBottomSheet:Button
    private lateinit var storeName:TextView
    private lateinit var email:TextView
    private lateinit var branchName:TextView
    private lateinit var branchLocation:TextView
    private lateinit var logOut:TextView
    private lateinit var sharedPreferences: SharedPreferences




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_store, container, false)
        getStoreInfo()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        //shared preference
        sharedPreferences = this.requireActivity().getSharedPreferences(
            "OwnerShared", Context.MODE_PRIVATE)

        editBottomSheet = view.findViewById(R.id.btnEdit)
        storeName = view.findViewById(R.id.txt_storeName_profile)
        email = view.findViewById(R.id.txt_email_Profile)
        branchName = view.findViewById(R.id.txt_branchName)
        branchLocation = view.findViewById(R.id.txt_branchLocation)
        logOut = view.findViewById(R.id.txtLogOut)
        logOut.setOnClickListener {
            getSharedPreferences()
        }
        editBottomSheet.setOnClickListener {
            bottomSheet()
        }

    }


    private fun bottomSheet() {
        val view: View = layoutInflater.inflate(R.layout.bottom_sheet, null)
        val builder = BottomSheetDialog(requireView()?.context)
        builder.setTitle("Forgot Password")

        /*continueBtn.setOnClickListener {
            upDateUserInfo(userNameEt.text.toString(), userPhoneEt.text.toString())
        }*/

        builder.setContentView(view)

        builder.show()
    }
    fun getStoreInfo() = CoroutineScope(Dispatchers.IO).launch {

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

                        storeName.text= name.toString()
                        email.text= ownerEmail.toString()
                        branchName.text= bName.toString()
                        branchLocation.text= bLocation.toString()

                    } else {
                        Log.e("error \n", "errooooooorr")
                    }
                }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                // Toast.makeText(coroutineContext,0,0, e.message, Toast.LENGTH_LONG).show()
                Log.e("FUNCTION createUserFirestore", "${e.message}")
            }
        }
    }

    //to log out
    private fun getSharedPreferences(){
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        sharedPreferences.getString("EMAIL","")
        sharedPreferences.getString("PASSWORD","")
        editor.clear()
        editor.apply()
        findNavController().navigate(MyStoreDirections.actionMyStoreToSignIn())
    }

/*    private fun upDateUserInfo(edName: String, edPhoneNumber: String) {

        val upDateUser = hashMapOf("fullName" to "${edName}",
            "phoneNumber" to "${edPhoneNumber}")
        val userRef = Firebase.firestore.collection("Users_Maharat")
        //-----------UID------------------------
        val uId = FirebaseAuth.getInstance().currentUser?.uid
        userRef.document("$uId").set(upDateUser, SetOptions.merge()).addOnCompleteListener {
            it
            when {
                it.isSuccessful -> {
                    readUserData()
                    Toast.makeText(context, "UpDate ", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    //dialog اسوي فانكشين لدايلوق  و امرر القيمة في الدخو او في الخطاء
                }
            }
        }
    }*/
    }

