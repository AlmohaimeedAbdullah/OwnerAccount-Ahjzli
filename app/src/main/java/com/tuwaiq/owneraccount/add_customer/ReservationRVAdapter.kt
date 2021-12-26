package com.tuwaiq.owneraccount

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
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
        holder.cusPeople.text = custom.numberOfTheCustomer.toString()


    }

    override fun getItemCount(): Int = customerListList.size
}

class CustomHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {

    val cusName: TextView = itemView.findViewById(R.id.txtCustomerNameAdd)
    val cusNumber: TextView = itemView.findViewById(R.id.txtPhoneNumberAdd)
    val cusPeople: TextView = itemView.findViewById(R.id.txtNumberOfPeopleAdd)



    init {
        itemView.setOnClickListener(this)
    }
    override fun onClick(v: View?) {

        cusNumber.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${cusNumber.text}")
            v?.context?.startActivity(intent)
        }

    }

}


