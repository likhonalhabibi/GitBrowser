package com.gitbrowser.sentinel.ui

import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun BrowserScreen() {
    var url by remember { mutableStateOf("https://www.google.com") }
    var webView: WebView? = null
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = url,
            onValueChange = { url = it },
            label = { Text("Enter URL") },
            modifier = Modifier
                .fillMaxWidth()
                .onKeyEvent {
                    if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                        webView?.loadUrl(url)
                        focusManager.clearFocus()
                        true
                    } else {
                        false
                    }
                },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
            keyboardActions = KeyboardActions(onGo = {
                webView?.loadUrl(url)
                focusManager.clearFocus()
            })
        )

        Row {
            Button(onClick = { webView?.goBack() }) {
                Text("Back")
            }
            Button(onClick = { webView?.goForward() }) {
                Text("Forward")
            }
            Button(onClick = { webView?.reload() }) {
                Text("Reload")
            }
            Button(onClick = { webView?.loadUrl(url) }) {
                Text("Go")
            }
        }

        AndroidView(factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true // Basic requirement for many sites
                loadUrl(url)
                webView = this
            }
        }, modifier = Modifier.fillMaxSize())
    }
}
