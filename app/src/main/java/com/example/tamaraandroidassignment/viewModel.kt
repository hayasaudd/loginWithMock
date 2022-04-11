package com.example.tamaraandroidassignment

import android.media.MediaRouter2.getInstance
import android.util.Log
import androidx.constraintlayout.utils.widget.MockView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tamaraandroidassignment.data.ResponseItem
import com.example.tamaraandroidassignment.data.myApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlin.reflect.jvm.internal.impl.descriptors.NotFoundClasses

enum class MyApiStatus { LOADING, ERROR, DONE }
class viewModel : ViewModel() {

    val _email = MutableLiveData<String>()
    val email1: MutableLiveData<String> get() = _email

    val _password = MutableLiveData<String>()
    val password1: MutableLiveData<String> get() = _password

    private val _userInfo = MutableLiveData<List<ResponseItem?>>()
    val userInfo: LiveData<List<ResponseItem?>> = _userInfo

    private val _status = MutableLiveData<MyApiStatus>()
    val status: LiveData<MyApiStatus> = _status


    var userIsValid: Boolean = false

    private val _errorMassege = MutableLiveData<String>()
    val errorMassege: LiveData<String> = _errorMassege

//    init {
//        checkUser()
//    }

    fun checkUser(email: String, userPassword: String) {

        viewModelScope.launch {

            Log.e("TAG", " userISValid whne start the corotine:  ${userIsValid}")
            _status.value = MyApiStatus.LOADING
            try {
                Log.e("TAG", " userISValid whne start try scope:  ${userIsValid}")

                val emailAPI =  myApi.retrofitServer.getUser(email)
                val passAPI = emailAPI.body()?.password

                if (emailAPI.isSuccessful && passAPI!!.equals(userPassword)) {
                    userIsValid = true
                } else {
                    userIsValid = false
                    _errorMassege.value = emailAPI.message().toString()
                }
            } catch (e: Exception) {
                Log.e("TAG", "getUserInfo:  error${e}")
                _status.value = MyApiStatus.ERROR
                _userInfo.value = listOf()
            }
        }
    }


}