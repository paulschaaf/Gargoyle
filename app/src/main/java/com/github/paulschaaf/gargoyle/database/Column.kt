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
import read
import set
import kotlin.reflect.KProperty

interface IColumn<T> {
  val name: String
  val klass: Class<T>

  val sqlDataType: String
    get() = when (klass) {
      java.lang.Double::class.java -> "DOUBLE"
      java.lang.Float::class.java  -> "FLOAT"
      Int::class.java              -> "INTEGER"
      else                         -> "TEXT"
    }

  val createProperties: String
    get() = ""

  val createSQL: String
    get() = "$name $sqlDataType $createProperties".trim()

  fun get(conValues: ContentValues): T = conValues.read(name, klass)

  fun set(conValues: ContentValues, value: T) = conValues.set(name, value)
}

open class Column<T>(override val name: String, override val klass: Class<T>): IColumn<T> {
  companion object {
    // save and extract the generic type parameter
    inline operator fun <reified T> invoke(name: String) = Column(name, T::class.java)
  }
}

class DoubleColumn(name: String): IColumn<Double?> by Column(name) {
  companion object {
    operator fun getValue(table: ISqlTable, property: KProperty<*>) = DoubleColumn(property.name)
  }
}

class IntColumn(name: String): IColumn<Int?> by Column(name) {
  companion object {
    operator fun getValue(table: ISqlTable, property: KProperty<*>) = IntColumn(property.name)
  }

  open class nonNull(name: String): IColumn<Int> by Column(name) {
    companion object {
      operator fun getValue(table: ISqlTable, property: KProperty<*>) = nonNull(property.name)
    }

    override val createProperties = "NOT NULL"
  }
}

class PrimaryKeyColumn(name: String): IntColumn.nonNull(name) {
  companion object {
    operator fun getValue(table: ISqlTable, property: KProperty<*>) = PrimaryKeyColumn(property.name)
  }
}

class StringColumn(name: String): IColumn<String?> by Column(name) {
  companion object {
    operator fun getValue(table: ISqlTable, property: KProperty<*>) = StringColumn(property.name)
  }

  open class nonNull(name: String): IColumn<String> by Column(name) {
    companion object {
      operator fun getValue(table: ISqlTable, property: KProperty<*>) = nonNull(property.name)
    }

    override val createProperties = "NOT NULL"

    open class unique(name: String): nonNull(name) {
      companion object {
        operator fun getValue(table: ISqlTable, property: KProperty<*>) = unique(property.name)
      }

      override val createProperties = "UNIQUE NOT NULL"
    }
  }
}

