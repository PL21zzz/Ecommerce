package com.ai.ecommerce.presentation.cart

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PaymentWebViewScreen(
    url: String,
    onPaymentComplete: (Boolean) -> Unit
) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, urlStr: String?) {
                        super.onPageFinished(view, urlStr)
                        if (urlStr != null && urlStr.contains("payment-callback")) {
                            onPaymentComplete(urlStr.contains("vnp_ResponseCode=00"))
                        }
                    }
                }
                loadUrl(url)
            }
        }
    )
}
