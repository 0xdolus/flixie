package com.dolus.flixie.ui.settings.testing

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.dolus.flixie.CommonActivity.showToast
import com.dolus.flixie.MainAPI
import com.dolus.flixie.R
import com.dolus.flixie.databinding.ProviderTestItemBinding
import com.dolus.flixie.mvvm.getAllMessages
import com.dolus.flixie.mvvm.getStackTracePretty
import com.dolus.flixie.plugins.PluginManager
import com.dolus.flixie.ui.BaseDiffCallback
import com.dolus.flixie.ui.NoStateAdapter
import com.dolus.flixie.ui.ViewHolderState
import com.dolus.flixie.utils.Coroutines.ioSafe
import com.dolus.flixie.utils.Coroutines.runOnMainThread
import com.dolus.flixie.utils.SubtitleHelper.getFlagFromIso
import com.dolus.flixie.utils.TestingUtils
import java.io.File

class TestResultAdapter() :
    NoStateAdapter<Pair<MainAPI, TestingUtils.TestResultProvider>>(
        diffCallback = BaseDiffCallback(
            itemSame = { a, b ->
                a.first.name == b.first.name && a.first.mainUrl == b.first.mainUrl
            },
            contentSame = { a, b ->
                a == b
            })
    ) {
    companion object {
        private fun String.lastLine(): String? {
            return this.lines().lastOrNull { it.isNotBlank() }
        }
    }

    override fun onClearView(holder: ViewHolderState<Any>) {
        val binding = holder.view as? ProviderTestItemBinding ?: return
        clearImage(binding.actionButton)
    }

    override fun onCreateContent(parent: ViewGroup): ViewHolderState<Any> {
        return ViewHolderState(
            ProviderTestItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindContent(
        holder: ViewHolderState<Any>,
        item: Pair<MainAPI, TestingUtils.TestResultProvider>,
        position: Int
    ) {
        val binding = holder.view as? ProviderTestItemBinding ?: return
        val (api, result) = item

        val itemView = holder.itemView

        val languageText: TextView = binding.langIcon
        val providerTitle: TextView = binding.mainText
        val statusText: TextView = binding.passedFailedMarker
        val failDescription: TextView = binding.failDescription
        val logButton: ImageView = binding.actionButton

        languageText.text = getFlagFromIso(api.lang)
        providerTitle.text = api.name

        val (resultText, resultColor) = if (result.success) {
            if (result.log.any { it.level == TestingUtils.Logger.LogLevel.Warning }) {
                R.string.test_warning to R.color.colorTestWarning
            } else {
                R.string.test_passed to R.color.colorTestPass
            }
        } else {
            R.string.test_failed to R.color.colorTestFail
        }

        statusText.setText(resultText)
        statusText.setTextColor(ContextCompat.getColor(itemView.context, resultColor))

        val stackTrace = result.exception?.getStackTracePretty(false)?.ifBlank { null }
        val messages = result.exception?.getAllMessages()?.ifBlank { null }
        val resultLog = result.log.joinToString("\n")
        val fullLog =
            resultLog +
                    (messages?.let { "\n\nError: $it" } ?: "") +
                    (stackTrace?.let { "\n\n$it" } ?: "")

        failDescription.text = messages?.lastLine() ?: resultLog.lastLine()

        logButton.setOnClickListener {
            val builder: AlertDialog.Builder =
                AlertDialog.Builder(it.context, R.style.AlertDialogCustom)
            builder.setMessage(fullLog)
                .setTitle(R.string.test_log)
                // Ok button just closes the dialog
                .setPositiveButton(R.string.ok) { _, _ -> }

            api.sourcePlugin?.let { path ->
                val pluginFile = File(path)
                // Cannot delete a deleted plugin
                if (!pluginFile.exists()) return@let

                builder.setNegativeButton(R.string.delete_plugin) { _, _ ->
                    ioSafe {
                        val success = PluginManager.deletePlugin(pluginFile)

                        runOnMainThread {
                            if (success) {
                                showToast(R.string.plugin_deleted, Toast.LENGTH_SHORT)
                            } else {
                                showToast(R.string.error, Toast.LENGTH_SHORT)
                            }
                        }
                    }
                }
            }

            builder.show()
        }
    }
}