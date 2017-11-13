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

import android.content.ContentValues

@Suppress("UNCHECKED_CAST")
fun <T> ContentValues.read(name: String, klass: Class<T>): T = when (klass) {
  Boolean::class.java          -> getAsBoolean(name)
  Byte::class.java             -> getAsByte(name)
  ByteArray::class.java        -> getAsByteArray(name)
  java.lang.Double::class.java -> getAsDouble(name)
  java.lang.Float::class.java  -> getAsFloat(name)
  Integer::class.java          -> getAsInteger(name)
  Long::class.java             -> getAsLong(name)
  Short::class.java            -> getAsShort(name)
  else                         -> getAsString(name)
} as T

fun <T> ContentValues.set(name: String, value: T) = when (value) {
  is Boolean?   -> put(name, value)
  is Byte?      -> put(name, value)
  is ByteArray? -> put(name, value)
  is Double?    -> put(name, value)
  is Float?     -> put(name, value)
  is Int?       -> put(name, value)
  is Long?      -> put(name, value)
  is Short?     -> put(name, value)
  else          -> put(name, value as String)
}
