package com.tuwaiq.owneraccount

data class OwnerData(
    val storeName:String,
    val storeOwnerEmail:String,
    val branchName:String,
    val branchLocation:String,
    val maxPeople:Int =5,
    val publish: Boolean= false
)