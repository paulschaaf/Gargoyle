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

import org.junit.Test

//@RunWith(AndroidJUnit4::class)
class IFDBTest {
  // http://ifdb.tads.org/viewgame?id=0dbnusxunq7fw5ro&ifiction
  val baseURL = "http://ifdb.tads.org"

  @Test
  fun testBronze() = testStory(TestStoryXml.SampleBuilder.Bronze)

//  @Test
//  fun testLostPig() = testStory(TestStoryXml.SampleBuilder.LostPig)
//
//  @Test
//  fun testSpellBreaker() = testStory(TestStoryXml.SampleBuilder.SpellBreaker)
//
//  @Test
//  fun testViolet() = testStory(TestStoryXml.SampleBuilder.Violet)
//
//  @Test
//  fun testZorkI() = testStory(TestStoryXml.SampleBuilder.ZorkI)
//
//  @Test
//  fun testNullFields() = testStory(TestStoryXml.SampleBuilder.Zork_nullFields)
//
//  @Test
//  fun testSpecialChars() = testStory(TestStoryXml.SampleBuilder.ZorkI_specialChars)

  private fun testStory(testStoryXmlBuilder: TestStoryXml.SampleBuilder) =
      assertXMLMatchesStory(testStoryXmlBuilder.build())

  private fun assertXMLMatchesStory(storyXML: ITestStoryXml) {
  }
}