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

package com.github.paulschaaf.gargoyle.database

import android.content.ContentValues
import com.github.paulschaaf.gargoyle.model.Story

interface IColumn<T> {
  val name: String
  val sqlDataType: String
  val createProperties: String

  val createSQL: String
    get() = "$name $sqlDataType $createProperties".trim()

  operator fun get(story: Story): T = get(story.contentValues)
  operator fun set(story: Story, value: T) = set(story.contentValues, value)

  operator fun get(conValues: ContentValues): T
  operator fun set(conValues: ContentValues, value: T)

  operator fun get(map: Map<String, T>): T? = map[name]
  operator fun set(map: MutableMap<String, T>, value: T) = map.put(name, value)
}

interface IDoubleColumn<T: Double?>: IColumn<T> {
  override val sqlDataType
    get() = "DOUBLE"

  @Suppress("UNCHECKED_CAST")
  override fun get(conValues: ContentValues): T = conValues.getAsDouble(name) as T

  override fun set(conValues: ContentValues, value: T) = conValues.put(name, value)
}

interface IIntColumn<T: Int?>: IColumn<T> {
  override val sqlDataType
    get() = "INTEGER"

  @Suppress("UNCHECKED_CAST")
  override fun get(conValues: ContentValues): T = conValues.getAsInteger(name) as T

  override fun set(conValues: ContentValues, value: T) = conValues.put(name, value)
}

interface IStringColumn<T: String?>: IColumn<T> {
  override val sqlDataType
    get() = "TEXT"

  @Suppress("UNCHECKED_CAST")
  override fun get(conValues: ContentValues): T = conValues.getAsString(name) as T

  override fun set(conValues: ContentValues, value: T) = conValues.put(name, value)
}

class DoubleColumn(override val name: String, override val createProperties: String = ""):
    IDoubleColumn<Double?> {
  class nonNull(override val name: String, override val createProperties: String = ""):
      IDoubleColumn<Double>
}

class IntColumn(override val name: String, override val createProperties: String = ""):
    IIntColumn<Int?> {
  class nonNull(override val name: String, override val createProperties: String = ""):
      IIntColumn<Int>
}

class StringColumn(override val name: String, override val createProperties: String = ""):
    IStringColumn<String?> {
  class nonNull(override val name: String, override val createProperties: String = ""):
      IStringColumn<String>
}
