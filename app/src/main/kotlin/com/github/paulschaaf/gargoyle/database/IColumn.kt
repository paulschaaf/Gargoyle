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

  operator fun get(story: Story): T? = get(story.contentValues)
  operator fun set(story: Story, value: T?) = set(story.contentValues, value)

  operator fun get(conValues: ContentValues): T?
  operator fun set(conValues: ContentValues, value: T?)

  operator fun get(map: Map<String, T?>): T? = map[name]
  operator fun set(map: MutableMap<String, T?>, value: T?) = map.put(name, value)
}

interface IDoubleColumn: IColumn<Double> {
  override val sqlDataType
    get() = "DOUBLE"

  override fun get(conValues: ContentValues): Double? = conValues.getAsDouble(name)
  override fun set(conValues: ContentValues, value: Double?) = conValues.put(name, value)
}

interface IIntColumn: IColumn<Int> {
  override val sqlDataType
    get() = "INTEGER"

  override fun get(conValues: ContentValues): Int? = conValues.getAsInteger(name)
  override fun set(conValues: ContentValues, value: Int?) = conValues.put(name, value)
}

interface IStringColumn: IColumn<String> {
  override val sqlDataType
    get() = "TEXT"

  override fun get(conValues: ContentValues): String? = conValues.getAsString(name)
  override fun set(conValues: ContentValues, value: String?) = conValues.put(name, value)
}

class DoubleColumn(override val name: String, override val createProperties: String = ""):
    IDoubleColumn

class IntColumn(override val name: String, override val createProperties: String = ""):
    IIntColumn

class StringColumn(override val name: String, override val createProperties: String = ""):
    IStringColumn

