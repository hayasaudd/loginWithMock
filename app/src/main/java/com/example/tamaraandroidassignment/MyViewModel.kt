package com.example.tamaraandroidassignment


import android.util.Log
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

}