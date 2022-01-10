package com.change.towerfarm.viewmodels

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.change.towerfarm.BuildConfig
import com.change.towerfarm.base.BaseViewModel
import com.change.towerfarm.utils.Event
import com.change.towerfarm.utils.Exclusive
import com.change.towerfarm.utils.SharedPrefsManager
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class StartViewModel(
    private val sharePref: SharedPrefsManager
) : BaseViewModel() {

    companion object {
        const val GOOGLE_AUTH_REQUEST_CODE = 0
        const val FACEBOOK_AUTH = ""
    }

    //account
    var loginAccount = MutableLiveData<String>()

    //pass
    var loginPassword = MutableLiveData<String>()

    private val _loginButtonClick = MutableLiveData<Event<Unit>>()
    val loginButtonClick: LiveData<Event<Unit>> = _loginButtonClick

    private val _registerButtonClick = MutableLiveData<Event<Unit>>()
    val registerButtonClick: LiveData<Event<Unit>> = _registerButtonClick

    // 「Googleで続行」　クリック
    private val _googleSignInClick = MutableLiveData<Event<Unit>>()
    val googleSignInClick: LiveData<Event<Unit>> = _googleSignInClick

    private val _firebaseException = MutableLiveData<Event<Exception>>()
    val firebaseException: LiveData<Event<Exception>> = _firebaseException

    var auth: FirebaseAuth = Firebase.auth
    // Initialize Facebook Login button
    var callbackManager = CallbackManager.Factory.create()


    /**
     * Firebase Google Auth
     */
    fun doGoogleAuthentication(activity: FragmentActivity?) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_FIREBASE_DEFAULT_WEB_CLIENT_ID)
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
        val signInIntent = googleSignInClient.signInIntent
        activity?.startActivityForResult(signInIntent, GOOGLE_AUTH_REQUEST_CODE)
    }

    fun firebaseAuthWithGoogle(
        activity: Activity,
        accountId: String,
        context: Context,
        fragmentManager: FragmentManager
    ) {
        startLoading()
        val credential = GoogleAuthProvider.getCredential(accountId, null)
        auth.signInWithCredential(credential).addOnCompleteListener(activity) {
            stopLoading()
            if (it.isSuccessful) {
                if (auth.currentUser != null) {
                    val task = auth.currentUser?.getIdToken(false)
                    if (task?.isSuccessful!!) {
                        val token = task.result?.token
                        //TODO: send auth login api
                    }
                }
            } else {
                solveFirebaseException(it.exception)
            }
        }
    }

    fun handleFacebookAccessToken(activity: FragmentActivity?, token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential).addOnCompleteListener(activity!!) { it ->
            if (it.isSuccessful) {
                if (auth.currentUser != null) {
                    val task = auth.currentUser?.getIdToken(false)
                    if (task?.isSuccessful!!) {
                        val token = task.result?.token
                        Log.v("logTry:", token.toString())
                        //TODO: send auth login api
                    }
                }
            } else {
                solveFirebaseException(it.exception)
            }
        }
    }

    /**
     * Firebaseスロexception
     */
    fun solveFirebaseException(e: Exception?) {
        if (e != null) {
            _firebaseException.postValue(Event(e))
        }
    }


    fun loginButtonClick() {
        Exclusive.normal().tap {
            _loginButtonClick.postValue(Event(Unit))
        }
    }

    fun registerButtonClick() {
        Exclusive.normal().tap {
            _registerButtonClick.postValue(Event(Unit))
        }
    }

    /**
     * Googleで続行クリック
     */
    fun googleSignInClick() {
        Exclusive.normal().tap {
            _googleSignInClick.postValue(Event(Unit))
        }
    }

}