package com.github.paulschaaf.gargoyle

import android.support.test.runner.AndroidJUnit4
import com.github.paulschaaf.gargoyle.ifdb.IFDBFeedReader
import com.github.paulschaaf.gargoyle.model.Story

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Created by pschaaf on 9/3/17.
 */

@RunWith(AndroidJUnit4::class)
class IFDBTest {
  inner class StoryXML(
      var author: String = "Marc Blank and Dave Lebling",
      var averageRating: Double? = 0.0,
      var description: String = "Many strange tales have been told of the fabulous treasure, exotic creatures, and diabolical puzzles in the Great Underground Empire. As an aspiring adventurer, you will undoubtedly want to locate these treasures and deposit them in your trophy case.  [--blurb from The Z-Files Catalogue]",
      var firstPublished: String = "1980",
      var forgiveness: String = "Cruel",
      var genre: String = "Zorkian/Cave crawl",
      var ifId: String = "ZCODE-88-840726",
      var id: String = "0dbnusxunq7fw5ro",
      var language: String = "English, Castilian (en, es)",
      var link: String = "http://ifdb.tads.org/viewgame?id=0dbnusxunq7fw5ro",
      var series: String = "Zork",
      var seriesNumber: Int? = 1,
      var ratingCountAvg: Int? = 159,
      var ratingCountTotal: Int? = 1789,
      var starRating: Int? = 4,
      var title: String = "Zork I"
  ) {

    val xml: String
      get() = """
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
      <title>${title}</title>
      <author>${author}</author>
      <language>${language}</language>
      <firstpublished>${firstPublished}</firstpublished>
      <genre>${genre}</genre>
      <description>${description}</description>
      <series>${series}</series>
      <seriesnumber>${seriesNumber}</seriesnumber>
      <forgiveness>${forgiveness}</forgiveness>
    </bibliographic>
    <contact>
    </contact>
    <ifdb xmlns="http://ifdb.tads.org/api/xmlns">
      <tuid>${id}</tuid>
      <link>${link}</link>
      <coverart>
        <url>http://ifdb.tads.org/viewgame?id=0dbnusxunq7fw5ro&amp;coverart</url>
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

  @Test
  fun readZorkI() {
    val storyXML = StoryXML()
    assertXMLMatchesStory(storyXML)
  }

  @Test
  fun handleNullFields() {
    val storyXML = StoryXML(
        author = "", averageRating = null, seriesNumber = null, starRating = null
    )
    assertXMLMatchesStory(storyXML)
  }

  private fun assertXMLMatchesStory(storyXML: IFDBTest.StoryXML) {
    val story = IFDBFeedReader.createStoryFrom(storyXML.xml.byteInputStream())
    assertEquals("checking 'author':", storyXML.author, story.author)
    assertEquals("checking 'averageRating':", storyXML.averageRating, story.averageRating)
    assertEquals("checking 'description':", storyXML.description, story.description)
    assertEquals("checking 'firstPublished':", storyXML.firstPublished, story.firstPublished)
    assertEquals("checking 'forgiveness':", storyXML.forgiveness, story.forgiveness)
    assertEquals("checking 'genre':", storyXML.genre, story.genre)
    assertEquals("checking 'ifId':", storyXML.ifId, story.ifId)
    assertEquals("checking 'id':", storyXML.id, story.id)
    assertEquals("checking 'language':", storyXML.language, story.language)
    assertEquals("checking 'link':", storyXML.link, story.link)
    assertNotNull("checking 'lookedUp':", story.lookedUp)
    assertEquals("checking 'series':", storyXML.series, story.series)
    assertEquals("checking 'seriesNumber':", storyXML.seriesNumber, story.seriesNumber)
    assertEquals("checking 'ratingCountAvg':", storyXML.ratingCountAvg, story.ratingCountAvg)
    assertEquals("checking 'ratingCountTotal':", storyXML.ratingCountTotal, story.ratingCountTotal)
    assertEquals("checking 'starRating':", storyXML.starRating, story.starRating)
    assertEquals("checking 'title':", storyXML.title, story.title)
  }
}

// http://ifdb.tads.org/viewgame?ifiction&id=0dbnusxunq7fw5ro

