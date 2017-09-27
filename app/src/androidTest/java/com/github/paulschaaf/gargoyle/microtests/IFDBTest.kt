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
import com.github.paulschaaf.gargoyle.model.IStory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.reflect.full.memberProperties

/**
 * Created by pschaaf on 9/3/17.
 */

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

  private fun assertXMLMatchesStory(gameXML: SampleGameXML) {
    val story = IFDBFeedReader.createStoryFrom(gameXML.xmlString.byteInputStream())
    story.assertIsDescribedBy(gameXML)
  }

  fun IStory.assertIsDescribedBy(other: IStory) {
    IStory::class.memberProperties.forEach {
      val fieldName = it.name
      print("checking property: " + fieldName)
      val expected = it(other)
      val actual = it(this)
      val checkingMessage = "bad value in '$fieldName'"
      when (expected) {
        null      -> assertNull(checkingMessage, actual)
        is Double -> assertEquals(checkingMessage, expected, actual as Double, 0.0)
        else      -> assertEquals(checkingMessage, expected, actual)
      }
    }
  }
}

