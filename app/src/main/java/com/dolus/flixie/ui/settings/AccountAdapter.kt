package com.dolus.flixie.ui.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.dolus.flixie.R
import com.dolus.flixie.databinding.AccountSingleBinding
import com.dolus.flixie.syncproviders.AuthData
import com.dolus.flixie.ui.BaseDiffCallback
import com.dolus.flixie.ui.NoStateAdapter
import com.dolus.flixie.ui.ViewHolderState
import com.dolus.flixie.utils.ImageLoader.loadImage

class AccountClickCallback(val action: Int, val view: View, val card: AuthData)

class AccountAdapter(
    private val clickCallback: (AccountClickCallback) -> Unit
) :
    NoStateAdapter<AuthData>(
        diffCallback = BaseDiffCallback(itemSame = { a, b ->
            a.user.id == b.user.id
        })
    ) {

    override fun onCreateContent(parent: ViewGroup): ViewHolderState<Any> {
        return ViewHolderState(
            AccountSingleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onClearView(holder: ViewHolderState<Any>) {
        val binding = holder.view as? AccountSingleBinding ?: return
        clearImage(binding.accountProfilePicture)
    }

    override fun onBindContent(holder: ViewHolderState<Any>, item: AuthData, position: Int) {
        val binding = holder.view as? AccountSingleBinding ?: return
        binding.apply {
            accountName.text = item.user.name
                ?: "${binding.accountName.context.getString(R.string.account)} ${position + 1}"
            accountProfilePicture.isVisible = true
            accountProfilePicture.loadImage(
                item.user.profilePicture,
                headers = item.user.profilePictureHeaders
            )

            root.setOnClickListener {
                clickCallback.invoke(AccountClickCallback(0, root, item))
            }
        }
    }
}
