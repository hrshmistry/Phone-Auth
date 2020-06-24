package com.hrshmistry.gctask

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit

class LoginActivity() : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    private var mCountryCode: EditText? = null
    private var mPhoneNumber: EditText? = null

    private var mGenerateBtn: Button? = null
    private var mLoginProgress: ProgressBar? = null

    private var mLoginFeedbackText: TextView? = null

    private var mCallbacks: OnVerificationStateChangedCallbacks? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//      initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        mCountryCode = findViewById(R.id.country_code_text)
        mPhoneNumber = findViewById(R.id.phone_number_text)

        mGenerateBtn = findViewById(R.id.generate_btn)
        mLoginProgress = findViewById(R.id.login_progress_bar)

        mLoginFeedbackText = findViewById(R.id.login_form_feedback)


        mGenerateBtn?.setOnClickListener(View.OnClickListener {
            val country_code = mCountryCode?.getText().toString()
            val phone_number = mPhoneNumber?.getText().toString()

            val complete_phone_number = "+$country_code$phone_number"

            if (country_code.isEmpty() || phone_number.isEmpty()) {
                mLoginFeedbackText?.setText("Please fill in the form to continue.")
                mLoginFeedbackText?.setVisibility(View.VISIBLE)
            } else {
                mLoginProgress?.setVisibility(View.VISIBLE)
                mGenerateBtn?.setEnabled(false)

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    complete_phone_number,      // Phone number to verify
                    60,                         // Timeout duration
                    TimeUnit.SECONDS,           // Unit of timeout
                    this@LoginActivity,         // Activity (for callback binding)
                    mCallbacks!!                // OnVerificationStateChangedCallbacks
                )

            }

        })

        mCallbacks = object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                mLoginFeedbackText?.setText("Verification Failed, please try again.")
                mLoginFeedbackText?.setVisibility(View.VISIBLE)
                mLoginProgress?.setVisibility(View.INVISIBLE)
                mGenerateBtn?.setEnabled(true)
            }

            override fun onCodeSent(
                s: String,
                forceResendingToken: ForceResendingToken
            ) {
                super.onCodeSent(s, forceResendingToken)

//              for auto OTP verification
//                Handler().postDelayed(
//                    {
//                        val otpIntent =
//                            Intent(this@LoginActivity, OtpActivity::class.java)
//                        otpIntent.putExtra("AuthCredentials", s)
//                        startActivity(otpIntent)
//                    },
//                    10000 // 10 sec
//                )

//              manually sending user to OTP verify page to write OTP

              val otpIntent =
                  Intent(this@LoginActivity, OtpActivity::class.java)
              otpIntent.putExtra("AuthCredentials", s)
              startActivity(otpIntent)

            }

        }

    }

    override fun onStart() {
        super.onStart()

//      Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.currentUser

        if (currentUser != null) {
            sendUserToHome()
        }
//        else {
//            setContentView(R.layout.activity_login)
//        }
    }

    @SuppressLint("SetTextI18n")
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(
                this@LoginActivity
            ) { task ->
                if (task.isSuccessful) {
                    sendUserToHome()
                    // ...
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        mLoginFeedbackText!!.visibility = View.VISIBLE
                        mLoginFeedbackText!!.text = "There was an error verifying OTP"
                    }
                }
                mLoginProgress!!.visibility = View.INVISIBLE
                mGenerateBtn!!.isEnabled = true
            }
    }

    private fun sendUserToHome() {
        val homeIntent = Intent(this@LoginActivity, MainActivity::class.java)
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(homeIntent)
        finish()
    }
}

