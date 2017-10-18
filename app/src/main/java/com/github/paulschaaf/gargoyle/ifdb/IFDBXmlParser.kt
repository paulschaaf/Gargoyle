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

import android.util.Xml
import com.github.paulschaaf.gargoyle.model.Story
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

class IFDBXmlParser {
  var path = listOf<String>()
  val parser = Xml.newPullParser().apply {
    setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
  }

  val story = Story()

  val documentParser = XmlParentElement(parser).apply {
    "identification" {
      "ifid" then { story.ifId = getText() ?: "-error-" }
    }
    "bibliographic" {
      "title" then { story.title = getText() }
      "author" then { story.author = getText() }
      "language" then { story.language = getText() }
      "firstpublished" then { story.firstPublished = getText() }
      "genre" then { story.genre = getText() }
      "description" then { story.description = getText() }
      "series" then { story.series = getText() }
      "seriesnumber" then { story.seriesNumber = getText()?.toIntOrNull() }
      "forgiveness" then { story.forgiveness = getText() }
    }
    "ifdb" {
      "tuid" then { story.tuid = getText() }
      "link" then { story.link = getText() }
      "coverart" {
        "url" then { story.coverArtURL = getText() }
      }
      "averageRating" then { story.averageRating = getText()?.toDoubleOrNull() }
      "starRating" then { story.starRating = getText()?.toDoubleOrNull() }
      "ratingCountAvg" then { story.ratingCountAvg = getText()?.toIntOrNull() }
      "ratingCountTot" then { story.ratingCountTotal = getText()?.toIntOrNull() }
    }
  }

  fun parseIFXml(inputStream: InputStream): Story {
    parser.setInput(inputStream, null)
    parser.next()
    documentParser.parse()
    return story
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

}

interface XmlElement {
  fun parse()
}

class XmlLeafElement(private val fn: () -> Unit): XmlElement {
  override fun parse() = fn()
}

class XmlParentElement(val parser: XmlPullParser): XmlElement {
  val children = mutableMapOf<String, XmlElement>()

  operator fun String.invoke(fn: XmlElement.() -> Unit): XmlElement {
    val element = XmlParentElement(parser).apply(fn)
    children[this] = element
    return element
  }

  infix fun String.then(fn: () -> Unit) {
    children[this] = XmlLeafElement(fn)
  }

  override fun parse() {
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType == XmlPullParser.START_TAG)
        children[parser.name]?.parse()
      else
        println("skipping element '${parser.name}'")
    }
  }
}
