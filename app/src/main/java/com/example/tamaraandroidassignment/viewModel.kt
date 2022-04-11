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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
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


    val _userIsValid = MutableLiveData<Boolean>()
    val userIsValid: LiveData<Boolean> get() = _userIsValid

    private val _errorMassege = MutableLiveData<String>()
    val errorMassege: LiveData<String> get() = _errorMassege

//    init {
//        checkUser()
//    }

    fun checkUser(email: String, userPassword: String) {


        viewModelScope.launch {
            _status.value = MyApiStatus.LOADING
            try {
                Log.e("TAG", " userISValid whne start try scope:  ${userIsValid}")
                val emailAPI = myApi.retrofitServer.getUser(email)
                val passAPI = emailAPI.body()?.password

                if (emailAPI.isSuccessful && passAPI!!.equals(userPassword)) {
                    _userIsValid.value = true
                } else {
                    _userIsValid.value = false
                    _errorMassege.value = "Password or email is not corecct"
                }
            } catch (e: Exception) {
                _status.value = MyApiStatus.ERROR
                _userInfo.value = listOf()
            }
        }
    }

}