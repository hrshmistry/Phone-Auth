package com.hrshmistry.gctask

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT:Long = 4000 // 3 sec
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // making the status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity

//          startActivity(Intent(this,MainActivity::class.java))
            startActivity(Intent(this, IntroSliderActivity::class.java))

            // close this activity
            finish()
        }, SPLASH_TIME_OUT)
    }
}