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

class DatabaseHelperTest: MockContentValuesTestBase() {
//  private val fieldNames = IFDBStory::class.memberProperties.map { it.name }
//
//  val tableDef = StoryTable.createSQL
//    .split("[,)(]".toRegex())
//    .map { str-> str.trim() }
//    .toSet()
//
//  @Test
//  fun createSQLContainsAllFields() {
//    val columns = StoryTable.columns
//
//    // sanity check
//    assertThat(columns)
//      .describedAs("StoryTable should have at least one column!")
//      .isNotEmpty
//
//    val unmatchedFieldNames = fieldNames.toMutableSet()
//    val unmatchedColumnNames = mutableSetOf<String>()
//
//    columns.forEach { (columnName, column)->
//      assertThat(tableDef)
//        .describedAs("CreateSQL is missing a column definition!")
//        .contains(column.createSQL)
//
//      if (columnName == "id" || columnName == "lookedUp")
//      // ignore it -- these db fields aren't part of the fetched IFDB record
//      else if (unmatchedFieldNames.remove(columnName))
//        println("matched " + columnName)
//      else unmatchedColumnNames.add(columnName)
//    }
//
//    assertThat(unmatchedFieldNames)
//      .describedAs("Fields with no corresponding column")
//      .isEmpty()
//
//    assertThat(unmatchedColumnNames)
//      .describedAs("Columns with no corresponding field")
//      .isEmpty()
//  }
}