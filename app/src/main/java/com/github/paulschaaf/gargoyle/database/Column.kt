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
  val table: ISqlTable?
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

open class Column<T>(final override val table: ISqlTable?, override val name: String, override val klass: Class<T>):
    IColumn<T> {
  companion object {
    // save and extract the generic type parameter
    inline operator fun <reified T> invoke(table: ISqlTable, name: String) =
        Column(table, name, T::class.java)

    inline operator fun <reified T> getValue(table: ISqlTable, property: KProperty<*>): Column<T> =
        Column<T>(table, property.name, T::class.java)
  }

  init {
    table?.addColumn(this)
  }

  inline operator fun <reified T> getValue(table: StoryTable, property: KProperty<*>) =
      Column<T>(table, property.name, T::class.java)
}

class DoubleColumn(table: ISqlTable, name: String): IColumn<Double?> by Column(table, name) {
  companion object {
    operator fun getValue(table: ISqlTable, property: KProperty<*>) =
        DoubleColumn(table, property.name)
  }
}

class IntColumn(table: ISqlTable, name: String): IColumn<Int?> by Column(table, name) {
  companion object {
    operator fun getValue(table: ISqlTable, property: KProperty<*>) =
        IntColumn(table, property.name)
  }

  open class nonNull(table: ISqlTable, name: String): IColumn<Int> by Column(table, name) {
    companion object {
      operator fun getValue(table: ISqlTable, property: KProperty<*>) =
          nonNull(table, property.name)
    }

    override val createProperties = "NOT NULL"
  }
}

class PrimaryKeyColumn(table: ISqlTable, name: String): IntColumn.nonNull(table, name) {
  companion object {
    operator fun getValue(table: ISqlTable, property: KProperty<*>) =
        PrimaryKeyColumn(table, property.name)
  }
}

class StringColumn(table: ISqlTable, name: String):
    IColumn<String?> by Column(table, name) {
  companion object {
    operator fun getValue(table: ISqlTable, property: KProperty<*>)
        = StringColumn(table, property.name)
  }

  open class nonNull(table: ISqlTable, name: String): IColumn<String> by Column(table, name) {
    companion object {
      operator fun getValue(table: ISqlTable, property: KProperty<*>) =
          nonNull(table, property.name)
    }

    override val createProperties = "NOT NULL"

    open class unique(table: ISqlTable, name: String): nonNull(table, name) {
      companion object {
        operator fun getValue(table: ISqlTable, property: KProperty<*>) =
            unique(table, property.name)
      }

      override val createProperties = "UNIQUE NOT NULL"
    }
  }
}

