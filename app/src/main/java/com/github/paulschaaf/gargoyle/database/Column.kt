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
    get() = "TEXT"

  val createProperties: String
    get() = ""

  val createSQL: String
    get() = "$name $sqlDataType $createProperties".trim()

  @Suppress("UNCHECKED_CAST")
  operator fun get(conValues: ContentValues): T = conValues.get(name) as T

  fun set(conValues: ContentValues, value: T) = conValues.put(name, value as String)
}

open class Column<T>(override val name: String, val klass: Class<T>): IColumn<T> {
  companion object {
    // save and extract the generic type parameter
    inline operator fun <reified T> invoke(name: String) = Column(name, T::class.java)
  }

  override val sqlDataType = when (klass) {
    java.lang.Double::class.java -> "DOUBLE"
    java.lang.Float::class.java  -> "FLOAT"
    Int::class.java              -> "INTEGER"
    else                         -> "TEXT"
  }

  @Suppress("UNCHECKED_CAST")
  override fun get(conValues: ContentValues): T = when (klass) {
    Boolean::class.java          -> conValues.getAsBoolean(name) as T
    Byte::class.java             -> conValues.getAsByte(name) as T
    ByteArray::class.java        -> conValues.getAsByteArray(name) as T
    java.lang.Double::class.java -> conValues.getAsDouble(name) as T
    java.lang.Float::class.java  -> conValues.getAsFloat(name) as T
    Integer::class.java          -> conValues.getAsInteger(name) as T
    Long::class.java             -> conValues.getAsLong(name) as T
    Short::class.java            -> conValues.getAsShort(name) as T
    String::class.java           -> conValues.getAsString(name) as T
    else                         -> super.get(conValues)
  }

  override fun set(conValues: ContentValues, value: T) = when (value) {
    is Boolean?   -> conValues.put(name, value)
    is Byte?      -> conValues.put(name, value)
    is ByteArray? -> conValues.put(name, value)
    is Double?    -> conValues.put(name, value)
    is Float?     -> conValues.put(name, value)
    is Int?       -> conValues.put(name, value)
    is Long?      -> conValues.put(name, value)
    is Short?     -> conValues.put(name, value)
    is String?    -> conValues.put(name, value)
    else          -> super.set(conValues, value)
  }
}

class DoubleColumn(name: String): IColumn<Double?> by Column(name) {
  companion object {
    operator fun getValue(table: SqlTable, property: KProperty<*>) = DoubleColumn(property.name)
  }
}

class IntColumn(name: String): IColumn<Int?> by Column(name) {
  companion object {
    operator fun getValue(table: SqlTable, property: KProperty<*>) = IntColumn(property.name)
  }

  open class nonNull(name: String): IColumn<Int> by Column(name) {
    companion object {
      operator fun getValue(table: SqlTable, property: KProperty<*>) = nonNull(property.name)
    }

    override val createProperties = "NOT NULL"
  }
}

class PrimaryKeyColumn(name: String): IntColumn.nonNull(name) {
  companion object {
    operator fun getValue(table: SqlTable, property: KProperty<*>) = PrimaryKeyColumn(property.name)
  }
}

class StringColumn(name: String): IColumn<String?> by Column(name) {
  companion object {
    operator fun getValue(table: SqlTable, property: KProperty<*>) = StringColumn(property.name)
  }

  open class nonNull(name: String): IColumn<String> by Column(name) {
    companion object {
      operator fun getValue(table: SqlTable, property: KProperty<*>) = nonNull(property.name)
    }

    override val createProperties = "NOT NULL"

    open class unique(name: String): nonNull(name) {
      companion object {
        operator fun getValue(table: SqlTable, property: KProperty<*>) = unique(property.name)
      }

      override val createProperties = "UNIQUE NOT NULL"
    }
  }
}

