package com.hrshmistry.gctask

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_intro_slider.*
import kotlinx.android.synthetic.main.activity_otp.*

class IntroSliderActivity : AppCompatActivity() {

    private val fragmentList = ArrayList<Fragment>()

//  private var mAuth: FirebaseAuth? = null

    lateinit var preference: SharedPreferences
    var pref_show_intro = "Intro"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        initialize Firebase Auth
//        mAuth = FirebaseAuth.getInstance()

        preference = getSharedPreferences("IntroSlider", Context.MODE_PRIVATE)

        if (!preference.getBoolean(pref_show_intro, true)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // making the status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        setContentView(R.layout.activity_intro_slider)

        val adapter = IntroSliderAdapter(this)
        vpIntroSlider.adapter = adapter

        fragmentList.addAll(listOf(
            Intro1Fragment(), Intro2Fragment(), Intro3Fragment()
        ))
        adapter.setFragmentList(fragmentList)

        indicatorLayout.setIndicatorCount(adapter.itemCount)
        indicatorLayout.selectCurrentPosition(0)

        registerListeners()
    }

//    override fun onStart() {
//        super.onStart()
//
////      Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = mAuth!!.currentUser
//
//        if (currentUser == null) {
////          sendUserToHome()
//            setContentView(R.layout.activity_intro_slider)
//        }
//        else {
//            sendUserToLogin()
//        }
//    }

    private fun registerListeners() {
        vpIntroSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                indicatorLayout.selectCurrentPosition(position)

                if (position < fragmentList.lastIndex) {
                    tvSkip.visibility = View.VISIBLE
                    tvNext.text = "Next"
                } else {
                    tvSkip.visibility = View.GONE
                    tvNext.text = "Get Started"
                }
            }
        })

        tvSkip.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            goToMainActivity()
        }

        tvNext.setOnClickListener {
            val position = vpIntroSlider.currentItem

            if (position < fragmentList.lastIndex) {
                vpIntroSlider.currentItem = position + 1
            } else {
                goToMainActivity()
            }
        }
    }

    fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
        val editor = preference.edit()
        editor.putBoolean(pref_show_intro, false)
        editor.apply()
    }

//    private fun sendUserToHome() {
//        val homeIntent = Intent(this@IntroSliderActivity, MainActivity::class.java)
//        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//        startActivity(homeIntent)
//        finish()
//    }
//
//    private fun sendUserToLogin() {
//        val loginIntent = Intent(this@IntroSliderActivity, LoginActivity::class.java)
//        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//        startActivity(loginIntent)
//        finish()
//    }

}