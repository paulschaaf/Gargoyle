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

import com.github.paulschaaf.gargoyle.model.Story
import java.net.HttpURLConnection
import java.net.URL

class IFDB {
  companion object {
    val CONNECT_TIMEOUT = 15000
    val QUERY_URL = "http://ifdb.tads.org/viewgame?ifiction&id="
    val READ_TIMEOUT = 10000

    fun createStoryFromIfID(ifID: String): Story {
      val urlString = QUERY_URL + ifID
      val httpURLConnection = (URL(urlString).openConnection() as HttpURLConnection).apply {
        readTimeout = READ_TIMEOUT
        connectTimeout = CONNECT_TIMEOUT
        requestMethod = "GET"
        doInput = true
      }

      return httpURLConnection.use { conn->
        conn.inputStream.use { IFDBXmlParser().parseIFXml(it) }
      }
    }
  }
}

fun <T> HttpURLConnection.use(action: (c: HttpURLConnection) -> T): T {
  this.connect()
  try {
    return action(this)
  }
  finally {
    this.disconnect()
  }
}