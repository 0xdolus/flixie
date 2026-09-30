package com.dolus.flixie.ui.setup

import android.view.View
import android.widget.AbsListView
import android.widget.ArrayAdapter
import androidx.core.content.edit
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.dolus.flixie.CloudStreamApp.Companion.setKey
import com.dolus.flixie.R
import com.dolus.flixie.databinding.FragmentSetupLayoutBinding
import com.dolus.flixie.mvvm.safe
import com.dolus.flixie.ui.BaseFragment
import com.dolus.flixie.utils.UIHelper.fixSystemBarsPadding

class SetupFragmentLayout : BaseFragment<FragmentSetupLayoutBinding>(
    BaseFragment.BindingCreator.Inflate(FragmentSetupLayoutBinding::inflate)
) {

    override fun fixLayout(view: View) {
        fixSystemBarsPadding(view)
    }

    override fun onBindingCreated(binding: FragmentSetupLayoutBinding) {
        safe {
            val ctx = context ?: return@safe

            val settingsManager = PreferenceManager.getDefaultSharedPreferences(ctx)

            val prefNames = resources.getStringArray(R.array.app_layout)
            val prefValues = resources.getIntArray(R.array.app_layout_values)

            val currentLayout =
                settingsManager.getInt(getString(R.string.app_layout_key), -1)

            val arrayAdapter =
                ArrayAdapter<String>(ctx, R.layout.sort_bottom_single_choice)

            arrayAdapter.addAll(prefNames.toList())
            binding.apply {
                listview1.adapter = arrayAdapter
                listview1.choiceMode = AbsListView.CHOICE_MODE_SINGLE
                listview1.setItemChecked(
                    prefValues.indexOf(currentLayout), true
                )

                listview1.setOnItemClickListener { _, _, position, _ ->
                    settingsManager.edit {
                        putInt(getString(R.string.app_layout_key), prefValues[position])
                    }
                    activity?.recreate()
                }

                nextBtt.setOnClickListener {
                    setKey(HAS_DONE_SETUP_KEY, true)
                    findNavController().navigate(R.id.navigation_home)
                }

                prevBtt.setOnClickListener {
                    findNavController().popBackStack()
                }
            }
        }
    }
}
