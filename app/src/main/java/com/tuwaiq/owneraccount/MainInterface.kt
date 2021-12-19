package com.tuwaiq.owneraccount

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*


class MainInterface : Fragment() {
    private lateinit var rv: RecyclerView
    private lateinit var myAdapter:ReservationRV
    private lateinit var cList:MutableList<AddCustomerData>
    private lateinit var db: FirebaseFirestore



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_interface, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv = view.findViewById(R.id.reservationRV)
        rv.layoutManager = LinearLayoutManager(this.context)
        rv.setHasFixedSize(true)

        cList = mutableListOf()
        myAdapter = ReservationRV(cList)
        rv.adapter = myAdapter
        getTheDataList()
    }
    private fun getTheDataList() {
        db = FirebaseFirestore.getInstance()
        db.collection("Reservation")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){

                        Log.e("Firestore Error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            cList.add(dc.document.toObject(AddCustomerData::class.java))
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                }

            })
    }
}