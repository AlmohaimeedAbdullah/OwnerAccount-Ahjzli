package com.tuwaiq.owneraccount

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    private val db = Firebase.firestore.collection("StoreOwner")/*
    private lateinit var addCustomerButton: Button
    private lateinit var cusName:EditText
    private lateinit var cusPhone:EditText
    private lateinit var numberOfPeople:EditText*/

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
        builder.setTitle("add")
        builder.setContentView(view)
        builder.show()

        val cusName1: EditText = view.findViewById(R.id.et_costumerName_add)
        val cusNumber1: EditText = view.findViewById(R.id.et_phoneNumber_add)
        val cusPeople1: EditText = view.findViewById(R.id.et_maxPeople_add)
        val add_customer_button: Button = view.findViewById(R.id.btnAddCus)

        //creation Date and time
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")
        val formatted = current.format(formatter)
        add_customer_button.setOnClickListener {

            val customerName2 = cusName1.text.toString()
            val customerPhone2 = cusNumber1.text.toString()
            val numOfPeople2 = cusPeople1.text.toString()
            val idReqCus = "$customerPhone2$formatted"
            val customer = AddCustomerData(idReqCus,customerName2, customerPhone2, numOfPeople2.toInt())
            saveCustomerToTheStore(customer)
        }
    }

    //save to the fire store
    fun saveCustomerToTheStore(cus: AddCustomerData) =
        CoroutineScope(Dispatchers.IO).launch {
            val uid = FirebaseAuth.getInstance().currentUser?.uid

        try {
            db.document(uid.toString()).collection("Reservation").document(cus.userId).set(cus)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, "customer add", Toast.LENGTH_LONG).show()
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}