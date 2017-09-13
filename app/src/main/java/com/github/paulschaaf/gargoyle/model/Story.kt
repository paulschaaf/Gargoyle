package com.github.paulschaaf.gargoyle.model

import android.content.ContentValues
import com.github.paulschaaf.gargoyle.database.Column

import java.io.File
import java.io.RandomAccessFile
import java.util.Date


class Story private constructor(val contentValues: ContentValues) {
  //
  // COMPANION OBJECT
  //
  companion object {
    fun valueOf(contentValues: ContentValues) = Story(contentValues)
  }

  constructor(): this(ContentValues()) {
    lookedUp = Date().toString()
  }

  //
  // COLUMN DEFINITIONS
  //

  enum class DoubleColumn(override var sqlNewColumnProperties: String = ""):
      com.github.paulschaaf.gargoyle.database.DoubleColumn {
    AverageRating;

    override val columnName = name
  }


  enum class IntColumn(override var sqlNewColumnProperties: String = ""):
      com.github.paulschaaf.gargoyle.database.IntColumn {
    _ID("PRIMARY KEY") {
      override val columnName = "_id"
    },
    RatingCountAvg,
    RatingCountTotal,
    SeriesNumber,
    StarRating;

    override val columnName = name
  }

  enum class StringColumn(override var sqlNewColumnProperties: String = ""):
      com.github.paulschaaf.gargoyle.database.StringColumn {
    Author,
    CoverArtURL,
    Description,
    FirstPublished,
    Forgiveness,
    Genre,
    IFID("UNIQUE NOT NULL"),
    Language,
    Link,
    LookedUp,
    Path,
    Series,
    TUID,
    Title;

    override val columnName = name
  }

  object Table: com.github.paulschaaf.gargoyle.database.Table {
    override val name = "Story"
    override val columns: List<Column<*>> = listOf(
        *IntColumn.values(),
        *DoubleColumn.values(),
        *StringColumn.values()
    )
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
    get() = StringColumn.Author[this]
    set(value) {
      StringColumn.Author[contentValues] = value
    }

  var averageRating
    get() = DoubleColumn.AverageRating[this]
    set(value) {
      DoubleColumn.AverageRating[this] = value
    }

  var coverArtURL
    get() = StringColumn.CoverArtURL[this]
    set(value) {
      StringColumn.CoverArtURL[this] = value
    }

  var description
    get() = StringColumn.Description[this]
    set(value) {
      StringColumn.Description[this] = value
    }

  var firstPublished
    get() = StringColumn.FirstPublished[this]
    set(value) {
      StringColumn.FirstPublished[this] = value
    }

  var forgiveness
    get() = StringColumn.Forgiveness[this]
    set(value) {
      StringColumn.Forgiveness[this] = value
    }

  var genre
    get() = StringColumn.Genre[this]
    set(value) {
      StringColumn.Genre[this] = value
    }

  var id
    get() = IntColumn._ID[this]
    set(value) {
      IntColumn._ID[this] = value
    }

  var ifId
    get() = StringColumn.IFID[this]
    set(value) {
      StringColumn.IFID[this] = value
    }

  var language
    get() = StringColumn.Language[this]
    set(value) {
      StringColumn.Language[this] = value
    }

  var link
    get() = StringColumn.Link[this]
    set(value) {
      StringColumn.Link[this] = value
    }

  var lookedUp
    get() = StringColumn.LookedUp[this]
    set(value) {
      StringColumn.LookedUp[this] = value
    }

  var path
    get() = StringColumn.Path[this]
    protected set(value) {
      StringColumn.Path[this] = value
    }

  var ratingCountAvg
    get() = IntColumn.RatingCountAvg[this]
    set(value) {
      IntColumn.RatingCountAvg[this] = value
    }

  var ratingCountTotal
    get() = IntColumn.RatingCountTotal[this]
    set(value) {
      IntColumn.RatingCountTotal[this] = value
    }

  var series
    get() = StringColumn.Series[this]
    set(value) {
      StringColumn.Series[this] = value
    }

  var seriesNumber
    get() = IntColumn.SeriesNumber[this]
    set(value) {
      IntColumn.SeriesNumber[this] = value
    }

  var starRating
    get() = IntColumn.StarRating[this]
    set(value) {
      IntColumn.StarRating[this] = value
    }

  var title
    get() = StringColumn.Title[this]
    set(value) {
      StringColumn.Title[this] = value
    }

  var tuid
    get() = StringColumn.TUID[this]
    set(value) {
      StringColumn.TUID[this] = value
    }

}
