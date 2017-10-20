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
      "ifid".writeTo(story::ifId, "-error-")
    }
    "bibliographic" {
      "title".writeTo(story::title, "-Unknown-")
      "author".writeTo(story::author)
      "language".writeTo(story::language)
      "firstpublished".writeTo(story::firstPublished)
      "genre".writeTo(story::genre)
      "description".writeTo(story::description)
      "series".writeTo(story::series)
      "seriesnumber".writeTo(story::seriesNumber) { it?.toIntOrNull() }
      "forgiveness".writeTo(story::forgiveness)
    }
    "ifdb" {
      "tuid".writeTo(story::tuid)
      "link".writeTo(story::link)
      "coverart" {
        "url".writeTo(story::coverArtURL)
      }
      "averageRating".writeTo(story::averageRating) { it?.toDoubleOrNull() }
      "starRating".writeTo(story::starRating) { it?.toDoubleOrNull() }
      "ratingCountAvg".writeTo(story::ratingCountAvg) { it?.toIntOrNull() }
      "ratingCountTot".writeTo(story::ratingCountTotal) { it?.toIntOrNull() }
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

class XmlDocument(val name: String, val structure: XmlParentElement.() -> Unit): XmlElement {
  override fun parse(parser: XmlPullParser) = XmlParentElement()
    .apply { name.invoke(structure) }
    .parse(parser)
}

class XmlLeafElement<T>(val prop: KMutableProperty<T>, val convert: (String?) -> T):
    XmlElement {
  override fun parse(parser: XmlPullParser) {
    val elementName = parser.name
    var result: String? = null
    do {
      if (parser.next() == XmlPullParser.TEXT) {
        result = parser.text
        parser.nextTag()
      }
    }
    while (parser.name != elementName)
    parser.require(XmlPullParser.END_TAG, null, elementName)
    val convertedValue = convert(result)
    prop.setter.call(convertedValue)
  }
}

open class XmlParentElement: XmlElement {
  val children = mutableMapOf<String, XmlElement>()

  operator fun String.invoke(fn: XmlParentElement.() -> Unit): XmlParentElement {
    val element = XmlParentElement().apply(fn)
    children[this] = element
    return element
  }

  fun String.writeTo(prop: KMutableProperty<String>, default: String) =
      writeTo(prop) { it ?: default }

  fun String.writeTo(prop: KMutableProperty<String?>) = writeTo(prop) { it }

  fun <T> String.writeTo(prop: KMutableProperty<T>, converter: (String?) -> T) {
    children[this] = XmlLeafElement(prop, converter)
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
