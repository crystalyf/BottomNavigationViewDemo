package com.change.towerfarm.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.change.towerfarm.R
import com.change.towerfarm.base.BaseActivity
import com.change.towerfarm.databinding.ActivityStartBinding
import com.change.towerfarm.extensions.getViewModelFactory
import com.change.towerfarm.viewmodels.StartViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

/**
 * 暂时no use
 */
class StartActivity : BaseActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    private var dataBinding: ActivityStartBinding? = null
    private val viewModel by viewModels<StartViewModel> { getViewModelFactory() }

    override var layout: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_start)
        initNavController()
        dataBinding?.lifecycleOwner = this
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == StartViewModel.GOOGLE_AUTH_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                viewModel.firebaseAuthWithGoogle(this, account.idToken!!, this, supportFragmentManager)
            } catch (e: ApiException) {
                viewModel.solveFirebaseException(e)
                e.printStackTrace()
            }
        }else if (viewModel.callbackManager != null) {
            // Pass the activity result back to the Facebook SDK
            viewModel.callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun initNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController
        appBarConfiguration =
            AppBarConfiguration.Builder(R.navigation.navigation_start).build()
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.registerFragment || navController.currentDestination?.id == R.id.forgetPwdFragment) {
            super.onBackPressed()
        } else if (navController.currentDestination?.id == R.id.loginFragment) {
            finish()
        }
    }
}