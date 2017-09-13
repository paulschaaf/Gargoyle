package com.github.paulschaaf.gargoyle.model

import android.content.ContentValues
import com.github.paulschaaf.gargoyle.database.StoryTable

import java.io.File
import java.io.RandomAccessFile
import java.util.Date


class Story private constructor(val contentValues: ContentValues) {
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

  var author
    get() = table.Author[this]
    set(value) {
      table.Author[contentValues] = value
    }

  var averageRating
    get() = table.AverageRating[this]
    set(value) {
      table.AverageRating[this] = value
    }

  var coverArtURL
    get() = table.CoverArtURL[this]
    set(value) {
      table.CoverArtURL[this] = value
    }

  var description
    get() = table.Description[this]
    set(value) {
      table.Description[this] = value
    }

  var firstPublished
    get() = table.FirstPublished[this]
    set(value) {
      table.FirstPublished[this] = value
    }

  var forgiveness
    get() = table.Forgiveness[this]
    set(value) {
      table.Forgiveness[this] = value
    }

  var genre
    get() = table.Genre[this]
    set(value) {
      table.Genre[this] = value
    }

  var id
    get() = table._ID[this]
    set(value) {
      table._ID[this] = value
    }

  var ifId
    get() = table.IFID[this]
    set(value) {
      table.IFID[this] = value
    }

  var language
    get() = table.Language[this]
    set(value) {
      table.Language[this] = value
    }

  var link
    get() = table.Link[this]
    set(value) {
      table.Link[this] = value
    }

  var lookedUp
    get() = table.LookedUp[this]
    set(value) {
      table.LookedUp[this] = value
    }

  var path
    get() = table.Path[this]
    protected set(value) {
      table.Path[this] = value
    }

  var ratingCountAvg
    get() = table.RatingCountAvg[this]
    set(value) {
      table.RatingCountAvg[this] = value
    }

  var ratingCountTotal
    get() = table.RatingCountTotal[this]
    set(value) {
      table.RatingCountTotal[this] = value
    }

  var series
    get() = table.Series[this]
    set(value) {
      table.Series[this] = value
    }

  var seriesNumber
    get() = table.SeriesNumber[this]
    set(value) {
      table.SeriesNumber[this] = value
    }

  var starRating
    get() = table.StarRating[this]
    set(value) {
      table.StarRating[this] = value
    }

  var title
    get() = table.Title[this]
    set(value) {
      table.Title[this] = value
    }

  var tuid
    get() = table.TUID[this]
    set(value) {
      table.TUID[this] = value
    }
}
