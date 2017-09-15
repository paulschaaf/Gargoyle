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
import kotlin.reflect.KProperty

interface IColumn<T> {
  val name: String
  val sqlDataType: String
  val createProperties: String

  val createSQL: String
    get() = "$name $sqlDataType $createProperties".trim()

  operator fun getValue(conValues: ContentValues, property: KProperty<*>): T
      = get(conValues)

  operator fun setValue(conValues: ContentValues, property: KProperty<*>, value: T)
      = set(conValues, value)

  operator fun get(conValues: ContentValues): T
  operator fun set(conValues: ContentValues, value: T)
}

interface IDoubleColumn<T: Double?>: IColumn<T> {
  override val sqlDataType
    get() = "DOUBLE"

  @Suppress("UNCHECKED_CAST")
  override fun get(conValues: ContentValues): T = conValues.getAsDouble(name) as T

  override fun set(conValues: ContentValues, value: T) = conValues.put(name, value)

  interface nonNull: IDoubleColumn<Double>
}

interface IIntColumn<T: Int?>: IColumn<T> {
  override val sqlDataType
    get() = "INTEGER"

  @Suppress("UNCHECKED_CAST")
  override fun get(conValues: ContentValues): T = conValues.getAsInteger(name) as T

  override fun set(conValues: ContentValues, value: T) = conValues.put(name, value)

  interface nonNull: IIntColumn<Int>
}

interface IStringColumn<T: String?>: IColumn<T> {
  override val sqlDataType
    get() = "TEXT"

  @Suppress("UNCHECKED_CAST")
  override fun get(conValues: ContentValues): T = conValues.getAsString(name) as T

  override fun set(conValues: ContentValues, value: T) = conValues.put(name, value)

  interface nonNull: IStringColumn<String>
}


open class DoubleColumn(override val name: String, override val createProperties: String = ""):
    IDoubleColumn<Double?> {
  open class nonNull(override val name: String, override val createProperties: String = ""):
      IDoubleColumn.nonNull
}

open class IntColumn(override val name: String, override val createProperties: String = ""):
    IIntColumn<Int?> {
  open class nonNull(override val name: String, override val createProperties: String = ""):
      IIntColumn.nonNull
}

class PrimaryKeyColumn(override val name: String, override val createProperties: String = ""):
    IntColumn.nonNull(name, createProperties)

open class StringColumn(override val name: String, override val createProperties: String = ""):
    IStringColumn<String?> {
  open class nonNull(override val name: String, override val createProperties: String = ""):
      IStringColumn.nonNull
}
