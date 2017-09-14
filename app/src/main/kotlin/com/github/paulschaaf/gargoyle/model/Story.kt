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

package com.github.paulschaaf.gargoyle.model

import android.content.ContentValues
import com.github.paulschaaf.gargoyle.database.StoryTable
import java.io.File
import java.io.RandomAccessFile
import java.util.*


class Story private constructor(val contentValues: ContentValues): IStory {
  //
  // COMPANION OBJECT
  //
  companion object {
    fun valueOf(contentValues: ContentValues) = Story(contentValues)

    val table = StoryTable
  }

  constructor(): this(ContentValues()) {
    lookedUp = Date().toString()
  }


  //
  // METHODS
  //

  override fun toString() = title + " #" + ifId


  //
  // DERIVED PROPERTIES
  //

  val exists: Boolean
    get() = file.exists()

  val file: File
    get() = File(path)

  var versionNumber = 0
    get() {
      if (field == 0) {
        field = RandomAccessFile(path, "r").use { it.readInt() }
      }
      return field
    }

  val zCodeVersion
    get() = when (versionNumber) {
      0    -> "0 (unknown)"
      70   -> "unknown (blorbed)"
      else -> {
        versionNumber.toString()
      }
    }


  //
  // SIMPLE MAPPED PROPERTIES
  //

  override var author
    get() = table.Author[this]
    set(value) {
      table.Author[contentValues] = value
    }

  override var averageRating
    get() = table.AverageRating[this]
    set(value) {
      table.AverageRating[this] = value
    }

  override var coverArtURL
    get() = table.CoverArtURL[this]
    set(value) {
      table.CoverArtURL[this] = value
    }

  override var description
    get() = table.Description[this]
    set(value) {
      table.Description[this] = value
    }

  override var firstPublished
    get() = table.FirstPublished[this]
    set(value) {
      table.FirstPublished[this] = value
    }

  override var forgiveness
    get() = table.Forgiveness[this]
    set(value) {
      table.Forgiveness[this] = value
    }

  override var genre
    get() = table.Genre[this]
    set(value) {
      table.Genre[this] = value
    }

  override var id
    get() = table._ID[this]
    set(value) {
      table._ID[this] = value
    }

  override var ifId
    get() = table.IFID[this]
    set(value) {
      table.IFID[this] = value
    }

  override var language
    get() = table.Language[this]
    set(value) {
      table.Language[this] = value
    }

  override var link
    get() = table.Link[this]
    set(value) {
      table.Link[this] = value
    }

  override var lookedUp
    get() = table.LookedUp[this]
    set(value) {
      table.LookedUp[this] = value
    }

  override var path
    get() = table.Path[this]
    protected set(value) {
      table.Path[this] = value
    }

  override var ratingCountAvg
    get() = table.RatingCountAvg[this]
    set(value) {
      table.RatingCountAvg[this] = value
    }

  override var ratingCountTotal
    get() = table.RatingCountTotal[this]
    set(value) {
      table.RatingCountTotal[this] = value
    }

  override var series
    get() = table.Series[this]
    set(value) {
      table.Series[this] = value
    }

  override var seriesNumber
    get() = table.SeriesNumber[this]
    set(value) {
      table.SeriesNumber[this] = value
    }

  override var starRating
    get() = table.StarRating[this]
    set(value) {
      table.StarRating[this] = value
    }

  override var title
    get() = table.Title[this]
    set(value) {
      table.Title[this] = value
    }

  override var tuid
    get() = table.TUID[this]
    set(value) {
      table.TUID[this] = value
    }
}
