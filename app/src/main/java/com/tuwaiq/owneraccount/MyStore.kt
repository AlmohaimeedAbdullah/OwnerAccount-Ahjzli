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
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tuwaiq.owneraccount.add_customer.AddCustomerData
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
    private lateinit var sharedPreferences2: SharedPreferences
    private lateinit var maxPeople:TextView
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var switch: Switch
    //bottom sheet
    private lateinit var bsStoreName:EditText
    private lateinit var bsBranchName:EditText
    private lateinit var bsMaxPeople:EditText
    private lateinit var confirmButton:Button



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_store, container, false)
        //getStoreInfo()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //getStoreInfo()
        deleteReservation()
        //shared preference
        sharedPreferences = this.requireActivity().getSharedPreferences(
            "OwnerShared", Context.MODE_PRIVATE)
        sharedPreferences2 = requireActivity().getSharedPreferences(
            "OwnerProfile", Context.MODE_PRIVATE)

        editBottomSheet = view.findViewById(R.id.btnEdit)
        storeName = view.findViewById(R.id.txt_storeName_profile)
        email = view.findViewById(R.id.txt_email_Profile)
        branchName = view.findViewById(R.id.txt_branchName)
        branchLocation = view.findViewById(R.id.txt_branchLocation)
        maxPeople = view.findViewById(R.id.txt_maxPeople)

        sharedPreferences2 = this.requireActivity().getSharedPreferences(
            "OwnerProfile", Context.MODE_PRIVATE)
        val sp1 = sharedPreferences2.getString("spStoreName"," ")
        val sp2 = sharedPreferences2.getString("spEmail"," ")
        val sp3 = sharedPreferences2.getString("spBranchName"," ")
        val sp4 = sharedPreferences2.getString("spBranchLocation"," ")
        val sp5 = sharedPreferences2.getString("spMax"," ")
        storeName.text= sp1
        email.text= sp2
        branchName.text= sp3
        branchLocation.text= sp4
        maxPeople.text = sp5

        switch = view.findViewById(R.id.swPublish)

        val switchValue = sharedPreferences2.getBoolean("publish",false)
        switch.isChecked = switchValue
        switch.setOnCheckedChangeListener { _, isChecked ->
            val publish: Boolean = isChecked
            Toast.makeText(context, publish.toString(),Toast.LENGTH_SHORT).show()
            ifPublish(publish)
        }

        logOut = view.findViewById(R.id.txtLogOut)
        logOut.setOnClickListener {
            getSPLogOut()
        }
        editBottomSheet.setOnClickListener {
            bottomSheet()
        }

    }

    private fun bottomSheet() {
        val view: View = layoutInflater.inflate(R.layout.bottom_sheet, null)
        bsStoreName = view.findViewById(R.id.et_storeName_profile)
        bsBranchName = view.findViewById(R.id.et_branchName_profile)
        bsMaxPeople = view.findViewById(R.id.et_maxPeople_Profile)
        confirmButton = view.findViewById(R.id.btnEditConfirm)

        bsStoreName.setText(storeName.text.toString())
        bsBranchName.setText(branchName.text.toString())
        bsMaxPeople.setText(maxPeople.text)

        val builder = BottomSheetDialog(requireView().context)
        confirmButton.setOnClickListener {
            editStoreProfile()
            //save the changes in the sp
            val editor3:SharedPreferences.Editor = sharedPreferences2.edit()
            editor3.putString("spStoreName",bsStoreName.text.toString())
            editor3.putString("spBranchName",bsBranchName.text.toString())
            editor3.putString("spMax",bsMaxPeople.text.toString())
            editor3.apply()
            storeName.text= bsStoreName.text.toString()
            branchName.text= bsBranchName.text.toString()
            maxPeople.text = bsMaxPeople.text.toString()
            builder.dismiss()
        }
        builder.setTitle("edit")
        builder.setContentView(view)
        builder.show()
    }

    private fun editStoreProfile() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val upDateUserData = Firebase.firestore.collection("StoreOwner")
        upDateUserData.document(uid.toString()).update("storeName", bsStoreName.text.toString(),
            "branchName", bsBranchName.text.toString(),
            "maxPeople", bsMaxPeople.text.toString().toInt())
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

    private fun ifPublish(publish:Boolean):Boolean{
        val uId =FirebaseAuth.getInstance().currentUser?.uid
        val dataBase =FirebaseFirestore.getInstance()
        dataBase.collection("StoreOwner").document("$uId")
            .update("publish",publish)
        sharedPreferences2 = requireActivity().getSharedPreferences("OwnerProfile", Context.MODE_PRIVATE)
        val editor3:SharedPreferences.Editor = sharedPreferences2.edit()
        editor3.putBoolean("publish",publish)
        editor3.apply()
        return publish
    }

    private fun deleteReservation(){
        val id =FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        db.collection("StoreOwner").whereEqualTo("idOwner", id)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {

                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.MODIFIED) {
                            val max = dc.document.data.get("maxPeople")
                            val editor3:SharedPreferences.Editor = sharedPreferences2.edit()
                            editor3.putString("spMax",max.toString())
                            editor3.apply()
                            maxPeople.text= max.toString()
                            Log.d("maxPeople",max.toString())
                        }
                    }
                }
            })
    }
}

