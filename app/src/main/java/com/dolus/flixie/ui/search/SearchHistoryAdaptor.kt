package com.dolus.flixie.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import com.fasterxml.jackson.annotation.JsonProperty
import com.dolus.flixie.TvType
import com.dolus.flixie.databinding.SearchHistoryFooterBinding
import com.dolus.flixie.databinding.SearchHistoryItemBinding
import com.dolus.flixie.ui.BaseDiffCallback
import com.dolus.flixie.ui.NoStateAdapter
import com.dolus.flixie.ui.ViewHolderState
import com.dolus.flixie.ui.settings.Globals.EMULATOR
import com.dolus.flixie.ui.settings.Globals.TV
import com.dolus.flixie.ui.settings.Globals.isLayout
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchHistoryItem(
    @JsonProperty("searchedAt") @SerialName("searchedAt") val searchedAt: Long,
    @JsonProperty("searchText") @SerialName("searchText") val searchText: String,
    @JsonProperty("type") @SerialName("type") val type: List<TvType>,
    @JsonProperty("key") @SerialName("key") val key: String,
)

data class SearchHistoryCallback(
    val item: SearchHistoryItem?,
    val clickAction: Int,
)

const val SEARCH_HISTORY_OPEN = 0
const val SEARCH_HISTORY_REMOVE = 1
const val SEARCH_HISTORY_CLEAR = 2

class SearchHistoryAdaptor(
    private val clickCallback: (SearchHistoryCallback) -> Unit,
) : NoStateAdapter<SearchHistoryItem>(diffCallback = BaseDiffCallback(itemSame = { a,b ->
    a.searchedAt == b.searchedAt && a.searchText == b.searchText
})) {
    
    // Add footer for all layouts
    override val footers = 1
    
    override fun submitList(list: Collection<SearchHistoryItem>?, commitCallback: Runnable?) {
        super.submitList(list, commitCallback)
        // Notify footer to rebind when list changes to update visibility
        if (footers > 0) {
            notifyItemChanged(itemCount - 1)
        }
    }
    
    override fun onCreateContent(parent: ViewGroup): ViewHolderState<Any> {
        return ViewHolderState(
            SearchHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )
    }

    override fun onBindContent(
        holder: ViewHolderState<Any>,
        item: SearchHistoryItem,
        position: Int
    ) {
        val binding = holder.view as? SearchHistoryItemBinding ?: return
        binding.apply {
            homeHistoryTitle.text = item.searchText

            homeHistoryRemove.setOnClickListener {
                clickCallback.invoke(SearchHistoryCallback(item, SEARCH_HISTORY_REMOVE))
            }
            homeHistoryTab.setOnClickListener {
                clickCallback.invoke(SearchHistoryCallback(item, SEARCH_HISTORY_OPEN))
            }
        }
    }
    
    override fun onCreateFooter(parent: ViewGroup): ViewHolderState<Any> {
        return ViewHolderState(
            SearchHistoryFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
    
    override fun onBindFooter(holder: ViewHolderState<Any>) {
        val binding = holder.view as? SearchHistoryFooterBinding ?: return
        // Hide footer when list is empty
        binding.searchClearCallHistory.apply {
            isGone = immutableCurrentList.isEmpty()
            if (isLayout(TV or EMULATOR)) {
                isFocusable = true
                isFocusableInTouchMode = true
            }
            setOnClickListener {
                clickCallback.invoke(SearchHistoryCallback(null, SEARCH_HISTORY_CLEAR))
            }
        }
    }
}
