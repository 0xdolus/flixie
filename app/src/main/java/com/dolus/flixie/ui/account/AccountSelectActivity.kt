package com.dolus.flixie.ui.account

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.activity.viewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import com.dolus.flixie.CommonActivity
import com.dolus.flixie.CommonActivity.loadThemes
import com.dolus.flixie.CommonActivity.showToast
import com.dolus.flixie.MainActivity
import com.dolus.flixie.R
import com.dolus.flixie.databinding.ActivityAccountSelectBinding
import com.dolus.flixie.mvvm.observe
import com.dolus.flixie.ui.AutofitRecyclerView
import com.dolus.flixie.ui.account.AccountAdapter.Companion.VIEW_TYPE_EDIT_ACCOUNT
import com.dolus.flixie.ui.account.AccountAdapter.Companion.VIEW_TYPE_SELECT_ACCOUNT
import com.dolus.flixie.ui.settings.Globals.EMULATOR
import com.dolus.flixie.ui.settings.Globals.PHONE
import com.dolus.flixie.ui.settings.Globals.TV
import com.dolus.flixie.ui.settings.Globals.isLayout
import com.dolus.flixie.utils.BiometricAuthenticator
import com.dolus.flixie.utils.BiometricAuthenticator.BiometricCallback
import com.dolus.flixie.utils.BiometricAuthenticator.biometricPrompt
import com.dolus.flixie.utils.BiometricAuthenticator.deviceHasPasswordPinLock
import com.dolus.flixie.utils.BiometricAuthenticator.isAuthEnabled
import com.dolus.flixie.utils.BiometricAuthenticator.promptInfo
import com.dolus.flixie.utils.BiometricAuthenticator.startBiometricAuthentication
import com.dolus.flixie.utils.DataStoreHelper.accounts
import com.dolus.flixie.utils.DataStoreHelper.selectedKeyIndex
import com.dolus.flixie.utils.DataStoreHelper.setAccount
import com.dolus.flixie.utils.UIHelper.enableEdgeToEdgeCompat
import com.dolus.flixie.utils.UIHelper.fixSystemBarsPadding
import com.dolus.flixie.utils.UIHelper.openActivity
import com.dolus.flixie.utils.UIHelper.setNavigationBarColorCompat

class AccountSelectActivity : FragmentActivity(), BiometricCallback {

    companion object {
        var hasLoggedIn: Boolean = false
    }

    val accountViewModel: AccountViewModel by viewModels()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Are we editing and coming from MainActivity?
        val isEditingFromMainActivity = intent.getBooleanExtra(
            "isEditingFromMainActivity",
            false
        )

        val isFromMainActivity = intent.getBooleanExtra(
            "isFromMainActivity",
            false
        )

        // Sometimes we start this activity when we have already logged in
        // For example when using cloudstreamsearch://
        // In those cases we want to just go to the main activity instantly
        if (hasLoggedIn && !isEditingFromMainActivity && !isFromMainActivity) {
            navigateToMainActivity()
            return
        }

        loadThemes(this)

        enableEdgeToEdgeCompat()
        setNavigationBarColorCompat(R.attr.primaryBlackBackground)

        val settingsManager = PreferenceManager.getDefaultSharedPreferences(this)
        val skipStartup = settingsManager.getBoolean(
            getString(R.string.skip_startup_account_select_key), false
        ) || accounts.count() <= 1

        fun askBiometricAuth() {

            if (isLayout(PHONE) && isAuthEnabled(this)) {
                if (deviceHasPasswordPinLock(this)) {
                    startBiometricAuthentication(
                        this,
                        R.string.biometric_authentication_title,
                        false
                    )

                    promptInfo?.let { prompt ->
                        biometricPrompt?.authenticate(prompt)
                    }
                }
            }
        }

        observe(accountViewModel.isAllowedLogin) { isAllowedLogin ->
            if (isAllowedLogin) {
                // We are allowed to continue to MainActivity
                navigateToMainActivity()
            }
        }

        // Don't show account selection if there is only
        // one account that exists
        if (!isFromMainActivity && !isEditingFromMainActivity && skipStartup) {
            val currentAccount = accounts.firstOrNull { it.keyIndex == selectedKeyIndex }
            if (currentAccount?.lockPin != null) {
                CommonActivity.init(this)
                accountViewModel.handleAccountSelect(currentAccount, this, true)
            } else {
                if (accounts.count() > 1) {
                    showToast(
                        this, getString(
                            R.string.logged_account,
                            currentAccount?.name
                        )
                    )
                }

                navigateToMainActivity()
            }

            return
        }

        CommonActivity.init(this)

        val binding = ActivityAccountSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fixSystemBarsPadding(binding.root, padTop = false)

        val recyclerView: AutofitRecyclerView = binding.accountRecyclerView

        observe(accountViewModel.accounts) { liveAccounts ->
            val adapter = AccountAdapter(
                // Handle the selected account
                accountSelectCallback = {
                    accountViewModel.handleAccountSelect(it, this)
                },
                accountCreateCallback = { accountViewModel.handleAccountUpdate(it, this) },
                accountEditCallback = {
                    accountViewModel.handleAccountUpdate(it, this)
                    // We came from MainActivity, return there
                    // and switch to the edited account
                    if (isEditingFromMainActivity) {
                        setAccount(it)
                        navigateToMainActivity()
                    }
                },
                accountDeleteCallback = { accountViewModel.handleAccountDelete(it, this) }
            ).apply {
                submitList(liveAccounts)
            }

            recyclerView.adapter = adapter

            if (isLayout(TV or EMULATOR)) {
                binding.editAccountButton.setBackgroundResource(
                    R.drawable.player_button_tv_attr_no_bg
                )
            }

            observe(accountViewModel.selectedKeyIndex) { selectedKeyIndex ->
                // Scroll to current account (which is focused by default)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                layoutManager.scrollToPositionWithOffset(selectedKeyIndex, 0)
                if (isLayout(TV or EMULATOR)) {
                    recyclerView.post {
                        layoutManager.findViewByPosition(selectedKeyIndex)?.requestFocus()
                    }
                }
            }

            observe(accountViewModel.isEditing) { isEditing ->
                if (isEditing) {
                    binding.editAccountButton.setImageResource(R.drawable.ic_baseline_close_24)
                    binding.title.setText(R.string.manage_accounts)
                    adapter.viewType = VIEW_TYPE_EDIT_ACCOUNT
                } else {
                    binding.editAccountButton.setImageResource(R.drawable.ic_baseline_edit_24)
                    binding.title.setText(R.string.select_an_account)
                    adapter.viewType = VIEW_TYPE_SELECT_ACCOUNT
                }

                adapter.notifyDataSetChanged()
            }

            if (isEditingFromMainActivity) {
                accountViewModel.setIsEditing(true)
            }

            binding.editAccountButton.setOnClickListener {
                // We came from MainActivity, return there
                // and resume its state
                if (isEditingFromMainActivity) {
                    navigateToMainActivity()
                    return@setOnClickListener
                }

                accountViewModel.toggleIsEditing()
            }

            if (isLayout(TV or EMULATOR)) {
                recyclerView.spanCount = if (liveAccounts.count() + 1 <= 6) {
                    liveAccounts.count() + 1
                } else 6
            }
        }

        askBiometricAuth()
    }

    @SuppressLint("UnsafeIntentLaunch")
    private fun navigateToMainActivity() {
        hasLoggedIn = true
        // We want to propagate any intent we get here to MainActivity since this is just an intermediary
        openActivity(MainActivity::class.java, baseIntent = intent)
        finish() // Finish the account selection activity
    }

    override fun onAuthenticationSuccess() {
        Log.i(BiometricAuthenticator.TAG, "Authentication successful in AccountSelectActivity")
    }

    override fun onAuthenticationError() {
        finish()
    }
}
