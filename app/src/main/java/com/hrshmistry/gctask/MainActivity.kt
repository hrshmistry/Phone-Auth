package com.hrshmistry.gctask

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var mAuth: FirebaseAuth? = null

    private var mLogoutBtn: Button? = null

    var toolbar: Toolbar? = null
    var drawerLayout: DrawerLayout? = null
    var navView: NavigationView? = null

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.app_name, 0
        )
        drawerLayout?.addDrawerListener(toggle)
        toggle.syncState()
        navView?.setNavigationItemSelectedListener(this)

//      initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        mLogoutBtn = findViewById(R.id.logout_btn)

        mLogoutBtn?.setOnClickListener(View.OnClickListener {
            mAuth!!.signOut()
            sendUserToLogin()
        })

    }

    override fun onStart() {
        super.onStart()

//      Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.currentUser

        if (currentUser == null) {
            sendUserToLogin()
        }
//      else
//      {
//         setContentView(R.layout.activity_main)
//      }
    }

    private fun sendUserToLogin() {
        val loginIntent = Intent(this@MainActivity, LoginActivity::class.java)
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(loginIntent)
        finish()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                Toast.makeText(
                    this,
                    "Home clicked",
                    Toast.LENGTH_SHORT
                ).show()
            }
            R.id.nav_search -> {
                Toast.makeText(
                    this,
                    "Search clicked",
                    Toast.LENGTH_SHORT
                ).show()
            }
            R.id.nav_about_us -> {
                Toast.makeText(
                    this,
                    "About Us clicked",
                    Toast.LENGTH_SHORT
                ).show()
            }
            R.id.nav_my_profile -> {
                Toast.makeText(
                    this,
                    "My Profile clicked",
                    Toast.LENGTH_SHORT
                ).show()
            }
            R.id.nav_logout -> {
                Toast.makeText(
                    this,
                    "Logout clicked",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        drawerLayout?.closeDrawer(GravityCompat.START)
        return true
    }
}

