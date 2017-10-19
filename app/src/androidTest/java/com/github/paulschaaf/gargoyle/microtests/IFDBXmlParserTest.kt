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
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IFDBXmlParserTest {
  // http://ifdb.tads.org/viewgame?id=0dbnusxunq7fw5ro&ifiction
  val baseURL = "http://ifdb.tads.org"

  @Test
  fun sanityCheckEnsureExampleStoryFieldsAreNotEmpty() {
    val storyCreator = TestStoryXml.SampleCreators.Bronze
    val storyXML = storyCreator.create()

    assertThat(storyXML::ifId).isNotNull
    assertThat(storyXML::title).isNotNull
    assertThat(storyXML::author).isNotNull
    assertThat(storyXML::language).isNotNull
    assertThat(storyXML::genre).isNotNull
    assertThat(storyXML::description).isNotNull
    assertThat(storyXML::series).isNotNull
    assertThat(storyXML::url).isNotNull
    assertThat(storyXML::tuid).isNotNull
    assertThat(storyXML::link).isNotNull
    assertThat(storyXML::coverArtURL).isNotNull
    assertThat(storyXML::averageRating).isNotNull
    assertThat(storyXML::starRating).isNotNull
    assertThat(storyXML::ratingCountAvg).isNotNull
    assertThat(storyXML::ratingCountTotal).isNotNull
  }

  @Test
  fun testBronze() = testStory(TestStoryXml.SampleCreators.Bronze)

  @Test
  fun testLostPig() = testStory(TestStoryXml.SampleCreators.LostPig)

  @Test
  fun testSpellBreaker() = testStory(TestStoryXml.SampleCreators.SpellBreaker)

  @Test
  fun testViolet() = testStory(TestStoryXml.SampleCreators.Violet)

  @Test
  fun testZorkI() = testStory(TestStoryXml.SampleCreators.ZorkI)

  @Test
  fun testNullFields() = testStory(TestStoryXml.SampleCreators.Zork_nullFields)

  @Test
  fun testSpecialChars() = testStory(TestStoryXml.SampleCreators.ZorkI_specialChars)

  private fun testStory(story: TestStoryXml.SampleCreators) = assertXMLMatchesStory(story.create())

//  @Test
//  fun testAllSamples() {
//    val errors = mutableListOf<Exception>()
//    TestStoryXml.SampleCreators.values().forEach { storyCreator->
//      println("Checking XML for ${storyCreator.name}")
//      try {
//        assertXMLMatchesStory(storyCreator.create())
//      }
//      catch (ex: Exception) {
//        errors.add(ex)
//      }
//    }
//  }

  private fun assertXMLMatchesStory(storyXML: ITestStoryXml) {
    val story = IFDBXmlParser().parseIFXml(storyXML.xmlString.byteInputStream())
    assertThat(story).isDescribedBy(storyXML)
  }
}

