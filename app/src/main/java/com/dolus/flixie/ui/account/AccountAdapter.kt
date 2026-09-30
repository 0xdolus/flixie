package com.dolus.flixie.ui.account

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.dolus.flixie.R
import com.dolus.flixie.databinding.AccountListItemAddBinding
import com.dolus.flixie.databinding.AccountListItemBinding
import com.dolus.flixie.databinding.AccountListItemEditBinding
import com.dolus.flixie.ui.NoStateAdapter
import com.dolus.flixie.ui.ViewHolderState
import com.dolus.flixie.ui.account.AccountHelper.showAccountEditDialog
import com.dolus.flixie.ui.settings.Globals.EMULATOR
import com.dolus.flixie.ui.settings.Globals.TV
import com.dolus.flixie.ui.settings.Globals.isLayout
import com.dolus.flixie.utils.DataStoreHelper
import com.dolus.flixie.utils.ImageLoader.loadImage
import android.util.TypedValue
import android.view.View

class AccountAdapter(
    private val accountSelectCallback: (DataStoreHelper.Account) -> Unit,
    private val accountCreateCallback: (DataStoreHelper.Account) -> Unit,
    private val accountEditCallback: (DataStoreHelper.Account) -> Unit,
    private val accountDeleteCallback: (DataStoreHelper.Account) -> Unit
) : NoStateAdapter<DataStoreHelper.Account>() {

    companion object {
        const val VIEW_TYPE_SELECT_ACCOUNT = 0
        const val VIEW_TYPE_EDIT_ACCOUNT = 2

        fun View.setRippleForeground() {
            val outValue = TypedValue()
            context.theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, outValue, true)
            foreground = context.getDrawable(outValue.resourceId)
        }
    }


    override val footers: Int = 1
    var viewType = VIEW_TYPE_SELECT_ACCOUNT

    override fun customContentViewType(item: DataStoreHelper.Account): Int {
        return viewType
    }

    override fun onBindContent(
        holder: ViewHolderState<Any>,
        item: DataStoreHelper.Account,
        position: Int
    ) {
        when (val binding = holder.view) {
            is AccountListItemBinding -> binding.apply {
                val isTv = isLayout(TV or EMULATOR) || !root.isInTouchMode
                val isLastUsedAccount = item.keyIndex == DataStoreHelper.selectedKeyIndex

                accountName.text = item.name
                accountImage.loadImage(item.image)
                lockIcon.isVisible = item.lockPin != null
                root.isSelected = isLastUsedAccount
                cardView.isSelected = isLastUsedAccount

                if (!isTv) cardView.setRippleForeground()

                if (!isTv) {
                    root.setOnLongClickListener {
                        showAccountEditDialog(
                            context = root.context,
                            account = item,
                            isNewAccount = false,
                            accountEditCallback = { account -> accountEditCallback.invoke(account) },
                            accountDeleteCallback = { account -> accountDeleteCallback.invoke(account) }
                        )
                        true
                    }
                }

                root.setOnClickListener {
                    accountSelectCallback.invoke(item)
                }
            }

            is AccountListItemEditBinding -> binding.apply {
                val isTv = isLayout(TV or EMULATOR) || !root.isInTouchMode
                val isLastUsedAccount = item.keyIndex == DataStoreHelper.selectedKeyIndex

                accountName.text = item.name
                accountImage.loadImage(item.image)
                lockIcon.isVisible = item.lockPin != null
                root.isSelected = isLastUsedAccount
                cardView.isSelected = isLastUsedAccount

                if (!isTv) cardView.setRippleForeground()

                root.setOnClickListener {
                    showAccountEditDialog(
                        context = root.context,
                        account = item,
                        isNewAccount = false,
                        accountEditCallback = { account -> accountEditCallback.invoke(account) },
                        accountDeleteCallback = { account -> accountDeleteCallback.invoke(account) }
                    )
                }
            }
        }
    }

    override fun onBindFooter(holder: ViewHolderState<Any>) {
        val binding = holder.view as? AccountListItemAddBinding ?: return
        binding.apply {
            val isTv = isLayout(TV or EMULATOR) || !root.isInTouchMode
            if (!isTv) cardView.setRippleForeground()

            root.setOnClickListener {
                val accounts = this@AccountAdapter.immutableCurrentList

                val remainingImages =
                    DataStoreHelper.profileImages.toSet() - accounts.filter { it.customImage == null }
                        .mapNotNull { DataStoreHelper.profileImages.getOrNull(it.defaultImageIndex) }
                        .toSet()

                val image =
                    DataStoreHelper.profileImages.indexOf(
                        remainingImages.randomOrNull()
                            ?: DataStoreHelper.profileImages.random()
                    )
                val keyIndex = (accounts.maxOfOrNull { it.keyIndex } ?: 0) + 1

                val accountName = root.context.getString(R.string.account)

                showAccountEditDialog(
                    root.context,
                    DataStoreHelper.Account(
                        keyIndex = keyIndex,
                        name = "$accountName $keyIndex",
                        customImage = null,
                        defaultImageIndex = image
                    ),
                    isNewAccount = true,
                    accountEditCallback = { account -> accountCreateCallback.invoke(account) },
                    accountDeleteCallback = {}
                )
            }
        }
    }

    override fun onCreateFooter(parent: ViewGroup): ViewHolderState<Any> {
        return ViewHolderState(
            AccountListItemAddBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onCreateContent(parent: ViewGroup): ViewHolderState<Any> {
        return ViewHolderState(
            when (viewType) {
                VIEW_TYPE_SELECT_ACCOUNT -> {
                    AccountListItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                }

                VIEW_TYPE_EDIT_ACCOUNT -> {
                    AccountListItemEditBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                }

                else -> throw IllegalArgumentException("Invalid view type")
            }
        )
    }
}