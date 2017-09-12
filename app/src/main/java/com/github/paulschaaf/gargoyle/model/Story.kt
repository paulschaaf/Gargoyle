package com.github.paulschaaf.gargoyle.model

import android.content.ContentValues
import android.provider.BaseColumns
import com.github.paulschaaf.gargoyle.database.Column

import java.io.File
import java.io.RandomAccessFile
import java.util.*
import com.github.paulschaaf.gargoyle.model.Story.IntColumn.*
import com.github.paulschaaf.gargoyle.model.Story.DoubleColumn.*
import com.github.paulschaaf.gargoyle.model.Story.StringColumn.*


class Story private constructor(val contentValues: ContentValues): BaseColumns {
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

  object Table {
    val name = "Story"

    val columns: List<Column<*>> = listOf(
        *IntColumn.values(),
        *DoubleColumn.values(),
        *StringColumn.values()
    )

    val createSQL = columns
      .map { it.createSQL }
      .joinToString(
          prefix = "CREATE TABLE $name (",
          separator = ", ",
          postfix = ");"
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
    get() = Author[this]
    set(value) {
      Author[contentValues] = value
    }

  var averageRating
    get() = AverageRating[this]
    set(value) {
      AverageRating[this] = value
    }

  var coverArtURL
    get() = CoverArtURL[this]
    set(value) {
      CoverArtURL[this] = value
    }

  var description
    get() = Description[this]
    set(value) {
      Description[this] = value
    }

  var firstPublished
    get() = FirstPublished[this]
    set(value) {
      FirstPublished[this] = value
    }

  var forgiveness
    get() = Forgiveness[this]
    set(value) {
      Forgiveness[this] = value
    }

  var genre
    get() = Genre[this]
    set(value) {
      Genre[this] = value
    }

  var id
    get() = _ID[this]
    set(value) {
      _ID[this] = value
    }

  var ifId
    get() = IFID[this]
    set(value) {
      IFID[this] = value
    }

  var language
    get() = Language[this]
    set(value) {
      Language[this] = value
    }

  var link
    get() = Link[this]
    set(value) {
      Link[this] = value
    }

  var lookedUp
    get() = LookedUp[this]
    set(value) {
      LookedUp[this] = value
    }

  var path
    get() = Path[this]
    protected set(value) {
      Path[this] = value
    }

  var ratingCountAvg
    get() = RatingCountAvg[this]
    set(value) {
      RatingCountAvg[this] = value
    }

  var ratingCountTotal
    get() = RatingCountTotal[this]
    set(value) {
      RatingCountTotal[this] = value
    }

  var series
    get() = Series[this]
    set(value) {
      Series[this] = value
    }

  var seriesNumber
    get() = SeriesNumber[this]
    set(value) {
      SeriesNumber[this] = value
    }

  var starRating
    get() = StarRating[this]
    set(value) {
      StarRating[this] = value
    }

  var title
    get() = Title[this]
    set(value) {
      Title[this] = value
    }

  var tuid
    get() = TUID[this]
    set(value) {
      TUID[this] = value
    }
}

