package com.example.tamaraandroidassignment


import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tamaraandroidassignment.data.ResponseItem
import com.example.tamaraandroidassignment.data.MyApi
import kotlinx.coroutines.launch


enum class MyApiStatus { LOADING, ERROR }
class MyViewModel : ViewModel() {

    private val _userInfo = MutableLiveData<List<ResponseItem?>>()
    private val _status = MutableLiveData<MyApiStatus>()

    private val _userIsValid = MutableLiveData<Boolean>()
    val userIsValid: LiveData<Boolean> get() = _userIsValid

    private val _errorMassage = MutableLiveData<String>()
    val errorMassage: LiveData<String> get() = _errorMassage

    private var passwordValid = false
    var emailValid = false


    fun checkUser(email: String, userPassword: String) {

        viewModelScope.launch {
            _status.value = MyApiStatus.LOADING
            try {
                Log.e("TAG", " userISValid when start try scope:  $userIsValid")
                val emailAPI = MyApi.RETROFIT_SERVER.getUser(email)
                val passAPI = emailAPI.body()?.password

                if (emailAPI.isSuccessful && passAPI!! == userPassword) {
                    _userIsValid.value = true
                } else {
                    _userIsValid.value = false
                    _errorMassage.value = "Password or email is not correct"
                }
            } catch (e: Exception) {
                _status.value = MyApiStatus.ERROR
                _userInfo.value = listOf()
            }
        }
    }

    fun emailFocusListener() {

        binding.editTextEmail.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                val checker = validEmail()
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

    private fun validEmail(): String? {
        val emailText = binding.editTextEmail.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return "Invalid Email Address"
        }
        return null
    }

    fun passwordFocusListener() {
        binding.editTextPassword.setOnFocusChangeListener { _, focused ->
            if (focused) {
                val checkerPass = validPassword()
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

    private fun validPassword(): String? {
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