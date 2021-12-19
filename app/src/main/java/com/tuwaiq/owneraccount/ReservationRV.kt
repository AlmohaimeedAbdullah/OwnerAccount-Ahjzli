package com.tuwaiq.owneraccount

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReservationRV(
    private val customerListList: List<AddCustomerData>,
) : RecyclerView.Adapter<CustomHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomHolder {
        // inflate layout
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.reservation_rv_list, parent, false)
        return CustomHolder(view)

    }

    override fun onBindViewHolder(holder: CustomHolder, position: Int) {
        val custom = customerListList[position]
        holder.cusName.text = custom.customerName
        holder.cusNumber.text = custom.phoneNumber
        holder.cusPeople.text = custom.numberOfPeople

    }

    override fun getItemCount(): Int = customerListList.size
}

class CustomHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val cusName: TextView = itemView.findViewById(R.id.txtCustomerNameAdd)
    val cusNumber: TextView = itemView.findViewById(R.id.txtPhoneNumberAdd)
    val cusPeople: TextView = itemView.findViewById(R.id.txtNumberOfPeopleAdd)





    }


