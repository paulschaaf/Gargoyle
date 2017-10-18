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

  fun readChildren(parseChild: (String) -> Unit) {
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType == XmlPullParser.START_TAG) parseChild(parser.name)
    }
  }

  fun parseIFXml(inputStream: InputStream): Story {
    parser.setInput(inputStream, null)
    parser.next()
    readChildren {
      when (it) {
        "story" -> readStory()
      }
    }
    return story
  }

  private fun readStory() = readChildren {
    when (it) {
      "identification" -> readChildren {
        when (it) {
          "ifid" -> story.ifId = getText() ?: "-error-"
        }
      }
      "bibliographic"  -> readBibliographic()
      "ifdb"           -> readIFDB()
    }
  }

  private fun readBibliographic() = readChildren {
    when (it) {
      "title"          -> story.title = getText()
      "author"         -> story.author = getText()
      "language"       -> story.language = getText()
      "firstpublished" -> story.firstPublished = getText()
      "genre"          -> story.genre = getText()
      "description"    -> story.description = getText()
      "series"         -> story.series = getText()
      "seriesnumber"   -> story.seriesNumber = getText()?.toIntOrNull()
      "forgiveness"    -> story.forgiveness = getText()
    }
  }

  private fun readIFDB() = readChildren {
    when (it) {
      "averageRating"  -> story.averageRating = getText()?.toDoubleOrNull()
      "coverart"       -> readChildren {
        when (it) {
          "url" -> story.coverArtURL = getText()
        }
      }
      "link"           -> story.link = getText()
      "starRating"     -> story.starRating = getText()?.toDoubleOrNull()
      "ratingCountAvg" -> story.ratingCountAvg = getText()?.toIntOrNull()
      "ratingCountTot" -> story.ratingCountTotal = getText()?.toIntOrNull()
      "tuid"           -> story.tuid = getText()
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

  interface XmlElementParser {
    fun parse()
  }

  inner class XmlLeafElement(private val fn: () -> Unit): XmlElementParser {
    override fun parse() = fn()
  }

  operator fun String.invoke(fn: XmlElementParser.() -> Unit): XmlElementParser {
    val element = XmlParentElement().apply(fn)
    element.children[this] = element.fn(this@XmlParentElement))
    return element
  }

  inner class XmlParentElement: XmlElementParser {
    val children = mutableMapOf<String, XmlElementParser>()

    infix fun String.then(fn: () -> Unit) = children.set(this, XmlLeafElement(fn))

    override fun parse() {
      while (parser.next() != XmlPullParser.END_TAG) {
        if (parser.eventType == XmlPullParser.START_TAG)
          children[parser.name]?.parse()
        else
          print("skipping element '${parser.name}'")
      }
    }
  }

    val baz = "ifdb" {
      "averageRating" then { story.averageRating = getText()?.toDoubleOrNull() }
      "coverart" {
        "url" then { story.coverArtURL = getText() }
      }
      "link" then { story.link = getText() }
    }

//    val bar = (
//        "identification" to (
//          "ifid" to { story.ifId = getText() ?: "-error-" }
//        ),
//        "bibliographic"  to (
//          "title"          to { story.title = getText() },
//          "author"         to { story.author = getText() },
//          "language"       to { story.language = getText() },
//          "firstpublished" to { story.firstPublished = getText() },
//          "genre"          to { story.genre = getText() },
//          "description"    to { story.description = getText() },
//          "series"         to { story.series = getText() },
//          "seriesnumber"   to { story.seriesNumber = getText()?.toIntOrNull() },
//          "forgiveness"    to { story.forgiveness = getText() }
//        ),
//        "ifdb"           to (
//          "tuid"           to { story.tuid = getText() },
//          "link"           to { story.link = getText() },
//          "coverart"       to (
//              "url" to { story.coverArtURL = getText() }
//          )
//          "averageRating"  to { story.averageRating = getText()?.toDoubleOrNull() },
//          "starRating"     to { story.starRating = getText()?.toDoubleOrNull() },
//          "ratingCountAvg" to { story.ratingCountAvg = getText()?.toIntOrNull() },
//          "ratingCountTot" to { story.ratingCountTotal = getText()?.toIntOrNull() }
//        ),
//    )
  }

}
