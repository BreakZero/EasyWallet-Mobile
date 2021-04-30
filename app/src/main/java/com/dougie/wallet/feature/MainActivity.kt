package com.dougie.wallet.feature

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.dougie.framework.base.BaseActivity
import com.dougie.wallet.R
import com.dougie.wallet.ShowWcActionViewDirections
import com.dougie.wallet.ext.setupWithNavController
import com.dougie.wallet.feature.wallectconnet.WCBroadcastReceiver
import com.dougie.wallet.feature.wallectconnet.WalletConnectService
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import timber.log.Timber

@KoinApiExtension
class MainActivity : BaseActivity(R.layout.activity_main) {

    private var currentNavController: LiveData<NavController>? = null

    private val wcBroadcastReceiver = WCBroadcastReceiver {
        Timber.d("====== WC DATA: $it")
        val action = ShowWcActionViewDirections.actionShowWalletConnectAction(it)
        currentNavController?.value?.navigate(action)
    }

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        window.statusBarColor = getColor(R.color.purple_700)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter().apply {
            addAction(WCBroadcastReceiver.WC_ACTION_FILTER_ACTION)
        }
        registerReceiver(wcBroadcastReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(wcBroadcastReceiver)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        Intent(this, WalletConnectService::class.java).also {
            stopService(it)
        }
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNav)

        val navGraphIds = listOf(
            R.navigation.nav_defi,
            R.navigation.nav_asset,
            R.navigation.nav_nft,
            R.navigation.nav_settings
        )
        val mainScreenIds = listOf(
            R.id.fragmentDefiIndex,
            R.id.homeFragment,
            R.id.fragmentNFTIndex,
            R.id.settingsFragment
        )

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.hostContainer,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this) {
            it.addOnDestinationChangedListener { _, destination, _ ->
                if (mainScreenIds.contains(destination.id)) {
                    bottomNavigationView.visibility = View.VISIBLE
                } else {
                    bottomNavigationView.visibility = View.GONE
                }
            }
        }
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
}
