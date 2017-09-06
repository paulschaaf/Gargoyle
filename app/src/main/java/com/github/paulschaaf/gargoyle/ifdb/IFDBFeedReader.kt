package com.github.paulschaaf.gargoyle.ifdb

import android.util.Log
import android.util.Xml
import com.github.paulschaaf.gargoyle.model.Story
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by pschaaf on 9/3/17.
 */
class IFDBFeedReader(val parser: XmlPullParser) {
  companion object {
    val CONNECT_TIMEOUT = 15000
    val QUERY_URL = "http://ifdb.tads.org/viewgame?ifiction&id="
    val READ_TIMEOUT = 10000
    val TAG = "IFDBFeedReader"

    //    @Throws(IOException::class, XmlPullParserException::class)
    fun createStoryFrom(ifID: String): Story? {
      val urlString = QUERY_URL + ifID
      val conn = URL(urlString).openConnection() as HttpURLConnection
      conn.apply {
        readTimeout = READ_TIMEOUT
        connectTimeout = CONNECT_TIMEOUT
        requestMethod = "GET"
        doInput = true
      }

      val inputStream = conn.inputStream
      try {
        conn.connect()
        return createStoryFrom(inputStream)
      }
      catch (ex: Exception) {
        Log.e(TAG, "Hit exception " + ex.toString())
      }
      finally {
        inputStream.close()
      }
      return null
    }

    fun createStoryFrom(inputStream: InputStream): Story {
      val parser = Xml.newPullParser().apply {
        setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        setInput(inputStream, null)
      }
      return IFDBFeedReader(parser).parseIFIndex()
    }
  }

  val story = Story()

  private fun parseIFIndex(): Story {
    parser.next()
    parser.require(XmlPullParser.START_TAG, null, "ifindex");
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType != XmlPullParser.START_TAG) continue
      when (parser.name) {
        "story"       -> parseStory()
        else          -> skip()
      }
    }
    return story
  }

  private fun parseStory() {
    parser.require(XmlPullParser.START_TAG, null, "story");
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType != XmlPullParser.START_TAG) continue
      when (parser.name) {
        "colophon"       -> readColophon()
        "identification" -> readIdentification()
        "bibliographic"  -> readBibliographic()
        "contact"        -> skip()
        "ifdb"           -> readIFDB()
        else             -> skip()
      }
    }
  }

  private fun readColophon() {
    parser.require(XmlPullParser.START_TAG, null, "colophon")
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType != XmlPullParser.START_TAG) continue
      skip()
    }
  }

  private fun readIdentification() {
    parser.require(XmlPullParser.START_TAG, null, "identification")
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType != XmlPullParser.START_TAG) continue
      when (parser.name) {
        "ifid"   -> story.ifId = getText()
        "format" -> skip()
        else     -> skip()
      }
    }
  }

  private fun readBibliographic() {
    parser.require(XmlPullParser.START_TAG, null, "bibliographic")
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType != XmlPullParser.START_TAG) continue
      when (parser.name) {
        "title"          -> story.title = getText()
        "author"         -> story.author = getText()
        "language"       -> story.language = getText()
        "firstpublished" -> story.firstPublished = getText()
        "genre"          -> story.genre = getText()
        "description"    -> story.description = getText()
        "series"         -> story.series = getText()
        "seriesnumber"   -> story.seriesNumber = Integer.valueOf(getText())
        "forgiveness"    -> story.forgiveness = getText()
        else             -> skip()
      }
    }
  }

  private fun readIFDB() {
    parser.require(XmlPullParser.START_TAG, null, "ifdb")
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType != XmlPullParser.START_TAG) continue
      when (parser.name) {
        "tuid"           -> skip()
        "link"           -> story.link = getText()
        "coverart"       -> readCoverArt()
        "averageRating"  -> story.averageRating = getText().toDouble()
        "starRating"     -> story.starRating = getText().toInt()
        "ratingCountAvg" -> story.ratingCountAvg = getText().toInt()
        "ratingCountTot" -> story.ratingCountTotal = getText().toInt()
        else             -> skip()
      }
    }
  }

  private fun readCoverArt() {
    parser.require(XmlPullParser.START_TAG, null, "coverart")
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType != XmlPullParser.START_TAG) continue
      when (parser.name) {
        "url" -> skip() // // todo pschaaf 09/247/17 22:09: download cover art
        else  -> skip()
      }
    }
  }

  private fun getText(): String {
    val elementName = parser.name
    parser.require(XmlPullParser.START_TAG, null, elementName)
    var result = ""
    if (parser.next() == XmlPullParser.TEXT) {
      result = parser.text
      parser.nextTag()
    }
    parser.require(XmlPullParser.END_TAG, null, elementName)
    return result
  }

  private fun skip() {
    if (parser.eventType != XmlPullParser.START_TAG) {
      throw IllegalStateException()
    }
    var depth = 1
    while (depth != 0) {
      when (parser.next()) {
        XmlPullParser.START_TAG -> depth++
        XmlPullParser.END_TAG   -> depth--
      }
    }
  }

}
