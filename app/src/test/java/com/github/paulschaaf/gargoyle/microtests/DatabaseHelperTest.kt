package com.github.paulschaaf.gargoyle.microtests

import com.github.paulschaaf.gargoyle.DatabaseHelper
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
  fun printCreateSQL() {
    assertEquals(
        "CREATE TABLE Story (_ID INTEGER PRIMARY KEY, IFID TEXT UNIQUE NOT NULL, Author TEXT, AverageRating DOUBLE, CoverArtURL TEXT, Description TEXT, FirstPublished TEXT, Forgiveness TEXT, Genre TEXT, Language TEXT, Link TEXT, LookedUp TEXT, Path TEXT, Series TEXT, SeriesNumber INTEGER, RatingCountAvg INTEGER, RatingCountTotal INTEGER, StarRating INTEGER, Title TEXT, TUID TEXT);",
        Story.Table.createSQL)
  }
}