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

// todo pschaaf 09/264/17 18:09: This hierarchy is too complicated!

package com.github.paulschaaf.gargoyle.database

import android.content.ContentValues
import kotlin.reflect.KProperty

interface IColumn<T> {
  val name: String
  val sqlDataType: String
  val createProperties
    get() = ""

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
}

interface IIntColumn<T: Int?>: IColumn<T> {
  override val sqlDataType
    get() = "INTEGER"

  @Suppress("UNCHECKED_CAST")
  override fun get(conValues: ContentValues): T = conValues.getAsInteger(name) as T

  override fun set(conValues: ContentValues, value: T) = conValues.put(name, value)

  interface nonNull: IIntColumn<Int> {
    override val createProperties: String
      get() = "NOT NULL"
  }
}

interface IStringColumn<T: String?>: IColumn<T> {
  override val sqlDataType
    get() = "TEXT"

  @Suppress("UNCHECKED_CAST")
  override fun get(conValues: ContentValues): T = conValues.getAsString(name) as T

  override fun set(conValues: ContentValues, value: T) = conValues.put(name, value)

  interface nonNull: IStringColumn<String> {
    override val createProperties: String
      get() = "NOT NULL"

    interface unique: IStringColumn<String> {
      override val createProperties: String
        get() = "UNIQUE " + super.createProperties
    }
  }
}


/**
 * IMPLEMENTATIONS
 */

abstract class Column<T>(override val name: String): IColumn<T> {
  abstract class nonNull<T: Any>(override val name: String): Column<T>(name) {
    override val createProperties = "NOT NULL"
  }
}

class DoubleColumn(override val name: String): IDoubleColumn<Double?> {
  companion object {
    operator fun getValue(table: SqlTable, property: KProperty<*>) = DoubleColumn(property.name)
  }
}

class IntColumn(override val name: String): IIntColumn<Int?> {
  companion object {
    operator fun getValue(table: SqlTable, property: KProperty<*>) = IntColumn(property.name)
  }

  open class nonNull(override val name: String): IIntColumn.nonNull
}

class PrimaryKeyColumn(override val name: String): IntColumn.nonNull("") {
  companion object {
    operator fun getValue(table: SqlTable, property: KProperty<*>) = PrimaryKeyColumn(property.name)
  }
}

class StringColumn(override val name: String): IStringColumn<String?> {
  companion object {
    operator fun getValue(table: SqlTable, property: KProperty<*>) = StringColumn(property.name)
  }

  open class nonNull(override val name: String): IStringColumn.nonNull {
    companion object {
      operator fun getValue(table: SqlTable, property: KProperty<*>) = nonNull(property.name)
    }

    open class unique(override val name: String): IStringColumn.nonNull.unique {
      companion object {
        operator fun getValue(table: SqlTable, property: KProperty<*>) = unique(property.name)
      }
    }
  }
}
