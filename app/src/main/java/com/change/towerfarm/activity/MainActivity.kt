package com.change.towerfarm.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.change.towerfarm.R
import com.change.towerfarm.base.BaseActivity
import com.change.towerfarm.databinding.ActivityMainBinding
import com.change.towerfarm.extensions.getViewModelFactory
import com.change.towerfarm.viewmodels.MainViewModel
import com.change.towerfarm.extensions.setupWithNavController

class MainActivity : BaseActivity() {

    /**
     * オブジェクトのNavigation挙動を管理
     */
    private var currentNavController: LiveData<NavController>? = null
    private val viewModel by viewModels<MainViewModel> { getViewModelFactory() }
    private var dataBinding: ActivityMainBinding? = null

    override var layout: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(dataBinding?.appBarFrame?.toolbar)
        setupBottomNavigationBar()
        viewModel.setSensorListData()
        dataBinding?.lifecycleOwner = this
    }

    override fun onSupportNavigateUp(): Boolean {
        val navigated = currentNavController?.value?.navigateUp() ?: false
        if (!navigated) {
            super.onBackPressed()
        }
        return navigated || super.onSupportNavigateUp()
    }

    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(
            R.navigation.navigation_home,
            R.navigation.navigation_record,
            R.navigation.navigation_notification,
            R.navigation.navigation_consult,
            R.navigation.navigation_setting
        )
        val controller = dataBinding?.navView?.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment,
            intent = intent
        )
        controller?.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
            navController.addOnDestinationChangedListener { nav, destination, args ->
                when (destination.id) {
                    //TODO:
                    R.id.homeFragment -> {
                        dataBinding?.appBarFrame?.toolbar?.setNavigationIcon(R.drawable.ic_drawer)
                        dataBinding?.appBarFrame?.toolbar?.setNavigationOnClickListener {
                            viewModel.navigationIconClick()
                        }
                    }
                    else -> {
                        dataBinding?.appBarFrame?.toolbar?.navigationIcon = null
                    }

                }
            }
        })
        currentNavController = controller
    }

    fun getToolbar(): Toolbar? {
        return dataBinding?.appBarFrame?.toolbar
    }
}