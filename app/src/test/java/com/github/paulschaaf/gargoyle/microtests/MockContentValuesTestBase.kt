/*
 * Copyright © 2018 P.G. Schaaf <paul.schaaf@gmail.com>
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

package com.github.paulschaaf.gargoyle.microtests

import android.content.ContentValues
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.io.InvalidObjectException

abstract class MockContentValuesTestBase {
  abstract val properties: MutableMap<String, Any>

  val contentValues: ContentValues
    get() = mock(ContentValues::class.java).apply {
      properties.forEach { (columnName, value)->
        when (value) {
          is Double -> {
            `when`(getAsDouble(columnName)).thenReturn(value)
            `when`(put(columnName, value)).then { properties.put(columnName, value) }
          }
          is Int    -> {
            `when`(getAsInteger(columnName)).thenReturn(value)
            `when`(put(columnName, value)).then { properties.put(columnName, value) }
          }
          is Long   -> {
            `when`(getAsLong(columnName)).thenReturn(value)
            `when`(put(columnName, value)).then { properties.put(columnName, value) }
          }
          is String -> {
            `when`(getAsString(columnName)).thenReturn(value.toString())
            `when`(put(columnName, value)).then { properties.put(columnName, value) }
          }
          else      -> InvalidObjectException("Test setup does not handle columns of type " + value.javaClass.name + ".")
        }
      }
    }
}