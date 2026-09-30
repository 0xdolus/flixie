package com.dolus.flixie.ui.download

import android.os.Bundle
import android.text.format.Formatter.formatShortFileSize
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.dolus.flixie.R
import com.dolus.flixie.databinding.FragmentChildDownloadsBinding
import com.dolus.flixie.mvvm.Resource
import com.dolus.flixie.mvvm.observe
import com.dolus.flixie.mvvm.observeNullable
import com.dolus.flixie.ui.BaseFragment
import com.dolus.flixie.ui.download.DownloadButtonSetup.handleDownloadClick
import com.dolus.flixie.ui.result.FOCUS_SELF
import com.dolus.flixie.ui.result.setLinearListLayout
import com.dolus.flixie.ui.settings.Globals.EMULATOR
import com.dolus.flixie.ui.settings.Globals.PHONE
import com.dolus.flixie.ui.settings.Globals.TV
import com.dolus.flixie.ui.settings.Globals.isLandscape
import com.dolus.flixie.ui.settings.Globals.isLayout
import com.dolus.flixie.utils.BackPressedCallbackHelper.attachBackPressedCallback
import com.dolus.flixie.utils.BackPressedCallbackHelper.detachBackPressedCallback
import com.dolus.flixie.utils.UIHelper.fixSystemBarsPadding
import com.dolus.flixie.utils.UIHelper.setAppBarNoScrollFlagsOnTV

class DownloadChildFragment : BaseFragment<FragmentChildDownloadsBinding>(
    BaseFragment.BindingCreator.Inflate(FragmentChildDownloadsBinding::inflate)
) {

    private val downloadViewModel: DownloadViewModel by activityViewModels()

    companion object {
        fun newInstance(headerName: String, folder: String): Bundle {
            return Bundle().apply {
                putString("folder", folder)
                putString("name", headerName)
            }
        }
    }

    override fun onDestroyView() {
        activity?.detachBackPressedCallback("Downloads")
        downloadViewModel.clearChildren()
        super.onDestroyView()
    }

    override fun fixLayout(view: View) {
        fixSystemBarsPadding(
            view,
            padBottom = isLandscape(),
            padLeft = isLayout(TV or EMULATOR)
        )
    }

    override fun onBindingCreated(binding: FragmentChildDownloadsBinding) {
        val folder = arguments?.getString("folder")
        val name = arguments?.getString("name")
        if (folder == null) {
            dispatchBackPressed()
            return
        }

        context?.let { downloadViewModel.updateChildList(it, folder) }

        binding.downloadChildToolbar.apply {
            title = name
            if (isLayout(PHONE or EMULATOR)) {
                setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
                setNavigationOnClickListener {
                    dispatchBackPressed()
                }
            }
            setAppBarNoScrollFlagsOnTV()
        }

        binding.downloadDeleteAppbar.setAppBarNoScrollFlagsOnTV()

        observe(downloadViewModel.childCards) { cards ->
            when (cards) {
                is Resource.Success -> {
                    if (cards.value.isEmpty()) {
                        dispatchBackPressed()
                    }
                    (binding.downloadChildList.adapter as? DownloadAdapter)?.submitList(cards.value)
                }

                else -> {
                    (binding.downloadChildList.adapter as? DownloadAdapter)?.submitList(null)
                }
            }
        }

        observe(downloadViewModel.selectedBytes) {
            updateDeleteButton(downloadViewModel.selectedItemIds.value?.count() ?: 0, it)
        }


        binding.apply {
            btnDelete.setOnClickListener { view ->
                downloadViewModel.handleMultiDelete(view.context ?: return@setOnClickListener)
            }

            btnCancel.setOnClickListener {
                downloadViewModel.cancelSelection()
            }

            btnToggleAll.setOnClickListener {
                val allSelected = downloadViewModel.isAllChildrenSelected()
                if (allSelected) {
                    downloadViewModel.clearSelectedItems()
                } else {
                    downloadViewModel.selectAllChildren()
                }
            }
        }

        observeNullable(downloadViewModel.selectedItemIds) { selection ->
            val isMultiDeleteState = selection != null
            val adapter = binding.downloadChildList.adapter as? DownloadAdapter
            adapter?.setIsMultiDeleteState(isMultiDeleteState)
            binding.downloadDeleteAppbar.isVisible = isMultiDeleteState
            binding.downloadChildToolbar.isGone = isMultiDeleteState

            if (selection == null) {
                activity?.detachBackPressedCallback("Downloads")
                return@observeNullable
            }
            activity?.attachBackPressedCallback("Downloads") {
                downloadViewModel.cancelSelection()
            }

            updateDeleteButton(selection.count(), downloadViewModel.selectedBytes.value ?: 0L)

            binding.btnDelete.isVisible = selection.isNotEmpty()
            binding.selectItemsText.isVisible = selection.isEmpty()

            val allSelected = downloadViewModel.isAllChildrenSelected()
            if (allSelected) {
                binding.btnToggleAll.setText(R.string.deselect_all)
            } else binding.btnToggleAll.setText(R.string.select_all)
        }

        val adapter = DownloadAdapter(
            {},
            { click ->
                if (click.action == DOWNLOAD_ACTION_DELETE_FILE) {
                    context?.let { ctx ->
                        downloadViewModel.handleSingleDelete(ctx, click.data.id)
                    }
                } else handleDownloadClick(click)
            },
            { itemId, isChecked ->
                if (isChecked) {
                    downloadViewModel.addSelected(itemId)
                } else downloadViewModel.removeSelected(itemId)
            }
        )

        binding.downloadChildList.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            this.adapter = adapter
            setLinearListLayout(
                isHorizontal = false,
                nextRight = FOCUS_SELF,
                nextDown = FOCUS_SELF,
            )
        }
    }

    private fun updateDeleteButton(count: Int, selectedBytes: Long) {
        val formattedSize = formatShortFileSize(context, selectedBytes)
        binding?.btnDelete?.text =
            getString(R.string.delete_format).format(count, formattedSize)
    }
}