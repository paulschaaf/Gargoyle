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
import org.fest.assertions.api.Assertions.assertThat
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
  fun readZorkI() = assertXMLMatchesStory(SampleGameXML.ZorkI)

  @Test
  fun handleSpecialCharacterFields() {
    val author = "©2017, Rosencrantz & Guildenstern"
    val description = "This's as \"complicated\" as it gets!"
    val alteredGame = SampleGameXML.ZorkI
      .with("author", author)
      .with("description", description)
      .with("averageRating", 1.0)

    assertThat(alteredGame.author)
      .describedAs("Did not successfully change the author. ")
      .isEqualTo(author)
    assertXMLMatchesStory(alteredGame)
  }

  @Test
  fun handleNullFields() = assertXMLMatchesStory(
      SampleGameXML.ZorkI
        .with("description", null)
        .with("seriesNumber", null)
        .with("starRating", null)
  )

  private fun assertXMLMatchesStory(gameXML: SampleGameXML) =
      IFDBFeedReader
        .createStoryFrom(gameXML.xmlString.byteInputStream())
        .assertIsDescribedBy(gameXML)

  fun IFDBStory.assertIsDescribedBy(other: IFDBStory) {
    IFDBStory::class.memberProperties.forEach { readProperty->
      assertThat(readProperty(other))
        .describedAs("bad value in '${readProperty.name}'")
        .isEqualTo(readProperty(this))

      print("verified property: " + readProperty.name)
    }
  }
}

