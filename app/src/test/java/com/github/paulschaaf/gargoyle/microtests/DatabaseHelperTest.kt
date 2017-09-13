package com.github.paulschaaf.gargoyle.microtests

import com.github.paulschaaf.gargoyle.model.Story
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner
import org.junit.Assert.*

/**
 * Created by pschaaf on 9/8/17.
 */

@RunWith(MockitoJUnitRunner::class)
class DatabaseHelperTest {
  @Test
  fun createSQLContainsAllFields() {
    val tableDef = Story.table.createSQL
      .split(",", "(", ")")
      .map { str -> str.trim() }
      .toSet()

    Story.table.columns
      .map { col -> col.createSQL }
      .forEach { colDef -> assertTrue("Could not find the column definition '$colDef' in this createSQL: $tableDef", tableDef.contains(colDef)) }
  }
}