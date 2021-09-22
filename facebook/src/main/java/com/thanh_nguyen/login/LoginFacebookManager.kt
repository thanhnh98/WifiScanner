package com.thanh_nguyen.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.thanh_nguyen.model.FacebookLoginResult
import java.lang.Exception

@SuppressLint("StaticFieldLeak")
class LoginFacebookManager() {
    private val loginManager = LoginManager.getInstance()
    private val callbackManager = CallbackManager.Factory.create()
    private lateinit var activity: Activity

    companion object{
        private var isInitialized = false
        fun init(context: Context){
            FacebookSdk.sdkInitialize(context)
            AppEventsLogger.activateApp(context)
            isInitialized = true
        }
    }

    fun register(activity: Activity){
        if (!isInitialized)
            throw Exception("LoginFaceBookManager not init yet, call LoginFaceBookManager.init(context) in your Application clas")
        this.activity = activity
    }

    fun login(){
        checkRegisteredOnActivity()
        logout()
        loginManager.logInWithReadPermissions(
            activity,
            listOf(
                "public_profile"
            )
        )
    }

    private fun checkRegisteredOnActivity(){
        if (!this::activity.isInitialized)
            throw Exception("LoginFaceBookManager not register yet...")
    }

    fun logout(){
        loginManager.logOut()
    }

    fun loginWithCallback(callback: (FacebookLoginResult) -> Unit){
        loginManager.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult>{
                override fun onSuccess(result: LoginResult?) {
                    val request = GraphRequest.newMeRequest(
                        result?.accessToken
                    ){ obj, res ->
                        val email = obj.getString("email")
                        val name = obj.getString("name")
                        val id = obj.getString("id")
                        val picture = obj.getString("picture")
                        val avatarUrl = JsonParser.parseString(picture).asJsonObject.get("data").asJsonObject.get("url")
                        callback.invoke(FacebookLoginResult(
                            result?.accessToken?.token,
                            email = email,
                            name = name,
                            id = id,
                            avatar = avatarUrl.toString()
                        ))
                    }

                    val parameters = Bundle()
                    parameters.putString("fields", "name,email,id,picture.type(large)")
                    request.parameters = parameters
                    request.executeAsync()
                }

                override fun onCancel() {
                    Log.e("login facebook", "onCancel")
                }

                override fun onError(error: FacebookException?) {
                    Log.e("login facebook", "onError")

                }
            }
        )
        login()
    }

    fun registerCallbackManager(requestCode: Int, resultCode: Int, data: Intent?): Boolean{
        return callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}