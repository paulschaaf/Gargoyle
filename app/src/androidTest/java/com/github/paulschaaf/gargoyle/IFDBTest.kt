package com.github.paulschaaf.gargoyle

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.github.paulschaaf.gargoyle.ifdb.IFDBFeedReader

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Created by pschaaf on 9/3/17.
 */

@RunWith(AndroidJUnit4::class)
class IFDBTest {
  val author = "Marc Blank and Dave Lebling"
  val averageRating = 3.7547
  val description = "Many strange tales have been told of the fabulous treasure, exotic creatures, and diabolical puzzles in the Great Underground Empire. As an aspiring adventurer, you will undoubtedly want to locate these treasures and deposit them in your trophy case.  [--blurb from The Z-Files Catalogue]"
  val firstPublished = "1980"
  val forgiveness = "Cruel"
  val genre = "Zorkian/Cave crawl"
  val collection = ""
  val headline = ""
  val ifId = "ZCODE-88-840726"
  val id = 0L
  val language = "English, Castilian (en, es)"
  val link = "http://ifdb.tads.org/viewgame?id=0dbnusxunq7fw5ro"
  val lookedUp = ""
  val path = ""
  val series = "Zork"
  val seriesNumber = 1
  val ratingCountAvg = 159
  val ratingCountTotal = 159
  val starRating = 4
  val title = "Zork I"

  val xmlResult = """
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
      <tuid>0dbnusxunq7fw5ro</tuid>
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

  @Test
  fun readFrom() {
    val ifID = "0dbnusxunq7fw5ro"
    var stream = xmlResult.byteInputStream()
    var story = IFDBFeedReader.createStoryFrom(stream)

    assertEquals(author, story.author)
    assertEquals(averageRating, story.averageRating, 0.0)
    assertEquals(description, story.description)
    assertEquals(firstPublished, story.firstPublished)
    assertEquals(forgiveness, story.forgiveness)
    assertEquals(genre, story.genre)
    assertEquals(collection, story.collection)
    assertEquals(headline, story.headline)
    assertEquals(ifId, story.ifId)
    assertEquals(id, story.id)
    assertEquals(language, story.language)
    assertEquals(link, story.link)
    assertEquals(lookedUp, story.lookedUp)
    assertEquals(path, story.path)
    assertEquals(series, story.series)
    assertEquals(seriesNumber, story.seriesNumber)
    assertEquals(ratingCountAvg, story.ratingCountAvg)
    assertEquals(ratingCountTotal, story.ratingCountTotal)
    assertEquals(starRating, story.starRating)
    assertEquals(title, story.title)
  }

}


// http://ifdb.tads.org/viewgame?ifiction&id=0dbnusxunq7fw5ro

