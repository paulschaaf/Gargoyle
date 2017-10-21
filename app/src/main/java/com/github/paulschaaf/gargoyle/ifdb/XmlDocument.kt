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
  protected fun skipElement(parser: XmlPullParser): StringBuilder {
    var depth = 1
    val result = StringBuilder()
    while (depth != 0) when (parser.next()) {
      XmlPullParser.TEXT      -> result.append(parser.text)
      XmlPullParser.START_TAG -> {
        depth++
        result.append('<').append(parser.name).append('>')
      }
      XmlPullParser.END_TAG   -> {
        depth--
        if (depth > 0) result.append("</").append(parser.name).append('>')
      }
    }
    return result
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

  infix fun String.to(prop: KMutableProperty<String?>) = writeTo(prop) { it }

  fun String.writeTo(prop: KMutableProperty<String?>) = writeTo(prop) { it }

  fun <T> String.writeTo(prop: KMutableProperty<T>, converter: (String?) -> T) {
    children[this] = XmlLeafElement(this, prop, converter)
  }

  override fun parse(parser: XmlPullParser) {
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType == XmlPullParser.START_TAG) {
        if (children.containsKey(parser.name))
          children[parser.name]!!.parse(parser)
        else {
          println("skipping element '${parser.name}'")
          skipElement(parser)
        }
      }
    }
  }
}

class XmlLeafElement<T>(name: String, val prop: KMutableProperty<T>, val convert: (String?) -> T):
    XmlElement(name) {
  override fun parse(parser: XmlPullParser) {
    val result = skipElement(parser)
    val convertedValue =
        if (result.isEmpty()) null
        else convert(result.toString().replace("<([^>]+)></\\1>".toRegex(), "<$1/>"))
    prop.setter.call(convertedValue)
  }
}
