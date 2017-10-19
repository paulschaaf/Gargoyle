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
import kotlin.reflect.KMutableProperty

class IFDBXmlParser {
  val parser = Xml.newPullParser().apply {
    setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
  }

  val story = Story()

  val documentParser = XmlDocument("story") {
    "identification" {
      "ifid" then { story.ifId = getText() ?: "-error-" }
    }
    "bibliographic" {
      "title" then { story.title = getText() ?: "-Unknown-" }
      "author" then { story.author = getText() }
      "language" then { story.language = getText() }
      "firstpublished" then { story.firstPublished = getText() }
      "genre" then { story.genre = getText() }
      "description" then { story.description = getText() }
      "series" then { story.series = getText() }
      "seriesnumber" sets story::seriesNumber
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
      "ratingCountAvg" sets story::ratingCountAvg
      "ratingCountTot" sets story::ratingCountTotal
    }
  }

  fun parseIFXml(inputStream: InputStream): Story {
    parser.setInput(inputStream, null)
    parser.next()
    documentParser.parse(parser)
    return story
  }
}

interface XmlElement {
  fun parse(parser: XmlPullParser)
}

class XmlDocument(val name: String, structure: XmlParentElement.() -> Unit): XmlElement {
  val root = XmlParentElement().apply { name.invoke(structure) }
  override fun parse(parser: XmlPullParser) = root.parse(parser)
}

class XmlLeafElement(private val fn: XmlLeafElement.() -> Unit): XmlElement {
  private var _parser: XmlPullParser? = null
  override fun parse(parser: XmlPullParser) {
    _parser = parser  // todo pschaaf 10/291/17 18:10: this is a hack
    fn(this)
  }

  fun getText(): String? {
    val elementName = _parser?.name
    var result: String? = null
    do {
      if (_parser?.next() == XmlPullParser.TEXT) {
        result = (result ?: "") + _parser?.text
        // pschaaf 09/250/17 14:09: this is a hack, but what else can I do? Instead of returning null the library returns "null".
        if (result == "null") result = null
        _parser?.nextTag()
      }
    }
    while (_parser?.name != elementName)
    _parser?.require(XmlPullParser.END_TAG, null, elementName)
    return result
  }

}

open class XmlParentElement: XmlElement {
  val children = mutableMapOf<String, XmlElement>()

  operator fun String.invoke(fn: XmlParentElement.() -> Unit): XmlParentElement {
    val element = XmlParentElement().apply(fn)
    children[this] = element
    return element
  }

  infix fun String.then(fn: XmlLeafElement.() -> Unit) {
    children[this] = XmlLeafElement(fn)
  }

  infix fun String.sets(prop: KMutableProperty<Int?>) {
    children[this] = XmlLeafElement { prop.setter.call(this.getText()?.toIntOrNull()) }
  }

  override fun parse(parser: XmlPullParser) {
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType == XmlPullParser.START_TAG && children.containsKey(parser.name))
        children[parser.name]!!.parse(parser)
      else {
        println("skipping element '${parser.name}'")
        var depth = 1
        while (depth != 0) when (parser.next()) {
          XmlPullParser.START_TAG -> depth++
          XmlPullParser.END_TAG   -> depth--
        }
      }
    }
  }
}
