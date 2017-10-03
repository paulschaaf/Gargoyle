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
import com.github.paulschaaf.gargoyle.model.IFDBStory
import org.fest.assertions.api.AbstractAssert
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.reflect.full.memberProperties

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
    val author = "©2017, Rosencrantz & Guildenstern"
    val description = "This's as \"complicated\" as it gets!"
    val alteredStory = TestStoryXml.SampleCreators.ZorkI.create().apply {
      this.title += " (Customized)"
      this.author = author
      this.description = description
      this.averageRating = 1.0
    }

    assertThat(alteredStory.author)
      .describedAs("Did not successfully change the author. ")
      .isEqualTo(author)
    assertXMLMatchesStory(alteredStory)
  }

  @Test
  fun handleNullFields() = assertXMLMatchesStory(
      TestStoryXml.SampleCreators.ZorkI.create().apply {
        this.description = null
        this.seriesNumber = null
        this.starRating = null
      }
  )

  private fun assertXMLMatchesStory(storyXML: ITestStoryXml) {
    val story = IFDBFeedReader.createStoryFrom(storyXML.xmlString)
    assertThat(story)
      .isDescribedBy(storyXML)
  }

  class IFDBStoryAssert internal constructor(actual: IFDBStory):
      AbstractAssert<IFDBStoryAssert, IFDBStory>(actual, IFDBStoryAssert::class.java) {

    fun isDescribedBy(other: IFDBStory) = IFDBStory::class.memberProperties.forEach { prop->
      assertThat(prop(other))
        .describedAs("actual value in '${prop.name}'")
        .isEqualTo(prop(other))

      print("verified property: " + prop.name)
    }
  }

  fun assertThat(story: IFDBStory) = IFDBStoryAssert(story)
}

