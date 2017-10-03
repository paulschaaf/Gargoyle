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

import com.github.paulschaaf.gargoyle.model.IFDBStory

interface ITestStoryXml: IFDBStory {
  val url: String
  val xmlString: String

  val toHtml: Map<String, String>
    get() = mapOf(
        "&" to "&amp;",
        "\"" to "&quot;",
        "'" to "&apos;",
        "&lt;" to "<",
        "&gt;" to ">"
    )

  private fun String.escape() = toHtml.entries.fold(this) { acc, (code, escapedCode)->
    acc.replace(code, escapedCode)
  }

  private fun String.unescape() = toHtml.entries.fold(this) { acc, (code, escapedCode)->
    acc.replace(escapedCode, code)
  }

  operator fun get(tag: String): String? {
    var prefix = ""
    var suffix = ""
    if (tag == "coverart") {
      prefix = "<url>"
      suffix = "</url>"
    }
    val match = Regex("^.*<$tag>$prefix(.*)$suffix</$tag>.*\$").matchEntire(xmlString)
    val value = match?.groups?.get(1)?.value
    return if (value.isNullOrEmpty()) null else value!!.unescape()
  }

  fun with(tag: String, value: String): TestStoryXml {
    val (prefix, suffix) = if (tag == "coverart")
      "<url>" to "</url"
    else
      "" to ""

    val newXML = xmlString.replace(
        Regex("<$tag>$prefix[^<]+$suffix</$tag>"),
        "<$tag>${value.escape()}</$tag>"
    )
    return TestStoryXml("custom", newXML)
  }

  fun with(tag: String, value: Any?): TestStoryXml = with(tag, value?.toString() ?: "")
}

open class TestStoryXml(override val url: String, override val xmlString: String): ITestStoryXml {
  override val author = this["author"]
  override val averageRating = this["averageRating"]?.toDoubleOrNull()
  override var coverArtURL = this["coverart"]
  override val description = this["description"]
  override val firstPublished = this["firstpublished"]
  override val forgiveness = this["forgiveness"]
  override val genre = this["genre"]
  override val ifId = this["ifid"] ?: "-error-"
  override val language = this["language"]
  override val link = this["link"]?.replace("&amp;", "&")
  override val path = this["path"]
  override val series = this["series"]
  override val tuid = this["tuid"]
  override val title = this["title"]
  override val ratingCountAvg = this["ratingCountAvg"]?.toIntOrNull()
  override val ratingCountTotal = this["ratingCountTot"]?.toIntOrNull()
  override val seriesNumber = this["seriesnumber"]?.toIntOrNull()
  override val starRating = this["starRating"]?.toDoubleOrNull()

  enum class Samples(url: String, str: String): ITestStoryXml by TestStoryXml(url, str) {
    Bronze("http://ifdb.tads.org/viewgame?ifiction&id=9p8kh3im2j9h2881",
           """<?xml version="1.0" encoding="UTF-8"?><ifindex version="1.0" xmlns="http://babel.ifarchive.org/protocol/iFiction/"><story><colophon><generator>ifdb.tads.org/viewgame</generator><generatorversion>1</generatorversion><originated>2016-10-23</originated></colophon><identification><ifid>1810847C-0DC7-44D5-94EF-313A3E7AF257</ifid><bafn>2893</bafn><format>zcode</format></identification><bibliographic><title>Bronze</title><author>Emily Short</author><language>en</language><firstpublished>2006</firstpublished><genre>Fantasy</genre><description>When the seventh day comes and it is time for you to return to the castle in the forest, your sisters cling to your sleeves.</description><series>fractured fairy tales</series></bibliographic><contact><url>http://inform7.com/learn/eg/bronze/index.html</url></contact><ifdb xmlns="http://ifdb.tads.org/api/xmlns"><tuid>9p8kh3im2j9h2881</tuid><link>http://ifdb.tads.org/viewgame?id=9p8kh3im2j9h2881</link><coverart><url>http://ifdb.tads.org/viewgame?id=9p8kh3im2j9h2881&amp;coverart</url></coverart><averageRating>4.2917</averageRating><starRating>4.5</starRating><ratingCountAvg>192</ratingCountAvg><ratingCountTot>192</ratingCountTot></ifdb></story></ifindex>"""
          ),

    LostPig("http://ifdb.tads.org/viewgame?ifiction&id=mohwfk47yjzii14w",
            """<?xml version="1.0" encoding="UTF-8"?><ifindex version="1.0" xmlns="http://babel.ifarchive.org/protocol/iFiction/"><story><colophon><generator>ifdb.tads.org/viewgame</generator><generatorversion>1</generatorversion><originated>2013-09-21</originated></colophon><identification><ifid>ZCODE-1-070917-994E</ifid><ifid>ZCODE-2-080406-A377</ifid><bafn>3035</bafn><format>zcode</format></identification><bibliographic><title>Lost Pig</title><author>Admiral Jota</author><language>en</language><firstpublished>2007-09-30</firstpublished><genre>Fantasy, Humor</genre><forgiveness>Polite</forgiveness></bibliographic><contact><url>http://www.grunk.org/lostpig</url></contact><ifdb xmlns="http://ifdb.tads.org/api/xmlns"><tuid>mohwfk47yjzii14w</tuid><link>http://ifdb.tads.org/viewgame?id=mohwfk47yjzii14w</link><coverart><url>http://ifdb.tads.org/viewgame?id=mohwfk47yjzii14w&amp;coverart</url></coverart><averageRating>4.4661</averageRating><starRating>4.5</starRating><ratingCountAvg>339</ratingCountAvg><ratingCountTot>340</ratingCountTot></ifdb></story></ifindex>"""
           ),

    SpellBreaker("http://ifdb.tads.org/viewgame?ifiction&id=wqsmrahzozosu3r",
                 """<?xml version="1.0" encoding="UTF-8"?><ifindex version="1.0" xmlns="http://babel.ifarchive.org/protocol/iFiction/"><story><colophon><generator>ifdb.tads.org/viewgame</generator><generatorversion>1</generatorversion><originated>2013-04-07</originated></colophon><identification><ifid>ZCODE-87-860904</ifid><ifid>ZCODE-63-850916</ifid></identification><bibliographic><title>Spellbreaker</title><author>Dave Lebling</author><language>en</language><firstpublished>1985</firstpublished><genre>Fantasy/Zorkian</genre><description>Spellbreaker, the riveting conclusion to the Enchanter trilogy, explores the mysterious underpinnings of the Zorkian universe. A world founded on sorcery suddenly finds its magic failing, and only you, leader of the Circle of Enchanters, can uncover and destroy the cause of this paralyzing chaos.  <br/>The very core of your civilization is under siege, and only a perilous journey through the black foundation of magic itself will yield a chance for survival.  <br/>And although your triumph over this unknown Evil is uncertain, you must embark without hesitation and prove yourself the worthiest mage in the land.  <br/>Difficulty: Expert</description><series>The Enchanter series</series><seriesnumber>3</seriesnumber><forgiveness>Cruel</forgiveness></bibliographic><contact></contact><ifdb xmlns="http://ifdb.tads.org/api/xmlns"><tuid>wqsmrahzozosu3r</tuid><link>http://ifdb.tads.org/viewgame?id=wqsmrahzozosu3r</link><coverart><url>http://ifdb.tads.org/viewgame?id=wqsmrahzozosu3r&amp;coverart</url></coverart><averageRating>4.2857</averageRating><starRating>4.5</starRating><ratingCountAvg>42</ratingCountAvg><ratingCountTot>42</ratingCountTot></ifdb></story></ifindex>"""
                ),

    Violet("http://ifdb.tads.org/viewgame?ifiction&id=mohwfk47yjzii14w",
           """<?xml version="1.0" encoding="UTF-8"?><ifindex version="1.0" xmlns="http://babel.ifarchive.org/protocol/iFiction/"><story><colophon><generator>ifdb.tads.org/viewgame</generator><generatorversion>1</generatorversion><originated>2013-09-21</originated></colophon><identification><ifid>ZCODE-1-070917-994E</ifid><ifid>ZCODE-2-080406-A377</ifid><bafn>3035</bafn><format>zcode</format></identification><bibliographic><title>Lost Pig</title><author>Admiral Jota</author><language>en</language><firstpublished>2007-09-30</firstpublished><genre>Fantasy, Humor</genre><forgiveness>Polite</forgiveness></bibliographic><contact><url>http://www.grunk.org/lostpig</url></contact><ifdb xmlns="http://ifdb.tads.org/api/xmlns"><tuid>mohwfk47yjzii14w</tuid><link>http://ifdb.tads.org/viewgame?id=mohwfk47yjzii14w</link><coverart><url>http://ifdb.tads.org/viewgame?id=mohwfk47yjzii14w&amp;coverart</url></coverart><averageRating>4.4661</averageRating><starRating>4.5</starRating><ratingCountAvg>339</ratingCountAvg><ratingCountTot>340</ratingCountTot></ifdb></story></ifindex>"""
          ),

    ZorkI("http://ifdb.tads.org/viewgame?ifiction&id=0dbnusxunq7fw5ro",
          """<?xml version="1.0" encoding="UTF-8"?><ifindex version="1.0" xmlns="http://babel.ifarchive.org/protocol/iFiction/"><story><colophon><generator>ifdb.tads.org/viewgame</generator><generatorversion>1</generatorversion><originated>2017-03-28</originated></colophon><identification><ifid>HUGO-25-49-53-02-06-99</ifid><ifid>ZCODE-88-840726-A129</ifid><ifid>ZCODE-52-871125</ifid><ifid>ZCODE-88-840726</ifid><bafn>987</bafn><format>hugo</format></identification><bibliographic><title>Zork I</title><author>Marc Blank and Dave Lebling</author><language>English, Castilian (en, es)</language><firstpublished>1980</firstpublished><genre>Zorkian/Cave crawl</genre><description>Many strange tales have been told of the fabulous treasure, exotic creatures, and diabolical puzzles in the Great Underground Empire. As an aspiring adventurer, you will undoubtedly want to locate these treasures and deposit them in your trophy case.  [--blurb from The Z-Files Catalogue]</description><series>Zork</series><seriesnumber>1</seriesnumber><forgiveness>Cruel</forgiveness></bibliographic><contact></contact><ifdb xmlns="http://ifdb.tads.org/api/xmlns"><tuid>0dbnusxunq7fw5ro</tuid><link>http://ifdb.tads.org/viewgame?id=0dbnusxunq7fw5ro</link><coverart><url>http://ifdb.tads.org/viewgame?id=0dbnusxunq7fw5ro&amp;coverart</url></coverart><averageRating>3.7547</averageRating><starRating>4</starRating><ratingCountAvg>159</ratingCountAvg><ratingCountTot>159</ratingCountTot></ifdb></story></ifindex>"""
         )
  }
}