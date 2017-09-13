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

