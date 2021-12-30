package com.tuwaiq.owneraccount

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tuwaiq.owneraccount.add_customer.AddCustomerData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    private  lateinit var addEventBar:FloatingActionButton
    val uId = FirebaseAuth.getInstance().currentUser?.uid
    private val db = Firebase.firestore.collection("Reservation")
    private var enterNumber:Int = 1
    private  var maxP:Int=0
    private lateinit var sharedPreferences2: SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        addEventBar = findViewById(R.id.addEventBtn)


        bottomNavController()

        addEventBar.setOnClickListener {
            bottomSheetAdd()
        }
    }
    private fun bottomNavController(){
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavView)
        val bottomBar = findViewById<BottomAppBar>(R.id.bottomAppBar)

        bottomNavView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splash -> {
                    bottomNavView.visibility = View.GONE
                    bottomBar.visibility = View.GONE
                    addEventBar.visibility = View.GONE
                }
                R.id.signIn -> {
                    bottomNavView.visibility = View.GONE
                    bottomBar.visibility = View.GONE
                    addEventBar.visibility = View.GONE
                }
                R.id.forgetPassword -> {
                    bottomNavView.visibility = View.GONE
                    bottomBar.visibility = View.GONE
                    addEventBar.visibility = View.GONE
                }
                R.id.register -> {
                    bottomNavView.visibility = View.GONE
                    bottomBar.visibility = View.GONE
                    addEventBar.visibility = View.GONE
                }
                else -> {
                    bottomNavView.visibility = View.VISIBLE
                    bottomBar.visibility = View.VISIBLE
                    addEventBar.visibility = View.VISIBLE
                }
            }
        }
    }
    @SuppressLint("InflateParams")
    private fun bottomSheetAdd() {
        val view: View = layoutInflater.inflate(R.layout.bottom_sheet_add,null)
        val builder = BottomSheetDialog(this)

        val cusName1: EditText = view.findViewById(R.id.et_costumerName_add)
        val cusNumber1: EditText = view.findViewById(R.id.et_phoneNumber_add)
        val cusPeople1: EditText = view.findViewById(R.id.et_maxPeople_add)
        val add_customer_button: Button = view.findViewById(R.id.btnAddCus)

        //creation Date and time
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-SS")
        val formatted = current.format(formatter)
        add_customer_button.setOnClickListener {
            val customerName2 = cusName1.text.toString()
            val customerPhone2 = cusNumber1.text.toString()
            val numOfPeople2 = cusPeople1.text.toString()
            val idReqCus = "$customerPhone2 $formatted"
            val customer = AddCustomerData(uId.toString(),idReqCus,customerName2, customerPhone2, numOfPeople2.toInt())

            val db = FirebaseFirestore.getInstance()
            db.collection("StoreOwner").document("$uId")
                .get().addOnCompleteListener {

                    if (it.result?.exists()!!) {

                        val max = it.result!!.get("maxPeople")
                        maxP=max.toString().toInt()
                        enterNumber = cusPeople1.text.toString().toInt()
                        if ( enterNumber <= maxP){
                            maxP -= enterNumber
                            saveCustomerToTheStore(customer)
                        }else {
                            if (maxP == 0) {
                                Toast.makeText(this@MainActivity, "There is no more space!!",
                                    Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(
                                    this@MainActivity, "You can't reserve more than: $maxP",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        Toast.makeText(this@MainActivity, "maxPeople ${maxP}", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("error \n", "errooooooorr")
                    }
                }
            builder.dismiss()
        }
        builder.setTitle("add")
        builder.setContentView(view)
        builder.show()
    }



    @SuppressLint("CommitPrefEdits")
    private fun upDateTheNumberOfPeople(maxP:Int){
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val upDateUserData = Firebase.firestore.collection("StoreOwner")
        upDateUserData.document("$uid").update("maxPeople", maxP)
        Toast.makeText(this, "$maxP", Toast.LENGTH_SHORT).show()
        sharedPreferences2 = this.getSharedPreferences(
            "OwnerProfile", Context.MODE_PRIVATE)
        val editor3:SharedPreferences.Editor = sharedPreferences2.edit()
        editor3.putString("spMax","$maxP")
        editor3.apply()
    }

    //save to the fire store
    fun saveCustomerToTheStore(cus: AddCustomerData) =
        CoroutineScope(Dispatchers.IO).launch {
        try {
            db.document(cus.idRq).set(cus)
            withContext(Dispatchers.Main) {
                upDateTheNumberOfPeople(maxP)
                Toast.makeText(this@MainActivity, "customer add", Toast.LENGTH_LONG).show()
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}