package com.dolus.flixie.utils

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.dolus.flixie.R
import com.dolus.flixie.ui.settings.Globals.EMULATOR
import com.dolus.flixie.ui.settings.Globals.TV
import com.dolus.flixie.ui.settings.Globals.isLandscape
import com.dolus.flixie.ui.settings.Globals.isLayout
import com.dolus.flixie.utils.UIHelper.fixSystemBarsPadding
import com.dolus.flixie4.compose.Screen
import com.dolus.flixie4.compose.createComposeView

abstract class BaseComposeFragment : Fragment(), Screen {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = createComposeView(inflater, container, savedInstanceState)

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        this.view?.let { view ->
            fixSystemBarsPadding(
                view,
                padLeft = isLayout(TV or EMULATOR),
                padBottom = isLandscape()
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fixSystemBarsPadding(
            view,
            padLeft = isLayout(TV or EMULATOR),
            padBottom = isLandscape()
        )
    }
}

abstract class BaseDialogComposeFragment : DialogFragment(), Screen {
    var systemBarsAddPadding = isLayout(TV or EMULATOR)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = createComposeView(inflater, container, savedInstanceState)

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        dialog?.window?.decorView?.let { view ->
            fixSystemBarsPadding(
                view,
                padLeft = systemBarsAddPadding,
                padBottom = systemBarsAddPadding || isLandscape()
            )
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setWindowAnimations(R.style.DialogFullscreenPlayer)
    }

    override fun getTheme(): Int {
        return R.style.DialogFullscreenPlayer
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.decorView?.let { view ->
            fixSystemBarsPadding(
                view,
                padLeft = systemBarsAddPadding,
                padBottom = systemBarsAddPadding || isLandscape()
            )
        }
    }
}