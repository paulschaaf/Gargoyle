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
import com.github.paulschaaf.gargoyle.assertThat
import com.github.paulschaaf.gargoyle.ifdb.IFDBXmlParser
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IFDBTest {
  // http://ifdb.tads.org/viewgame?id=0dbnusxunq7fw5ro&ifiction
  val baseURL = "http://ifdb.tads.org"

  @Test
  fun testAllSamples() {
    val errors = mutableListOf<Exception>()
    TestStoryXml.SampleCreators.values().forEach {
      println("Checking XML for ${it.name}")
      try {
        assertXMLMatchesStory(it.create())
      }
      catch (ex: Exception) {
        errors.add(ex)
      }
    }
  }

  @Test
  fun handleSpecialCharacterFields() {
    val newAuthor = "©2017, Rosencrantz & Guildenstern"
    val newDescription = "This's as \"complicated\" as it gets!"
    val alteredStory = TestStoryXml.SampleCreators.ZorkI.create().apply {
      title += " (Customized)"
      author = newAuthor
      description = newDescription
      averageRating = 1.0
    }

    assertThat(alteredStory.author)
      .describedAs("Did not successfully change the author. ")
      .isEqualTo(newAuthor)
    assertXMLMatchesStory(alteredStory)
  }

  @Test
  fun handleNullFields() = assertXMLMatchesStory(
      TestStoryXml.SampleCreators.ZorkI.create().apply {
        description = null
        seriesNumber = null
        starRating = null
      }
  )

  private fun assertXMLMatchesStory(storyXML: ITestStoryXml) {
    val story = IFDBXmlParser.createStoryFrom(storyXML.xmlString)
    assertThat(story).isDescribedBy(storyXML)
  }
}

