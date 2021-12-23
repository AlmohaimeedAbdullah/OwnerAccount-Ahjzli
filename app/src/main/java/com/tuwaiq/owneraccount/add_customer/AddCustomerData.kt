package com.tuwaiq.owneraccount.add_customer

data class AddCustomerData(
    val ownerId:String = "",
    val idRq:String="",
    val userName :String="",
    val userPhone: String="",
    val numberOfTheCustomer:Int=0
)