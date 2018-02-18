/*
 * Copyright © 2018 P.G. Schaaf <paul.schaaf@gmail.com>
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
import com.github.paulschaaf.gargoyle.model.Story
import org.junit.Assert.assertEquals
import org.junit.Test

class StoryTest: MockContentValuesTestBase() {
  override val properties = mapOf(
      "author" to "P.G. Schaaf",
      "averageRating" to 4.5,
      "description" to "Your greatest adventure lies ahead! (Then left, down the stairs, and through the second door on the right.)",
      "firstPublished" to "5/6/2017",
      "forgiveness" to "hard",
      "genre" to "Adventure",
      "id" to "31415",
      "ifId" to "ifid_zork_pi",
      "language" to "EN/US",
      "link" to "http://paulschaaf.com/",
      "lookedUp" to "9/2/2017",
      "path" to "/var/data/IntFic.dat",
      "ratingCountAvg" to 17,
      "ratingCountTotal" to 137,
      "series" to "Zork",
      "seriesNumber" to 5,
      "starRating" to 5.0,
      "title" to "Zork 3.14"
  )

  var story = Story(contentValues)

  @Test
  fun readPropertiesFromStory() = with(StoryTable) {
    assertEquals(properties["author"], story.author)
    assertEquals(properties["averageRating"], story.averageRating)
    assertEquals(properties["description"], story.description)
    assertEquals(properties["firstPublished"], story.firstPublished)
    assertEquals(properties["forgiveness"], story.forgiveness)
    assertEquals(properties["genre"], story.genre)
    assertEquals(properties["ifId"], story.ifId)
    assertEquals(properties["id"], story.id)
    assertEquals(properties["language"], story.language)
    assertEquals(properties["link"], story.link)
    assertEquals(properties["lookedUp"], story.lookedUp)
    assertEquals(properties["path"], story.path)
    assertEquals(properties["series"], story.series)
    assertEquals(properties["seriesNumber"], story.seriesNumber)
    assertEquals(properties["ratingCountAvg"], story.ratingCountAvg)
    assertEquals(properties["ratingCountTotal"], story.ratingCountTotal)
    assertEquals(properties["starRating"], story.starRating)
    assertEquals(properties["title"], story.title)
  }

//  @Test   // todo pschaaf 02/48/18 19:02: Would this test--were it to pass--prove anything?
//  fun testUpdateProperties() {
//    assertThat(story::author)
//      .isEqualTo(properties["author"] as String)
//
//    val newAuthor = "P.G. Schaaf, Jr., et. al."
//    `when`(contentValues.getAsString("author")).thenReturn(newAuthor)
//
//    assertThat(story::author)
//      .isEqualTo(newAuthor)
//  }
}