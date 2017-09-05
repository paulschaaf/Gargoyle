package com.github.paulschaaf.gargoyle.microtest

import com.github.paulschaaf.gargoyle.ifdb.IFDBFeedReader
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

/**
 * Created by pschaaf on 9/3/17.
 */

@RunWith(MockitoJUnitRunner::class)
class IFDBTest {
  val xmlResult = """
<ifindex xmlns="http://babel.ifarchive.org/protocol/iFiction/" version="1.0">
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
      <ifid>ZCODE-88-840726</ifid>
      <bafn>987</bafn>
      <format>hugo</format>
    </identification>
    <bibliographic>
      <title>Zork I</title>
      <author>Marc Blank and Dave Lebling</author>
      <language>English, Castilian (en, es)</language>
      <firstpublished>1980</firstpublished>
      <genre>Zorkian/Cave crawl</genre>
      <description>
        Many strange tales have been told of the fabulous treasure, exotic creatures, and diabolical puzzles in the Great Underground Empire. As an aspiring adventurer, you will undoubtedly want to locate these treasures and deposit them in your trophy case. [--blurb from The Z-Files Catalogue]
      </description>
      <series>Zork</series>
      <seriesnumber>1</seriesnumber>
      <forgiveness>Cruel</forgiveness>
    </bibliographic>
    <contact/>
    <ifdb xmlns="http://ifdb.tads.org/api/xmlns">
      <tuid>0dbnusxunq7fw5ro</tuid>
      <link>http://ifdb.tads.org/viewgame?id=0dbnusxunq7fw5ro</link>
      <coverart>
        <url>
          http://ifdb.tads.org/viewgame?id=0dbnusxunq7fw5ro&coverart
        </url>
      </coverart>
      <averageRating>3.7547</averageRating>
      <starRating>4</starRating>
      <ratingCountAvg>159</ratingCountAvg>
      <ratingCountTot>159</ratingCountTot>
    </ifdb>
  </story>
</ifindex>
"""

  @Test
  fun readFrom() {
    val ifID = "0dbnusxunq7fw5ro"
    var story = IFDBFeedReader.createStoryFrom(ifID)
  }

}


// http://ifdb.tads.org/viewgame?ifiction&id=0dbnusxunq7fw5ro

