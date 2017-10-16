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
      "tuid"           -> story.tuid = getText()
      "link"           -> story.link = getText()
      "coverart"       -> readChildren {
        when (it) {
          "url" -> story.coverArtURL = getText()
        }
      }
      "averageRating"  -> story.averageRating = getText()?.toDoubleOrNull()
      "starRating"     -> story.starRating = getText()?.toDoubleOrNull()
      "ratingCountAvg" -> story.ratingCountAvg = getText()?.toIntOrNull()
      "ratingCountTot" -> story.ratingCountTotal = getText()?.toIntOrNull()
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
}
