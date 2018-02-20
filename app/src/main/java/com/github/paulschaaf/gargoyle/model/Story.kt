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

package com.github.paulschaaf.gargoyle.model

import java.io.File
import java.io.RandomAccessFile

data class Story(
    val id: String,
    val lookedUp: String,
    override val author: String?,
    override val averageRating: Double?,
    override val contact: String?,
    override val coverArtURL: String?,
    override val description: String?,
    override val firstPublished: String?,
    override val forgiveness: String?,
    override val genre: String?,
    override val ifId: String,
    override val language: String?,
    override val link: String?,
    override val path: String?,
    override val ratingCountAvg: Int?,
    override val ratingCountTotal: Int?,
    override val series: String?,
    override val seriesNumber: Int?,
    override val starRating: Double?,
    override val title: String,
    override val tuid: String
): IFDBStory {
//  constructor(val contentValues: ContentValues) {}

  companion object {
    class StoryBuilder {
      var withId: String = ""
      var withLookedUp: String = ""
      var withAuthor: String? = null
      var withAverageRating: Double? = null
      var withContact: String? = null
      var withCoverArtURL: String? = null
      var withDescription: String? = null
      var withFirstPublished: String? = null
      var withForgiveness: String? = null
      var withGenre: String? = null
      var withIfId: String = ""
      var withLanguage: String? = null
      var withLink: String? = null
      var withPath: String? = null
      var withRatingCountAvg: Int? = 0
      var withRatingCountTotal: Int? = null
      var withSeries: String? = ""
      var withSeriesNumber: Int? = null
      var withStarRating: Double? = null
      var withTitle: String = ""
      var withTuid: String = ""
      fun build(): Story {
        return Story(
            id = this.withId,
            lookedUp = withLookedUp,
            author = withAuthor,
            averageRating = withAverageRating,
            contact = withContact,
            coverArtURL = withCoverArtURL,
            description = withDescription,
            firstPublished = withFirstPublished,
            forgiveness = withForgiveness,
            genre = withGenre,
            ifId = withIfId,
            language = withLanguage,
            link = withLink,
            path = withPath,
            ratingCountAvg = withRatingCountAvg,
            ratingCountTotal = withRatingCountTotal,
            series = withSeries,
            seriesNumber = withSeriesNumber,
            starRating = withStarRating,
            title = withTitle,
            tuid = withTuid
        )
      }
    }
  }
}
//    constructor(): this(ContentValues()) {
//      lookedUp = Date().toString()
//    }

  override fun toString() = title + " #" + ifId

val file = if (path == null) null else File(path)

val exists = file?.exists() == true

val versionNumber = RandomAccessFile(path, "r").use { it.readInt() }

val zCodeVersion = when (versionNumber) {
      0    -> "0 (unknown)"
      70   -> "unknown (blorbed)"
      else -> versionNumber.toString()
    }

//    var id by StoryTable.id
//    override var author by StoryTable.author
//    override var averageRating by StoryTable.averageRating
//    override var contact by StoryTable.contact
//    override var coverArtURL by StoryTable.coverArtURL
//    override var description by StoryTable.description
//    override var firstPublished by StoryTable.firstPublished
//    override var forgiveness by StoryTable.forgiveness
//    override var genre by StoryTable.genre
//    override var ifId by StoryTable.ifId
//    override var language by StoryTable.language
//    override var link by StoryTable.link
//    var lookedUp by StoryTable.lookedUp
//    override var path by StoryTable.path
//    override var ratingCountAvg by StoryTable.ratingCountAvg
//    override var ratingCountTotal by StoryTable.ratingCountTotal
//    override var series by StoryTable.series
//    override var seriesNumber by StoryTable.seriesNumber
//    override var starRating by StoryTable.starRating
//    override var title by StoryTable.title
//    override var tuid by StoryTable.tuid
}

//// Allows an IColumn wrapping my contentValues to be a delegate for my IFDBStory properties
//operator fun <T> IColumn<T>.getValue(story: Story, property: KProperty<*>): T
//    = get(story.contentValues)
//
//operator fun <T> IColumn<T>.setValue(story: Story, property: KProperty<*>, value: T)
//    = set(story.contentValues, value)
//
