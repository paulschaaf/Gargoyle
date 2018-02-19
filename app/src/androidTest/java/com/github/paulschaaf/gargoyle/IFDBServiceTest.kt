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

package com.github.paulschaaf.gargoyle

import android.support.test.runner.AndroidJUnit4
import com.github.paulschaaf.gargoyle.ifdb.IFDBService
<<<<<<< HEAD:app/src/androidTest/java/com/github/paulschaaf/gargoyle/IFDBServiceTest.kt
=======
import com.github.paulschaaf.gargoyle.microtests.TestStoryXml
>>>>>>> 2e968d9c1111882942d295482dff019dfc019f43:app/src/androidTest/java/com/github/paulschaaf/gargoyle/IFDBServiceTest.kt
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IFDBServiceTest {
  @Test
<<<<<<< HEAD:app/src/androidTest/java/com/github/paulschaaf/gargoyle/IFDBServiceTest.kt
  fun testBronze() = testStory(StoryXMLTest.Bronze)

  @Test
  fun testLostPig() = testStory(StoryXMLTest.LostPig)

  @Test
  fun testSpellBreaker() = testStory(StoryXMLTest.SpellBreaker)

  @Test
  fun testViolet() = testStory(StoryXMLTest.Violet)

  @Test
  fun testZorkI() = testStory(StoryXMLTest.ZorkI)

  private fun testStory(testStoryXmlBuilder: StoryXMLTest) {
=======
  fun testBronze() = testStory(TestStoryXml.Bronze)

  @Test
  fun testLostPig() = testStory(TestStoryXml.LostPig)

  @Test
  fun testSpellBreaker() = testStory(TestStoryXml.SpellBreaker)

  @Test
  fun testViolet() = testStory(TestStoryXml.Violet)

  @Test
  fun testZorkI() = testStory(TestStoryXml.ZorkI)

  private fun testStory(testStoryXmlBuilder: TestStoryXml) {
>>>>>>> 2e968d9c1111882942d295482dff019dfc019f43:app/src/androidTest/java/com/github/paulschaaf/gargoyle/IFDBServiceTest.kt
    val tuid = testStoryXmlBuilder["tuid"]!!
    val ifIDLookup = IFDBService(tuid)
    val expected = testStoryXmlBuilder.xmlString
      .replace(Regex(">\\s*", RegexOption.DOT_MATCHES_ALL), ">")
      .removeRatingRegion()
    val actual = ifIDLookup.processStream { stream-> stream.reader().readText().removeRatingRegion() }
    assertThat(actual)
      .describedAs("Looked up story #$tuid")
      .isEqualTo(expected)
  }

  // ignore the numbers in the ratings region: they've likely changed from our cached test data
  private fun String.removeRatingRegion() = replace("<averageRating.*ratingCountTot>".toRegex(), "")
}