package com.exomatik.manajemenpangkat.ui.adminFakultas

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.exomatik.manajemenpangkat.R
import kotlinx.android.synthetic.main.activity_detail_pdf.*

class DetailPDFActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail_pdf)

        webView.webViewClient = WebViewClient()
        webView.settings.setSupportZoom(true)
        webView.settings.javaScriptEnabled = true
        val url = intent.getStringExtra("urlFile")
//        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$url")
        webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=$url")
    }
}