package com.github.paulschaaf.gargoyle.database

interface Table {
  val columns: List<Column<*>>
  val name: String

  val createSQL: String
    get() = columns
      .map { it.createSQL }
      .joinToString(
          prefix = "CREATE TABLE $name (",
          separator = ", ",
          postfix = ");"
      )
}

