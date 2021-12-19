package com.tuwaiq.owneraccount

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
    private lateinit var maxPeople:TextView
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var switch: Switch
    val uId =FirebaseAuth.getInstance().currentUser?.uid


    //bottom sheet
    private lateinit var bsStoreName:EditText
    private lateinit var bsBranchName:EditText
    private lateinit var bsBranchLocation:EditText
    private lateinit var bsMaxPeople:EditText
    private lateinit var confirmButton:Button



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
        maxPeople = view.findViewById(R.id.txt_maxPeople)

        switch = view.findViewById(R.id.swPublish)
        switch.setOnCheckedChangeListener { _, isChecked ->
            val puplish: Boolean = isChecked
            Toast.makeText(context, puplish.toString(),
                Toast.LENGTH_SHORT).show()
            ifPublish(puplish)
        }

        logOut = view.findViewById(R.id.txtLogOut)
        logOut.setOnClickListener {
            getSPLogOut()
        }
        editBottomSheet.setOnClickListener {
            bottomSheet()
        }

    }

    fun getStoreInfo() = CoroutineScope(Dispatchers.IO).launch {

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

                        storeName.text= name.toString()
                        email.text= ownerEmail.toString()
                        branchName.text= bName.toString()
                        branchLocation.text= bLocation.toString()
                        maxPeople.text = max.toString()

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

    private fun bottomSheet() {
        val view: View = layoutInflater.inflate(R.layout.bottom_sheet, null)
        bsStoreName = view.findViewById(R.id.et_storeName_profile)
        bsBranchName = view.findViewById(R.id.et_branchName_profile)
        bsBranchLocation = view.findViewById(R.id.et_branchLocation_profile)
        bsMaxPeople = view.findViewById(R.id.et_maxPeople_Profile)
        confirmButton = view.findViewById(R.id.btnEditConfirm)

        bsStoreName.setText(storeName.text)
        bsBranchName.setText(branchName.text)
        bsBranchLocation.setText(branchLocation.text)
        bsMaxPeople.setText(maxPeople.text)
        confirmButton.setOnClickListener {
            editProfile()
        }
        val builder = BottomSheetDialog(requireView()?.context)
        builder.setTitle("edit")
        builder.setContentView(view)
        builder.show()
    }

    private fun editProfile() {
        val uId = FirebaseAuth.getInstance().currentUser?.uid
        val upDateUserData = Firebase.firestore.collection("StoreOwner")
        upDateUserData.document(uId.toString()).update("storeName", bsBranchName.text.toString())
        upDateUserData.document(uId.toString()).update("branchName", bsBranchName.text.toString())
        upDateUserData.document(uId.toString()).update("branchLocation", bsBranchLocation.text.toString())
        upDateUserData.document(uId.toString()).update("maxPeople", bsMaxPeople.text.toString())
        Toast.makeText(context,"edit is successful",Toast.LENGTH_LONG).show()
    }

    //to log out
    private fun getSPLogOut(){
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        sharedPreferences.getString("EMAIL","")
        sharedPreferences.getString("PASSWORD","")
        editor.clear()
        editor.apply()
        findNavController().navigate(MyStoreDirections.actionMyStoreToSignIn())
    }

    private fun ifPublish(puplish:Boolean){
        val publish =FirebaseFirestore.getInstance()
        publish.collection("StoreOwner").document("$uId")
            .update("publish",puplish)
    }
}

