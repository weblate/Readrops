package com.readrops.app.utils

import android.content.Context
import com.readrops.app.R
import com.readrops.app.database.entities.Feed
import com.readrops.app.database.entities.Folder
import com.readrops.readropslibrary.opml.model.Body
import com.readrops.readropslibrary.opml.model.Head
import com.readrops.readropslibrary.opml.model.OPML
import com.readrops.readropslibrary.opml.model.Outline

object OPMLMatcher {

    fun opmltoFoldersAndFeeds(opml: OPML): Map<Folder, List<Feed>> {
        val foldersAndFeeds: MutableMap<Folder, List<Feed>> = HashMap()
        val body = opml.body!!

        body.outlines?.forEach { outline ->
            val folder = Folder(outline.title)

            val feeds = arrayListOf<Feed>()
            outline.outlines?.forEach { feedOutline ->
                val feed = Feed().let {
                    it.name = feedOutline.title
                    it.url = feedOutline.xmlUrl
                    it.siteUrl = feedOutline.htmlUrl

                    it
                }

                feeds.add(feed)
            }

            foldersAndFeeds[folder] = feeds
        }

        return foldersAndFeeds
    }


    fun foldersAndFeedsToOPML(foldersAndFeeds: Map<Folder, List<Feed>>, context: Context): OPML {
        val outlines = arrayListOf<Outline>()
        for (folderAndFeeds in foldersAndFeeds) {
            val outline = Outline(folderAndFeeds.key.name)

            val feedOutlines = arrayListOf<Outline>()
            folderAndFeeds.value.forEach { feed ->
                val feedOutline = Outline(feed.name, feed.url, feed.siteUrl)

                feedOutlines.add(feedOutline)
            }

            outline.outlines = feedOutlines
            outlines.add(outline)
        }

        val head = Head(context.getString(R.string.subscriptions))
        val body = Body(outlines)

        return OPML("2.0", head, body)
    }
}