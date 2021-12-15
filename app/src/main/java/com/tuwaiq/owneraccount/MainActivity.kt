package com.tuwaiq.owneraccount

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private  lateinit var addEventBar:FloatingActionButton
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
    private fun bottomSheetAdd() {
        val view: View = layoutInflater.inflate(R.layout.bottom_sheet_add, null)
        val builder = BottomSheetDialog(this)
        builder.setTitle("Forgot Password")
        builder.setContentView(view)

        builder.show()
    }
}