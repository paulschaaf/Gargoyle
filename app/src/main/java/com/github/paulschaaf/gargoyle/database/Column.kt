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
import org.jetbrains.anko.db.INTEGER
import org.jetbrains.anko.db.NOT_NULL
import org.jetbrains.anko.db.PRIMARY_KEY
import org.jetbrains.anko.db.REAL
import org.jetbrains.anko.db.SqlType
import org.jetbrains.anko.db.SqlTypeModifier
import org.jetbrains.anko.db.TEXT
import org.jetbrains.anko.db.UNIQUE
import read
import set
import kotlin.reflect.KProperty

interface IColumn<T> {
  val name: String
  val table: ISqlTable
  val klass: Class<T>

  val sqlDataType: SqlType
    get() = when (klass) {
      java.lang.Double::class.java -> REAL
      java.lang.Float::class.java  -> REAL
      Int::class.java              -> INTEGER
      else                         -> TEXT
    }

  val typeModifiers: List<SqlTypeModifier>
    get() = listOf()

  val createSQL: Pair<String, SqlType>
    get() = name to (typeModifiers.fold(sqlDataType) { sql: SqlType, mod: SqlTypeModifier-> sql + mod })

  fun get(conValues: ContentValues): T = conValues.read(name, klass)

  fun set(conValues: ContentValues, value: T) = conValues.set(name, value)
}

open class Column<T>(final override val table: ISqlTable, override val name: String, override val klass: Class<T>):
    IColumn<T> {
  companion object {
    // save and extract the generic type parameter
    inline operator fun <reified T> invoke(table: ISqlTable, name: String) =
        Column(table, name, T::class.java)
  }

  init {
    table.addColumn(this)
  }
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

  open class NonNull(table: ISqlTable, name: String): IColumn<Int> by Column(table, name) {
    companion object {
      operator fun getValue(table: ISqlTable, property: KProperty<*>) =
          NonNull(table, property.name)
    }

    override val typeModifiers = super.typeModifiers + NOT_NULL
  }
}

class StringColumn(table: ISqlTable, name: String): IColumn<String?> by Column(table, name) {
  companion object {
    operator fun getValue(table: ISqlTable, property: KProperty<*>)
        = StringColumn(table, property.name)
  }

  override fun get(conValues: ContentValues): String? = super.get(conValues)?.trim()

  override fun set(conValues: ContentValues, value: String?) = super.set(conValues, value?.trim())

  open class NonNull(table: ISqlTable, name: String): IColumn<String> by Column(table, name) {
    companion object {
      operator fun getValue(table: ISqlTable, property: KProperty<*>) =
          NonNull(table, property.name)
    }

    override val typeModifiers = super.typeModifiers + NOT_NULL

    open class Unique(table: ISqlTable, name: String): NonNull(table, name) {
      companion object {
        operator fun getValue(table: ISqlTable, property: KProperty<*>) =
            Unique(table, property.name)
      }

      override val typeModifiers = super.typeModifiers + UNIQUE
    }
  }
}

class PrimaryKeyColumn(table: ISqlTable, name: String): StringColumn.NonNull(table, name) {
  companion object {
    operator fun getValue(table: ISqlTable, property: KProperty<*>) =
        PrimaryKeyColumn(table, property.name)
  }

  override val typeModifiers = super.typeModifiers + PRIMARY_KEY
}
