package com.readrops.api.localfeed

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.readrops.api.utils.extensions.checkRoot
import java.io.InputStream

object LocalRSSHelper {

    private const val RSS_1_CONTENT_TYPE = "application/rdf+xml"
    private const val RSS_2_CONTENT_TYPE = "application/rss+xml"
    private const val ATOM_CONTENT_TYPE = "application/atom+xml"
    private const val JSONFEED_CONTENT_TYPE = "application/feed+json"
    private const val JSON_CONTENT_TYPE = "application/json"

    private const val RSS_1_REGEX = "<rdf:RDF.*xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\""
    private const val RSS_2_REGEX = "rss.*version=\"2.0\""
    private const val ATOM_REGEX = "<feed.* xmlns=\"http://www.w3.org/2005/Atom\""

    const val RSS_1_ROOT_NAME = "RDF"
    const val RSS_2_ROOT_NAME = "rss"
    const val ATOM_ROOT_NAME = "feed"
    val RSS_ROOT_NAMES = Names.of(RSS_1_ROOT_NAME, RSS_2_ROOT_NAME, ATOM_ROOT_NAME)

    /**
     * Guess RSS type based on content-type header
     */
    fun getRSSType(contentType: String): RSSType {
        return when (contentType) {
            RSS_1_CONTENT_TYPE -> RSSType.RSS_1
            RSS_2_CONTENT_TYPE -> RSSType.RSS_2
            ATOM_CONTENT_TYPE -> RSSType.ATOM
            JSON_CONTENT_TYPE, JSONFEED_CONTENT_TYPE -> RSSType.JSONFEED
            else -> RSSType.UNKNOWN
        }
    }

    /**
     * Guess RSS type based on xml content
     */
    fun getRSSContentType(content: InputStream): RSSType {
        val stringBuffer = StringBuffer()
        val reader = content.bufferedReader()

        // we get the first 10 lines which should be sufficient to get the type,
        // otherwise iterating over the whole file could be too slow
        for (i in 0..9) stringBuffer.append(reader.readLine())

        val string = stringBuffer.toString()
        val type = when {
            RSS_1_REGEX.toRegex().containsMatchIn(string) -> RSSType.RSS_1
            RSS_2_REGEX.toRegex().containsMatchIn(string) -> RSSType.RSS_2
            ATOM_REGEX.toRegex().containsMatchIn(string) -> RSSType.ATOM
            else -> RSSType.UNKNOWN
        }

        reader.close()
        content.close()
        return type
    }

    @JvmStatic
    fun isRSSType(type: String?): Boolean {
        return if (type != null) getRSSType(type) != RSSType.UNKNOWN else false
    }

    fun guessRSSType(konsumer: Konsumer): RSSType = when {
        konsumer.checkRoot(RSS_1_ROOT_NAME) -> RSSType.RSS_1
        konsumer.checkRoot(RSS_2_ROOT_NAME) -> RSSType.RSS_2
        konsumer.checkRoot(ATOM_ROOT_NAME) -> RSSType.ATOM
        else -> RSSType.UNKNOWN
    }

    enum class RSSType {
        RSS_1,
        RSS_2,
        ATOM,
        JSONFEED,
        UNKNOWN
    }
}