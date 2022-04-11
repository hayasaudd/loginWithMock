package com.example.tamaraandroidassignment


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.tamaraandroidassignment.databinding.ActivityLoginBinding


lateinit var binding: ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val viewModel: viewModel by viewModels()
    var passwordValid = false
    var emailValid = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //bind logIn layout
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        emailFocusListener()
        passwordFocusListener()
        observers()
        //bind log in button
        binding.button.setOnClickListener {
            Log.e("TAG", " userISValid in click button   ${viewModel.userIsValid}")
            clickbutton()


        }

    }

    fun observers() {
        viewModel.userIsValid.observe(this, Observer {
            if (it) {
                Toast.makeText(this@LoginActivity, "navigat to next page ", Toast.LENGTH_SHORT)
                    .show()
                startActivity(Intent(this@LoginActivity, welcomeActivity::class.java))
            }
        })

        viewModel.errorMassege.observe(this, Observer {
            Toast.makeText(
                this@LoginActivity,
                it,
                Toast.LENGTH_SHORT
            ).show()
        })

    }


    //when the user press the signIn button
    fun clickbutton() {
        if (emailValid) {
            viewModel.checkUser(
                binding.editTextEmail.text.toString().toLowerCase(),
                binding.editTextPassword.text.toString()
            )

        } else {
            Toast.makeText(this, "pleas insure your email or password", Toast.LENGTH_SHORT).show()

        }
    }

    //email validation
    private fun emailFocusListener() {

        binding.editTextEmail.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                var checker = validEmail()
                if (checker != null) {
                    binding.emailContainer.helperText = checker
                    emailValid = false

                } else {
                    binding.emailContainer.helperText = checker
                    emailValid = true
                }
            }
        }
    }

    //function check if the is email valid or not
    private fun validEmail(): String? {
        val emailText = binding.editTextEmail.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return "Invalid Email Address"
        }
        return null
    }

    //password validation
    private fun passwordFocusListener() {
        binding.editTextPassword.setOnFocusChangeListener { _, focused ->
            if (focused) {
                var checkerPass = validpassword()
                if (checkerPass != null) {
                    binding.passwordContainer.helperText = checkerPass
                    passwordValid = false
                } else {
                    binding.passwordContainer.helperText = checkerPass
                    passwordValid = true
                }
            }
        }
    }


    //function check if the password is valid or not
    private fun validpassword(): String? {
        val passText = binding.editTextPassword.text.toString()
        if (passText.length < 8) {
            return "Minimum 8 Character Password"
        }
        if (!passText.matches(".*[A-Z].*".toRegex())) {
            return "Must Contain 1 Upper-case Character"
        }
        if (!passText.matches(".*[a-z].*".toRegex())) {
            return "Must Contain 1 Lower-case Character"
        }
        if (!passText.matches(".*[@#\$%^&+=].*".toRegex())) {
            return "Must Contain 1 Special Character ()"
        }
        return null
    }


}