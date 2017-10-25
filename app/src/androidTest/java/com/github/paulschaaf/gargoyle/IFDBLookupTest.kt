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

package com.github.paulschaaf.gargoyle

import android.support.test.runner.AndroidJUnit4
import com.github.paulschaaf.gargoyle.ifdb.IFDB
import com.github.paulschaaf.gargoyle.microtests.ITestStoryXml
import com.github.paulschaaf.gargoyle.microtests.TestStoryXml
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IFDBLookupTest {
  @Test
  fun testBronze() = testStory(TestStoryXml.SampleBuilder.Bronze)

  @Test
  fun testLostPig() = testStory(TestStoryXml.SampleBuilder.LostPig)

  @Test
  fun testSpellBreaker() = testStory(TestStoryXml.SampleBuilder.SpellBreaker)

  @Test
  fun testViolet() = testStory(TestStoryXml.SampleBuilder.Violet)

  @Test
  fun testZorkI() = testStory(TestStoryXml.SampleBuilder.ZorkI)

  private fun testStory(testStoryXmlBuilder: TestStoryXml.SampleBuilder) =
      assertXMLMatchesStory(testStoryXmlBuilder.build())

  // ignore the numbers in the ratings region: they've likely changed from our cached test data
  private fun String.removeRatingRegion() = replace("<averageRating.*ratingCountTot>".toRegex(), "")

  private fun assertXMLMatchesStory(storyXML: ITestStoryXml) {
    val tuid = storyXML.tuid
    val ifIDLookup = IFDB.lookupIFID(tuid)

    val expected = storyXML.xmlString
      .replace(Regex(">\\s*", RegexOption.DOT_MATCHES_ALL), ">")
      .removeRatingRegion()
    val actual = ifIDLookup.processXml { it.reader().readText().removeRatingRegion() }

    assertThat(actual)
      .describedAs("Looked up story #$tuid at " + ifIDLookup.urlString)
      .isEqualTo(expected)
  }
}