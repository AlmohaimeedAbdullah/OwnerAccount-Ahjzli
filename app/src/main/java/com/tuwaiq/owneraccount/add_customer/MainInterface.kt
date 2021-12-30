package com.tuwaiq.owneraccount.add_customer

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tuwaiq.owneraccount.R
import com.tuwaiq.owneraccount.ReservationRVAdapter
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class MainInterface : Fragment() {
    private lateinit var rv: RecyclerView
    private lateinit var myAdapter: ReservationRVAdapter
    private lateinit var cList:MutableList<AddCustomerData>
    private  var db = Firebase.firestore

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
        myAdapter = ReservationRVAdapter(cList)
        rv.adapter = myAdapter

        val taskTouchHelper= ItemTouchHelper(simpleCallback)
        taskTouchHelper.attachToRecyclerView(rv)
       // getTheDataList()
        getTheReservationList()
    }
    private var simpleCallback= object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.layoutPosition
            val deleteCus = cList[position]
            when (direction) {
                ItemTouchHelper.LEFT -> {
                    deleteReservation(deleteCus)
                    cList.remove(deleteCus)
                    ReservationRVAdapter(cList).notifyItemRemoved(position)

                    addNumberTheOwner(deleteCus.ownerId,deleteCus.numberOfTheCustomer)
                }
                ItemTouchHelper.RIGHT -> {
                    deleteReservation(deleteCus)
                    cList.remove(deleteCus)
                    ReservationRVAdapter(cList).notifyItemRemoved(position)
                    addNumberTheOwner(deleteCus.ownerId,deleteCus.numberOfTheCustomer)

                }
            }
        }

        private fun addNumberTheOwner(ownerId:String, numberOfTheCustomer:Int) {
            db.collection("StoreOwner").document(ownerId)
                .get().addOnCompleteListener {
                    if (it.result?.exists()!!) {
                        val maxP = it.result!!.get("maxPeople").toString().toInt()
                        db.collection("StoreOwner").document(ownerId)
                            .update("maxPeople", numberOfTheCustomer + maxP)
                    }
                }
        }

        override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeLeftBackgroundColor(android.graphics.Color.parseColor("#E80000"))
                .addSwipeRightBackgroundColor(android.graphics.Color.parseColor("#E80000"))
                .addSwipeLeftActionIcon(R.drawable.ic_delete)
                .addSwipeRightActionIcon(R.drawable.ic_delete)
                .create()
                .decorate()
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }
    private fun deleteReservation(delete:AddCustomerData){
        db.collection("Reservation").document(delete.idRq).delete()
    }
private fun getTheReservationList() {

    val id =FirebaseAuth.getInstance().currentUser?.uid
    db.collection("Reservation").whereEqualTo("ownerId", id.toString())
        .addSnapshotListener(object : EventListener<QuerySnapshot> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("Firestore Add", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {

                        cList.add(dc.document.toObject(AddCustomerData::class.java))
                    }
                }
                myAdapter.notifyDataSetChanged()
            }
        })
}
}