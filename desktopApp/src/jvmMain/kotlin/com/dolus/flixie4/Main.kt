package com.dolus.flixie4

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.dolus.flixie4.compose.BlackButton
import com.dolus.flixie4.compose.LocalFocusOutlineDefault
import com.dolus.flixie4.compose.WhiteButton
import com.dolus.flixie4.generated.resources.Res
import com.dolus.flixie4.generated.resources.app_name
import com.dolus.flixie4.generated.resources.default_icon
import com.dolus.flixie4.generated.resources.preview
import com.dolus.flixie4.theme.CloudStreamTheme
import com.dolus.flixie4.theme.CloudStreamThemeMode
import com.mihon.presentation.settings.widget.SwitchPreferenceWidget
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.app_name),
        icon = painterResource(Res.drawable.default_icon)
    ) {
        CloudStreamTheme(mode = CloudStreamThemeMode.Dark) {
            CompositionLocalProvider(LocalFocusOutlineDefault provides false) {
                Scaffold(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ) {
                    Column {
                        Text("Hello, World!")
                        Row {
                            WhiteButton("Hello in White") {
                            }
                            BlackButton("Hello in Black") {
                            }
                        }
                        var checked by remember { mutableStateOf(false) }
                        SwitchPreferenceWidget(
                            title = "hello", subtitle = "world", icon = painterResource(
                                Res.drawable.preview
                            ),
                            checked = checked,
                            onCheckedChanged = { checked = !checked }
                        )
                    }
                }
            }

        }
    }
}