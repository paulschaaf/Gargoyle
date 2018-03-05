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

class StoryTest {
  var story = Story.create {
    author = "P.G. Schaaf"
    averageRating = 4.5
    description = "Your greatest adventure lies ahead! (Then left, down the stairs, and through the secon door on the right.)"
    firstPublished = "5/6/2017"
    forgiveness = "hard"
    genre = "Adventure"
    id = "31415"
    ifId = "ifid_zork_pi"
    language = "EN/US"
    link = "http://paulschaaf.com/"
    lookedUp = "9/2/2017"
    path = "/var/data/IntFic.dat"
    ratingCountAvg = 17
    ratingCountTotal = 137
    series = "Zork"
    seriesNumber = 5
    starRating = 5.0
    title = "Zork 3.14"
  }

  @Test
  fun readPropertiesFromStory() = with(StoryTable) {
    val properties = story.toContentValues()
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
}