  private fun parseIFIndex(): Story {
    parser.next()
    parser.require(XmlPullParser.START_TAG, null, "ifindex");
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType != XmlPullParser.START_TAG) continue
      when (parser.name) {
        "story" -> parseStory()
        else    -> skip()
      }
    }
    return story
  }

  private fun parseStory() {
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType != XmlPullParser.START_TAG) continue
      when (parser.name) {
        "colophon"       -> readColophon()
        "identification" -> readIdentification()
        "bibliographic"  -> readBibliographic()
        "ifdb"           -> readIFDB()
        "contact"        -> skip()
        else             -> skip()
      }
    }
  }

  private fun readColophon() {
        "author"         -> story.author = getText()
        "language"       -> story.language = getText()
        "firstpublished" -> story.firstPublished = getText()
        "genre"          -> story.genre = getText()
        "description"    -> story.description = getText()
        "series"         -> story.series = getText()
        "seriesnumber"   -> story.seriesNumber = getText()?.toIntOrNull()
        "forgiveness"    -> story.forgiveness = getText()
        else             -> skip()
      }
    }
  }

  private fun readIFDB() {
    parser.require(XmlPullParser.START_TAG, null, "ifdb")
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType != XmlPullParser.START_TAG) continue
      when (parser.name) {
        "tuid"           -> story.id = getText()
        "link"           -> story.link = getText()
        "coverart"       -> readCoverArt()
        "averageRating"  -> story.averageRating = getText()?.toDoubleOrNull()
        "starRating"     -> story.starRating = getText()?.toIntOrNull()
        "ratingCountAvg" -> story.ratingCountAvg = getText()?.toIntOrNull()
        "ratingCountTot" -> story.ratingCountTotal = getText()?.toIntOrNull()
        else             -> skip()
      }
    }
  }

  private fun readCoverArt() {
    parser.require(XmlPullParser.START_TAG, null, "coverart")
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType != XmlPullParser.START_TAG) continue
      when (parser.name) {
        "url" -> story.coverArtURL = getText()
        else  -> skip()
      }
    }
  }

  private fun getText(): String? {
    val elementName = parser.name
    parser.require(XmlPullParser.START_TAG, null, elementName)
    var result: String? = null
    if (parser.next() == XmlPullParser.TEXT) {
      result = parser.text
      // pschaaf 09/250/17 14:09: this is a hack, but what else can I do? Instead of returning null the library returns the string "null".
      if (result == "null") result = null
      parser.nextTag()
    }
    parser.require(XmlPullParser.END_TAG, null, elementName)
    return result
  }

  private fun skip() {
    if (parser.eventType == XmlPullParser.END_TAG) parser.next()
    else {
      if (parser.eventType != XmlPullParser.START_TAG) {
        throw IllegalStateException()
      }
      var depth = 1
      while (depth != 0) {
        when (parser.next()) {
          XmlPullParser.START_TAG -> depth++
          XmlPullParser.END_TAG   -> depth--
        }
      }
    }
  }
}
