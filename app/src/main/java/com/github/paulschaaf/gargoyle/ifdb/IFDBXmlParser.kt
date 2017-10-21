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

package com.github.paulschaaf.gargoyle.ifdb

import com.github.paulschaaf.gargoyle.model.Story
import java.io.InputStream

class IFDBXmlParser {
  val story = Story()

  val storyXmlDocumentGrammar = XmlDocument("ifindex") {
    "story" {
      "identification" {
        "ifid".writeTo(story::ifId) { it ?: "-error-" }
      }
      "bibliographic" {
        "title".writeTo(story::title) { it ?: "-Unknown-" }
        "author" to story::author
        "language".writeTo(story::language)
        "firstpublished".writeTo(story::firstPublished)
        "genre".writeTo(story::genre)
        "description".writeTo(story::description)
        "series".writeTo(story::series)
        "seriesnumber".writeTo(story::seriesNumber) { it?.toIntOrNull() }
        "forgiveness".writeTo(story::forgiveness)
      }
      "contact" {
        "url".writeTo(story::contact)
      }
      "ifdb" {
        "tuid".writeTo(story::tuid)
        "link".writeTo(story::link)
        "coverart" {
          "url".writeTo(story::coverArtURL)
        }
        "averageRating".writeTo(story::averageRating) { it?.toDoubleOrNull() }
        "starRating".writeTo(story::starRating) { it?.toDoubleOrNull() }
        "ratingCountAvg".writeTo(story::ratingCountAvg) { it?.toIntOrNull() }
        "ratingCountTot".writeTo(story::ratingCountTotal) { it?.toIntOrNull() }
      }
    }
  }

  fun parse(inputStream: InputStream): Story {
    storyXmlDocumentGrammar.parse(inputStream)
    return story
  }
}

