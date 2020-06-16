package com.hrshmistry.gctask

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.*

class OtpActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var mCurrentUser: FirebaseUser? = null

    private var mAuthVerificationId: String? = null
    private var mOtpText: EditText? = null

    private var mVerifyBtn: Button? = null
    private var mOtpProgress: ProgressBar? = null
    private var mOtpFeedback: TextView? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        mAuth = FirebaseAuth.getInstance()
        mCurrentUser = mAuth!!.currentUser

        mAuthVerificationId = intent.getStringExtra("AuthCredentials")

        mOtpFeedback = findViewById(R.id.otp_form_feedback)
        mOtpProgress = findViewById(R.id.otp_progress_bar)
        mOtpText = findViewById(R.id.otp_text_view)
        mVerifyBtn = findViewById(R.id.verify_btn)

        mVerifyBtn?.setOnClickListener(View.OnClickListener {
            val otp = mOtpText?.getText().toString()

            if (otp.isEmpty()) {
                mOtpFeedback?.setVisibility(View.VISIBLE)
                mOtpFeedback?.setText("Please fill in the form and try again.")
            } else {
                mOtpProgress?.setVisibility(View.VISIBLE)
                mVerifyBtn?.setEnabled(false)
                val credential =
                    PhoneAuthProvider.getCredential(mAuthVerificationId!!, otp)
                signInWithPhoneAuthCredential(credential)
            }

        })

    }

    @SuppressLint("SetTextI18n")
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(
                this@OtpActivity
            ) { task ->
                if (task.isSuccessful) {
                    sendUserToHome()
                    // ...
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        mOtpFeedback!!.visibility = View.VISIBLE
                        mOtpFeedback!!.text = "There was an error verifying OTP"
                    }
                }
                mOtpProgress!!.visibility = View.INVISIBLE
                mVerifyBtn!!.isEnabled = true
            }
    }

    override fun onStart() {
        super.onStart()
        if (mCurrentUser != null) {
            sendUserToHome()
        }
    }

    fun sendUserToHome() {
        val homeIntent = Intent(this@OtpActivity, MainActivity::class.java)
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(homeIntent)
        finish()
    }
}