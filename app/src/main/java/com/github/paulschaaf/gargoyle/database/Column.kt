package com.github.paulschaaf.gargoyle.database

import android.content.ContentValues
import com.github.paulschaaf.gargoyle.model.Story

interface Column<T> {
  val columnName: String
  val sqlDataType: String
  val sqlNewColumnProperties: String
    get() = ""

  val createSQL: String
    get() = "$columnName $sqlDataType $sqlNewColumnProperties".trim()

  operator fun get(story: Story): T? = get(story.contentValues)
  operator fun set(story: Story, value: T?) = set(story.contentValues, value)

  operator fun get(conValues: ContentValues): T?
  operator fun set(conValues: ContentValues, value: T?)

  operator fun get(map: Map<String, T?>): T? = map[columnName]
  operator fun set(map: MutableMap<String, T?>, value: T?) = map.put(columnName, value)
}

interface DoubleColumn: Column<Double> {
  override val sqlDataType
    get() = "DOUBLE"

  override fun get(conValues: ContentValues): Double? = conValues.getAsDouble(columnName)
  override fun set(conValues: ContentValues, value: Double?) = conValues.put(columnName, value)
}

interface IntColumn: Column<Int> {
  override val sqlDataType
    get() = "INTEGER"

  override fun get(conValues: ContentValues): Int? = conValues.getAsInteger(columnName)
  override fun set(conValues: ContentValues, value: Int?) = conValues.put(columnName, value)
}

interface StringColumn: Column<String> {
  override val sqlDataType
    get() = "TEXT"

  override fun get(conValues: ContentValues): String? = conValues.getAsString(columnName)
  override fun set(conValues: ContentValues, value: String?) = conValues.put(columnName, value)
}

