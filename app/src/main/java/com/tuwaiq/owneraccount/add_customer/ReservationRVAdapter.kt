package com.tuwaiq.owneraccount

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tuwaiq.owneraccount.add_customer.AddCustomerData

class ReservationRVAdapter(
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
        holder.cusName.text = custom.userName
        holder.cusNumber.text = custom.userPhone
        holder.cusPeople.text = custom.numberOfTheCustomer.toInt().toString()

    }

    override fun getItemCount(): Int = customerListList.size
}

class CustomHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val cusName: TextView = itemView.findViewById(R.id.txtCustomerNameAdd)
    val cusNumber: TextView = itemView.findViewById(R.id.txtPhoneNumberAdd)
    val cusPeople: TextView = itemView.findViewById(R.id.txtNumberOfPeopleAdd)

    }


