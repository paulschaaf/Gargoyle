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

package com.github.paulschaaf.gargoyle.microtests

import com.github.paulschaaf.gargoyle.database.StoryTable
import com.github.paulschaaf.gargoyle.model.IFDBStory
import org.junit.Assert.assertTrue
import org.junit.Test

class DatabaseHelperTest: MockContentValuesTestBase() {
  val fieldsByName = IFDBStory::class.java.methods.associateBy {
    val name = it.name.removePrefix("get")
    name[0].toLowerCase() + name.substring(1)
  }

  val tableDef = StoryTable.createSQL
      .split(",", "(", ")")
      .map { str -> str.trim() }
      .toSet()

  @Test
  fun createSQLContainsAllFields() {
    val columns = StoryTable.columns
    assertTrue("StoryTable should have at least one column!", columns.isNotEmpty())

    val unmatchedFieldNames = fieldsByName.keys.toMutableSet()
    val unmatchedColumnNames = mutableSetOf<String>()

    columns.forEach { (columnName, column) ->
      val sql = column.createSQL
      assertTrue(
          "Could not find the column definition '$sql' in this createSQL: $tableDef",
          tableDef.any { it.matches(sql.toRegex()) }
      )
      if (columnName == "id" || columnName == "lookedUp")
      // ignore it
      else if (unmatchedFieldNames.remove(columnName))
        println("matched " + columnName)
      else unmatchedColumnNames.add(columnName)
    }
    val unmatcheFieldsString = unmatchedFieldNames
        .joinToString(", ", prefix = "The following fields do not have a corresponding column: ")
    assertTrue(unmatcheFieldsString, unmatchedFieldNames.isEmpty())

    val unmatchedColumnsString = unmatchedColumnNames
        .joinToString(", ", prefix = "The following columns do not have a corresponding field: ")
    assertTrue(unmatchedColumnsString, unmatchedColumnNames.isEmpty())
  }
}