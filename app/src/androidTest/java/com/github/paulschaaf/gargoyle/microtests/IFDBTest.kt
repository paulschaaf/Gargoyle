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

import android.support.test.runner.AndroidJUnit4
import com.github.paulschaaf.gargoyle.ifdb.IFDBFeedReader
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IFDBTest {
  // http://ifdb.tads.org/viewgame?id=0dbnusxunq7fw5ro&ifiction
  val baseURL = "http://ifdb.tads.org"

  @Test
  fun readZorkI() {
    val storyXML = SampleGameXML.ZorkI
    assertXMLMatchesStory(storyXML)
  }

  @Test
  fun handleSpecialCharacterFields() {
    val author = "©2017, Rosencrantz & Guildenstern"
    val description = "This's as \"complicated\" as it gets!"
    val alteredGame = SampleGameXML.ZorkI
      .set("author", author)
      .set("description", description)
      .set("averageRating", 1.0)

    assertEquals("Did not successfully change the author. ", author, alteredGame.author)
    assertXMLMatchesStory(alteredGame)
  }

  @Test
  fun handleNullFields() {
    val alteredGame = SampleGameXML.ZorkI
      .set("description", null)
      .set("seriesNumber", null)
      .set("starRating", null)

    assertXMLMatchesStory(alteredGame)
  }

  fun <T> assertFieldEquals(fieldName: String, expected: T, actual: T) {
    when {
      expected == null   -> assertNull("checking '$fieldName':", actual)
      expected is Double -> assertEquals("checking '$fieldName':",
                                         expected,
                                         actual as Double, // explicit cast to keep the compiler happy
                                         0.0
      )
      else               -> assertEquals("checking '$fieldName':",
                                         expected,
                                         actual
      )
    }
  }

  private fun assertXMLMatchesStory(gameXML: SampleGameXML) {
    val story = IFDBFeedReader.createStoryFrom(gameXML.xmlString.byteInputStream())
    assertFieldEquals("ifId", gameXML.ifId, story.ifId)
    assertFieldEquals("author", gameXML.author, story.author)
    assertFieldEquals("averageRating", gameXML.averageRating, story.averageRating)
    assertFieldEquals("coverArtURL", gameXML.coverArtURL, story.coverArtURL)
    assertFieldEquals("description", gameXML.description, story.description)
    assertFieldEquals("firstPublished", gameXML.firstPublished, story.firstPublished)
    assertFieldEquals("forgiveness", gameXML.forgiveness, story.forgiveness)
    assertFieldEquals("genre", gameXML.genre, story.genre)
    assertFieldEquals("language", gameXML.language, story.language)
    assertFieldEquals("link", gameXML.link, story.link)
    assertNotNull("lookedUp", story.lookedUp)
    assertFieldEquals("ratingCountAvg", gameXML.ratingCountAvg, story.ratingCountAvg)
    assertFieldEquals("ratingCountTotal", gameXML.ratingCountTotal, story.ratingCountTotal)
    assertFieldEquals("series", gameXML.series, story.series)
    assertFieldEquals("seriesNumber", gameXML.seriesNumber, story.seriesNumber)
    assertFieldEquals("starRating", gameXML.starRating, story.starRating)
    assertFieldEquals("title", gameXML.title, story.title)
    assertFieldEquals("tuid", gameXML.tuid, story.tuid)
  }
}

