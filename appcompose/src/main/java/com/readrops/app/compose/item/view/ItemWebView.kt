package com.readrops.app.compose.item.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Base64
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.readrops.app.compose.R
import com.readrops.app.compose.util.Utils
import com.readrops.db.pojo.ItemWithFeed
import org.jsoup.Jsoup
import org.jsoup.parser.Parser

@SuppressLint("SetJavaScriptEnabled")
class ItemWebView(
    context: Context,
    attrs: AttributeSet?,
) : WebView(context, attrs) {

    constructor(context: Context): this(context, null)

    init {
        settings.javaScriptEnabled = true
        settings.builtInZoomControls = true
        settings.displayZoomControls = false
        settings.setSupportZoom(false)

        webViewClient = WebViewClient()
        isVerticalScrollBarEnabled = false
    }

    fun loadText(
        itemWithFeed: ItemWithFeed,
        accentColor: Color,
        backgroundColor: Color,
        onBackgroundColor: Color
    ) {
        val string = context.getString(
            R.string.webview_html_template,
            Utils.getCssColor(accentColor.toArgb()),
            Utils.getCssColor(onBackgroundColor.toArgb()),
            Utils.getCssColor(backgroundColor.toArgb()),
            formatText(itemWithFeed)
        )

        val data = Base64.encodeToString(string.encodeToByteArray(), Base64.NO_PADDING)
        loadData(data, "text/html; charset=utf-8", "base64")
    }

    private fun formatText(itemWithFeed: ItemWithFeed): String {
        return if (itemWithFeed.item.content != null) {
            val document = if (itemWithFeed.websiteUrl != null) Jsoup.parse(
                Parser.unescapeEntities(itemWithFeed.item.text, false), itemWithFeed.websiteUrl
            ) else Jsoup.parse(
                Parser.unescapeEntities(itemWithFeed.item.text, false)
            )

            document.select("div,span").forEach { it.clearAttributes() }
            return document.body().html()
        } else {
            ""
        }
    }

}