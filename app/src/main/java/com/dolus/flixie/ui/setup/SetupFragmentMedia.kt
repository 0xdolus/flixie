package com.dolus.flixie.ui.setup

import android.view.View
import android.widget.AbsListView
import android.widget.ArrayAdapter
import androidx.core.content.edit
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.dolus.flixie.R
import com.dolus.flixie.TvType
import com.dolus.flixie.databinding.FragmentSetupMediaBinding
import com.dolus.flixie.mvvm.safe
import com.dolus.flixie.ui.BaseFragment
import com.dolus.flixie.utils.DataStoreHelper
import com.dolus.flixie.utils.UIHelper.fixSystemBarsPadding

class SetupFragmentMedia : BaseFragment<FragmentSetupMediaBinding>(
    BaseFragment.BindingCreator.Inflate(FragmentSetupMediaBinding::inflate)
) {

    override fun fixLayout(view: View) {
        fixSystemBarsPadding(view)
    }

    override fun onBindingCreated(binding: FragmentSetupMediaBinding) {
        safe {
            val ctx = context ?: return@safe
            val settingsManager = PreferenceManager.getDefaultSharedPreferences(ctx)
            val prefKey = getString(R.string.prefer_media_type_key)

            val arrayAdapter =
                ArrayAdapter<String>(ctx, R.layout.sort_bottom_single_choice)

            val sortedTypes = enumValues<TvType>().sorted()

            arrayAdapter.addAll(sortedTypes.map { it.name })
            binding.apply {
                listview1.let {
                    it.adapter = arrayAdapter
                    it.choiceMode = AbsListView.CHOICE_MODE_MULTIPLE

                    val stored = settingsManager.getStringSet(prefKey, null)
                    sortedTypes.forEachIndexed { index, type ->
                        // All media types except NSFW are checked by default
                        val isChecked = stored?.contains(type.ordinal.toString())
                            ?: (type != TvType.NSFW)
                        it.setItemChecked(index, isChecked)
                    }

                    val saveSelection = {
                        val values = sortedTypes
                            .filterIndexed { index, _ -> it.isItemChecked(index) }
                            .map { type -> type.ordinal.toString() }
                            .toSet()
                        settingsManager.edit { putStringSet(prefKey, values) }

                        // Regenerate set homepage
                        DataStoreHelper.currentHomePage = null
                    }

                    if (stored == null) saveSelection()

                    it.setOnItemClickListener { _, _, _, _ -> saveSelection() }
                }

                nextBtt.setOnClickListener {
                    findNavController().navigate(R.id.navigation_setup_media_to_navigation_setup_layout)
                }

                prevBtt.setOnClickListener {
                    findNavController().popBackStack()
                }
            }
        }
    }
}
