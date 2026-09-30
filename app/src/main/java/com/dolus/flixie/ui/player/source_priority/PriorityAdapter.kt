package com.dolus.flixie.ui.player.source_priority

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dolus.flixie.databinding.PlayerPrioritizeItemBinding
import com.dolus.flixie.ui.NoStateAdapter
import com.dolus.flixie.ui.ViewHolderState

data class SourcePriority<T>(
    val data: T,
    val name: String,
    var priority: Int
)

class PriorityAdapter<T>() :
    NoStateAdapter<SourcePriority<T>>() {

    override fun onCreateContent(parent: ViewGroup): ViewHolderState<Any> {
        return ViewHolderState(
            PlayerPrioritizeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindContent(
        holder: ViewHolderState<Any>,
        item: SourcePriority<T>,
        position: Int
    ) {
        val binding = holder.view as? PlayerPrioritizeItemBinding ?: return
        binding.priorityText.text = item.name

        fun updatePriority() {
            binding.priorityNumber.text = item.priority.toString()
        }

        updatePriority()
        binding.addButton.setOnClickListener {
            // If someone clicks til the integer limit then they deserve to crash.
            item.priority++
            updatePriority()
        }

        binding.subtractButton.setOnClickListener {
            item.priority--
            updatePriority()
        }
    }
}