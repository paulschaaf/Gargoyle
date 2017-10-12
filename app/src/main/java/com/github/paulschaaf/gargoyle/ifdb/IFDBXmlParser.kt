/*
 * Copyright © 2017 P.G. Schaaf <paul.schaaf@gmail.com>
 * This file is part of Gargoyle.
 * Gargoyle is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.paulschaaf.gargoyle.ifdb

import android.util.Log
import android.util.Xml
import com.github.paulschaaf.gargoyle.model.Story
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

class IFDBXmlParser(val parser: XmlPullParser) {
  companion object {
    val TAG = "IFDBXmlParser"

    fun createStoryFrom(string: String) = string
      .byteInputStream()
      .use { createStoryFrom(it) }

    fun createStoryFrom(inputStream: InputStream): Story {
      val parser = Xml.newPullParser().apply {
        setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        setInput(inputStream, null)
      }
      try {
        return inputStream.use { IFDBXmlParser(parser).parseIFIndex() }
      }
      catch (ex: Exception) {
        Log.e(TAG, "Hit exception " + ex.toString())
      }
      return Story()
    }
  }

  val story = Story()

  private fun parseIFIndex(): Story {
    parser.next()
    parser.require(XmlPullParser.START_TAG, null, "ifindex")
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType != XmlPullParser.START_TAG) continue
      when (parser.name) {
        "story" -> parseStory()
        else    -> skip()
      }
    }
    return story
  }

  private fun parseStory() {
    parser.require(XmlPullParser.START_TAG, null, "story")
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType != XmlPullParser.START_TAG) continue
      when (parser.name) {
        "colophon"       -> readColophon()
        "identification" -> readIdentification()
        "bibliographic"  -> readBibliographic()
        "ifdb"           -> readIFDB()
        "contact"        -> skip()
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
        "ifid"   -> story.ifId = getText() ?: "-error-"
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
        "seriesnumber"   -> story.seriesNumber = getText()?.toIntOrNull()
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
        "tuid"           -> story.tuid = getText()
        "link"           -> story.link = getText()
        "coverart"       -> readCoverArt()
        "averageRating"  -> story.averageRating = getText()?.toDoubleOrNull()
        "starRating"     -> story.starRating = getText()?.toDoubleOrNull()
        "ratingCountAvg" -> story.ratingCountAvg = getText()?.toIntOrNull()
        "ratingCountTot" -> story.ratingCountTotal = getText()?.toIntOrNull()
        else             -> skip()
      }
    }
  }

  private fun readCoverArt() {
    parser.require(XmlPullParser.START_TAG, null, "coverart")
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType != XmlPullParser.START_TAG) continue
      when (parser.name) {
        "url" -> story.coverArtURL = getText()
        else  -> skip()
      }
    }
  }

  private fun getText(): String? {
    val elementName = parser.name
    parser.require(XmlPullParser.START_TAG, null, elementName)
    var result: String? = null
    do {
      if (parser.next() == XmlPullParser.TEXT) {
        result = (result ?: "") + parser.text
        // pschaaf 09/250/17 14:09: this is a hack, but what else can I do? Instead of returning null the library returns "null".
        if (result == "null") result = null
        parser.nextTag()
      }
    }
    while (parser.name != elementName)
    parser.require(XmlPullParser.END_TAG, null, elementName)
    return result
  }

  private fun skip() {
    if (parser.eventType == XmlPullParser.END_TAG) parser.next()
    else {
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
}
