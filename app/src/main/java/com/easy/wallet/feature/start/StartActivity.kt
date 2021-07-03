package com.easy.wallet.feature.start

import android.os.Bundle
import android.os.PersistableBundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.easy.framework.base.BaseActivity
import com.easy.wallet.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates

class StartActivity : BaseActivity(R.layout.activity_start) {
    private val viewModel by viewModel<StartViewModel>()

    private var navController: NavController by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.startHostContainer) as NavHostFragment
        navController = navHostFragment.navController
        navController.setGraph(R.navigation.nav_start)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}
