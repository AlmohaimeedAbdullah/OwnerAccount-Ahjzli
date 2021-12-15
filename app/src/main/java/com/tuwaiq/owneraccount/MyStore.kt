package com.tuwaiq.owneraccount

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MyStore : Fragment() {

    private lateinit var editBottomSheet:Button
    private lateinit var storeName:TextView
    private lateinit var email:TextView
    private lateinit var branchName:TextView
    private lateinit var branchLocation:TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_store, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        editBottomSheet = view.findViewById(R.id.btnEdit)
        storeName = view.findViewById(R.id.txt_storeName_profile)
        email = view.findViewById(R.id.txt_email_Profile)
        branchName = view.findViewById(R.id.txt_branchName)
        branchLocation = view.findViewById(R.id.txt_branchLocation)
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

/*    private fun upDateUserInfo(edName: String, edPhoneNumber: String) {

        val upDateUser = hashMapOf(
            "fullName" to "${edName}",
            "phoneNumber" to "${edPhoneNumber}"
        )
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

