package com.example.tamaraandroidassignment


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.tamaraandroidassignment.databinding.ActivityLoginBinding
import java.util.*


lateinit var binding: ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val myViewModel: MyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //bind logIn layout
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myViewModel.emailFocusListener()
        myViewModel.passwordFocusListener()
        observers()

        //bind log in button
        binding.button.setOnClickListener {
            Log.e("TAG", " userISValid in click button   ${myViewModel.userIsValid}")
            clickButton()

        }

    }

    private fun observers() {
        myViewModel.userIsValid.observe(this) {
            if (it) {
                Toast.makeText(this@LoginActivity, "navigate to next page ", Toast.LENGTH_SHORT)
                    .show()
                startActivity(Intent(this@LoginActivity, WelcomePageActivity::class.java))
            }
        }

        myViewModel.errorMassage.observe(this) {
            Toast.makeText(
                this@LoginActivity,
                it,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    //when the user press the signIn button
    private fun clickButton(): Boolean {
        return if (myViewModel.emailValid) {
            myViewModel.checkUser(
                binding.editTextEmail.text.toString().lowercase(Locale.getDefault()),
                binding.editTextPassword.text.toString()
            )
            true
        } else {
            Toast.makeText(this, "pleas insure your email or password", Toast.LENGTH_SHORT).show()
            false
        }
    }

}