package com.thanh_nguyen.google.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.thanh_nguyen.google.modle.LoginResult
import java.lang.Exception
import kotlin.math.acos


@SuppressLint("StaticFieldLeak")
class LoginGoogleManager {
    private var listener: GoogleCallbackManager? = null
    private lateinit var activity: Activity

    companion object{
        private lateinit var gso: GoogleSignInOptions
        private lateinit var mGoogleSignInClient: GoogleSignInClient
        private const val GG_SIGN_IN_REQUEST_CODE = 0x9999
        private var isInitialized = false

        fun init(context: Context){
            gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
            mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
            isInitialized = true
        }
    }

    fun register(activity: Activity){
        if (!isInitialized)
            throw Exception("LoginGoogleManager not init yet, call LoginGoogleManager.init(context) in your Application clas")
        this.activity = activity
    }

    fun register(activity: Activity, listener: GoogleCallbackManager? = null){
        if (!isInitialized)
            throw Exception("LoginGoogleManager not init yet, call LoginGoogleManager.init(context) in your Application clas")
        this.activity = activity
        this.listener = listener
    }

    private fun checkRegisteredOnActivity(){
        if (!this::activity.isInitialized)
            throw Exception("LoginFaceBookManager not register yet...")
    }

    fun login(){
        checkRegisteredOnActivity()
        logOut()
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, GG_SIGN_IN_REQUEST_CODE)
    }

    fun getLastSignedIn(): LoginResult?{
        checkRegisteredOnActivity()
        val account = GoogleSignIn.getLastSignedInAccount(activity)?: return null
        return LoginResult(
            idToken = account.idToken,
            email = account.email,
            avatar = account.photoUrl.toString(),
            displayName = account.displayName,
            personId = account.id
        )
    }

    fun logOut(){
        mGoogleSignInClient.signOut()
            .addOnCompleteListener {
                listener?.onLogOutSuccess()
            }
    }

    fun registerListener(listener: GoogleCallbackManager){
        this.listener = listener
    }

    fun onGoogleSignedInResult(requestCode: Int, resultCode: Int, result: Intent?){

        if (requestCode != GG_SIGN_IN_REQUEST_CODE) {
            listener?.onLoginFailed()
            return
        }
        Log.e("???","$requestCode - $resultCode")
        when(resultCode){
            Activity.RESULT_CANCELED -> listener?.onLoginCancelled()
            Activity.RESULT_OK -> listener?.onLoginSuccess(getDataAccountSignedIn(result))
            else -> listener?.onLoginFailed()
        }
    }

    private fun getDataAccountSignedIn(result: Intent?): LoginResult? {
        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result)
        try {
            val account = task.getResult(ApiException::class.java) ?: return null
            return LoginResult(
                displayName = account.displayName,
                avatar = account.photoUrl.toString(),
                email = account.email,
                idToken = account.idToken,
                personId = account.id
            )
        }
        catch (e: ApiException){
            Log.e("failed", "on getDataAccountSignedIn: ${e.message}")
        }
        return null
    }

    interface GoogleCallbackManager{
        fun onLoginFailed()
        fun onLoginCancelled()
        fun onLoginSuccess(result: LoginResult?)
        fun onLogOutSuccess()
    }
}