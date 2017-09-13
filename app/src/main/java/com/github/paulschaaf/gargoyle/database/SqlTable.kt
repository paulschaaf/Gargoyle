package com.github.paulschaaf.gargoyle.database

interface SqlTable {
  val columns: List<IColumn<*>>
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

