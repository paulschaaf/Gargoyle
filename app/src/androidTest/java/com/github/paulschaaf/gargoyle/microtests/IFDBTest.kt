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
import android.text.Html
import com.github.paulschaaf.gargoyle.ifdb.IFDBFeedReader
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

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
    val description = "This&apos;s as &quot;complicated&quot; &lt;br&gt; as &lt;p/&gt; &lt;span&gt;&lt;/span&gt;it gets&lt;!&gt;"
    val alteredGame = (SampleGameXML.ZorkI
        .set("author", author)
        .set("description", description)
//      .set("averageRating", 1.0)
        )
    assertEquals("Did not successfully change the author. ", Html.escapeHtml(author), alteredGame.author)
    assertXMLMatchesStory(alteredGame)
  }

  @Test
  fun handleNullFields() {
    val alteredGame = (SampleGameXML.ZorkI
        .set("averageRating", null)
        .set("description", null)
        .set("seriesNumber", null)
        .set("starRating", null)
        )
    assertXMLMatchesStory(alteredGame)
  }

  private fun assertXMLMatchesStory(gameXML: SampleGameXML) {
    val story = IFDBFeedReader.createStoryFrom(gameXML.xml.byteInputStream())
    assertEquals("checking 'ifId':", gameXML.ifId, story.ifId)

    assertEquals("checking 'author':", gameXML.author!!, story.author!!)
//    assertEquals("checking 'averageRating':", gameXML.averageRating, story.averageRating, 0.0)
    assertEquals("checking 'coverArtURL':", gameXML.coverArtURL!!, story.coverArtURL!!)
    assertEquals("checking 'description':", gameXML.description!!, story.description!!)
    assertEquals("checking 'firstPublished':", gameXML.firstPublished!!, story.firstPublished!!)
    assertEquals("checking 'forgiveness':", gameXML.forgiveness!!, story.forgiveness!!)
    assertEquals("checking 'genre':", gameXML.genre!!, story.genre!!)
    assertEquals("checking 'language':", gameXML.language!!, story.language!!)
    assertEquals("checking 'link':", gameXML.link!!, story.link!!)
    assertNotNull("checking 'lookedUp':", story.lookedUp!!)
    assertEquals("checking 'ratingCountAvg':", gameXML.ratingCountAvg!!, story.ratingCountAvg!!)
    assertEquals("checking 'ratingCountTotal':", gameXML.ratingCountTotal!!, story.ratingCountTotal!!)
    assertEquals("checking 'series':", gameXML.series!!, story.series!!)
    assertEquals("checking 'seriesNumber':", gameXML.seriesNumber!!, story.seriesNumber!!)
    assertEquals("checking 'starRating':", gameXML.starRating!!, story.starRating!!, 0.0)
    assertEquals("checking 'title':", gameXML.title!!, story.title!!)
    assertEquals("checking 'tuid':", gameXML.tuid!!, story.tuid!!)
  }
}

