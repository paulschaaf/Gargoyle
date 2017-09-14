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
import com.github.paulschaaf.gargoyle.model.IStory
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

  inner class StoryXML(
      override var author: String? = "Marc Blank and Dave Lebling",
      override var averageRating: Double? = 0.0,
      override var description: String? = "Many strange tales have been told of the fabulous treasure, exotic creatures, and diabolical puzzles in the Great Underground Empire. As an aspiring adventurer, you will undoubtedly want to locate these treasures and deposit them in your trophy case.  [--blurb from The Z-Files Catalogue]",
      override var firstPublished: String? = "1980",
      override var forgiveness: String? = "Cruel",
      override var genre: String? = "Zorkian/Cave crawl",
      override var ifId: String = "ZCODE-88-840726",
      override var language: String? = "English, Castilian (en, es)",
      override var series: String? = "Zork",
      override var seriesNumber: Int? = 1,
      override var ratingCountAvg: Int? = 159,
      override var ratingCountTotal: Int? = 1789,
      override var starRating: Double? = 4.0,
      override var title: String? = "Zork I",
      override var tuid: String? = "0dbnusxunq7fw5ro",

      // derived properties
      override var link: String = "${baseURL}/viewgame?id=${tuid}",
      override var coverArtURL: String = link + "&coverart"
  ): IStory {

    override val lookedUp = null
    override val path = null
    override val id = 0

    fun String.toHtml(): String = Html.escapeHtml(this).replace("&nbsp;", " ")

    val xml: String
      get() {
        return """
      <ifindex version="1.0" xmlns="http://babel.ifarchive.org/protocol/iFiction/">
        <story>
          <colophon>
            <generator>ifdb.tads.org/viewgame</generator>
            <generatorversion>1</generatorversion>
            <originated>2017-03-28</originated>
          </colophon>
          <identification>
            <ifid>HUGO-25-49-53-02-06-99</ifid>
            <ifid>ZCODE-88-840726-A129</ifid>
            <ifid>ZCODE-52-871125</ifid>
            <ifid>${ifId}</ifid>
            <bafn>987</bafn>
            <format>hugo</format>
          </identification>
          <bibliographic>
            <title>${title?.toHtml()}</title>
            <author>${author?.toHtml()}</author>
            <language>${language?.toHtml()}</language>
            <firstpublished>${firstPublished?.toHtml()}</firstpublished>
            <genre>${genre?.toHtml()}</genre>
            <description>${description?.toHtml()}</description>
            <series>${series?.toHtml()}</series>
            <seriesnumber>${seriesNumber}</seriesnumber>
            <forgiveness>${forgiveness?.toHtml()}</forgiveness>
          </bibliographic>
          <contact/>
          <ifdb xmlns="${baseURL.toHtml()}/api/xmlns">
            <tuid>${tuid}</tuid>
            <link>${link.toHtml()}</link>
            <coverart>
              <url>${coverArtURL.toHtml()}</url>
            </coverart>
            <averageRating>${averageRating}</averageRating>
            <starRating>${starRating}</starRating>
            <ratingCountAvg>${ratingCountAvg}</ratingCountAvg>
            <ratingCountTot>${ratingCountTotal}</ratingCountTot>
          </ifdb>
        </story>
      </ifindex>
      """
      }
  }

  @Test
  fun checkZorkAuthor() {
    assertEquals("Marc Blank and Dave Lebling", SampleGameXML.ZorkI.author)
  }

  @Test
  fun readZorkI_2() {
    val storyXML = SampleGameXML.ZorkI
    assertXMLMatchesStory(storyXML)
  }

  @Test
  fun readZorkI() {
    val storyXML = StoryXML()
//    assertXMLMatchesStory(storyXML)
  }

  @Test
  fun handleSpecialCharacterFields() {
    val storyXML = StoryXML(
        author = "©2017, Rosencrantz & Guildenstern",
        description = "This's as \"complicated\" <br> as <p/> <span></span>it gets<!>"
    )
//    assertXMLMatchesStory(storyXML)
  }

  @Test
  fun handleNullFields() {
    val storyXML = StoryXML(
        description = null, averageRating = null, seriesNumber = null, starRating = null
    )
//    assertXMLMatchesStory(storyXML)
  }

  private fun assertXMLMatchesStory(gameXML: SampleGameXML) {
    val story = IFDBFeedReader.createStoryFrom(gameXML.xml.byteInputStream())
    assertEquals("checking 'author':", gameXML.author, story.author)
    assertEquals("checking 'averageRating':", gameXML.averageRating, story.averageRating)
    assertEquals("checking 'coverArtURL':", gameXML.coverArtURL, story.coverArtURL)
    assertEquals("checking 'description':", gameXML.description, story.description)
    assertEquals("checking 'firstPublished':", gameXML.firstPublished, story.firstPublished)
    assertEquals("checking 'forgiveness':", gameXML.forgiveness, story.forgiveness)
    assertEquals("checking 'genre':", gameXML.genre, story.genre)
    assertEquals("checking 'ifId':", gameXML.ifId, story.ifId)
    assertEquals("checking 'language':", gameXML.language, story.language)
    assertEquals("checking 'link':", gameXML.link, story.link)
    assertNotNull("checking 'lookedUp':", story.lookedUp)
    assertEquals("checking 'ratingCountAvg':", gameXML.ratingCountAvg, story.ratingCountAvg)
    assertEquals("checking 'ratingCountTotal':", gameXML.ratingCountTotal, story.ratingCountTotal)
    assertEquals("checking 'series':", gameXML.series, story.series)
    assertEquals("checking 'seriesNumber':", gameXML.seriesNumber, story.seriesNumber)
    assertEquals("checking 'starRating':", gameXML.starRating, story.starRating)
    assertEquals("checking 'title':", gameXML.title, story.title)
    assertEquals("checking 'tuid':", gameXML.tuid, story.tuid)
  }
}

