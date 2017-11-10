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
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import kotlin.reflect.KMutableProperty

class XmlDocument(val name: String, val structure: XmlParentElement.() -> Unit) {
  fun parse(inputStream: InputStream) {
    val parser = Xml.newPullParser().apply {
      setInput(inputStream, null)
      require(XmlPullParser.START_DOCUMENT, null, name)
      next()
      require(XmlPullParser.START_TAG, null, name)
    }
    XmlParentElement(name, structure)
      .parse(parser)
  }
}

abstract class XmlElement(val name: String) {
  abstract fun parse(parser: XmlPullParser)

  protected fun skipElement(parser: XmlPullParser) {
    println("skipping element '${parser.name}'")
    summarizeElement(parser, null)
  }

  protected fun summarizeElement(parser: XmlPullParser, summary: StringBuilder?): String? {
    var depth = 1
    while (depth != 0) when (parser.next()) {
      XmlPullParser.TEXT      -> summary?.append(parser.text.trim())
      XmlPullParser.START_TAG -> {
        depth++
        if (summary != null) summary.append('<').append(parser.name).append('>')
      }
      XmlPullParser.END_TAG   -> {
        depth--
        if (depth > 0 && summary != null) summary.append("</").append(parser.name).append('>')
      }
    }
    if (summary == null || summary.isEmpty()) return null
    return summary.toString()
      .replace("<([^>]+)></\\1>".toRegex(), "<$1/>") // collapse empty tags like <br></br> to <br/>
  }
}

open class XmlParentElement(name: String, val structure: XmlParentElement.() -> Unit):
    XmlElement(name) {
  val children = mutableMapOf<String, XmlElement>()

  init {
    apply(structure)
  }

  operator fun String.invoke(structure: XmlParentElement.() -> Unit): XmlParentElement {
    val element = XmlParentElement(this, structure)
    children[this] = element
    return element
  }

  infix fun <T> String.to(prop: KMutableProperty<T>): XmlLeafElement<T> {
    val elem = XmlLeafElement(this, prop)
    children[this] = elem
    return elem
  }

  override fun parse(parser: XmlPullParser) {
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType == XmlPullParser.START_TAG) {
        if (children.containsKey(parser.name))
          children[parser.name]!!.parse(parser)
        else skipElement(parser)
      }
    }
  }
}

class XmlLeafElement<T>(name: String, val prop: KMutableProperty<T>): XmlElement(name) {
  @Suppress("UNCHECKED_CAST")
  private var convertBlock = { it: String-> it as T }

  infix fun via(action: (String) -> T) {
    convertBlock = action
  }

  infix fun default(defaultValue: T) {
    @Suppress("UNCHECKED_CAST")
    convertBlock = { it as T ?: defaultValue }
  }

  override fun parse(parser: XmlPullParser) {
    val str = summarizeElement(parser, StringBuilder())
    if (str != null && str.isNotBlank()) prop.setter.call(convertBlock(str))
  }
}
