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

package com.github.paulschaaf.gargoyle.ifdb

import com.github.paulschaaf.gargoyle.model.Story
import java.io.InputStream

class IFDBXmlParser private constructor(story: Story.EditableStory) {
  companion object {
    fun parse(inputStream: InputStream) = Story.create {
      IFDBXmlParser(this).storyXmlDocumentGrammar.parse(inputStream)
    }
  }

  val storyXmlDocumentGrammar = XmlDocumentGrammar("ifindex") {
    "story" {
      "identification" {
        "ifid" to story::ifId
      }
      "bibliographic" {
        "title" to story::title
        "author" to story::author
        "language" to story::language
        "firstpublished" to story::firstPublished
        "genre" to story::genre
        "description" to story::description
        "series" to story::series
        "seriesnumber" to story::seriesNumberString
        "forgiveness" to story::forgiveness
      }
      "contact" {
        "url" to story::contact
      }
      "ifdb" {
        "tuid" to story::tuid
        "link" to story::link
        "coverart" {
          "url" to story::coverArtURL
        }
        "averageRating" to story::averageRatingString
        "starRating" to story::starRatingString
        "ratingCountAvg" to story::ratingCountAvgString
        "ratingCountTot" to story::ratingCountTotalString
      }
    }
  }
}