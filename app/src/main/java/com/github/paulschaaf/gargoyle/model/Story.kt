package com.github.paulschaaf.gargoyle.model

import android.content.ContentValues
import android.provider.BaseColumns

import java.io.File
import java.io.RandomAccessFile
import java.util.*

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

  sealed class Column<T> {
    override fun toString() = this.javaClass.simpleName
    val name: kotlin.String
      get() = toString()

    operator fun plus(other: Any?) = name + other?.toString()

    abstract val createSQL: kotlin.String

    operator fun get(story: Story): T = get(story.contentValues)
    operator fun set(story: Story, value: T) = set(story.contentValues, value)

    abstract operator fun get(conValues: ContentValues): T
    abstract operator fun set(conValues: ContentValues, value: T)

    abstract class Double: Column<kotlin.Double?>() {
      override operator fun get(conValues: ContentValues) = conValues.getAsDouble(name)
      override operator fun set(conValues: ContentValues, value: kotlin.Double?) = conValues.put(name, value)
      override val createSQL: kotlin.String
        get() = "${name} DOUBLE"
    }

    abstract class Int: Column<kotlin.Int?>() {
      override operator fun get(conValues: ContentValues) = conValues.getAsInteger(name)
      override operator fun set(conValues: ContentValues, value: kotlin.Int?) = conValues.put(name, value)
      override val createSQL: kotlin.String
        get() = "${name} INTEGER"
    }

    abstract class String: Column<kotlin.String?>() {
      override operator fun get(conValues: ContentValues) = conValues.get(name)?.toString()
      override operator fun set(conValues: ContentValues, value: kotlin.String?) = conValues.put(name, value?.trim())
      override val createSQL: kotlin.String
        get() = "${name} TEXT"
    }
  }

  //
  // COLUMN DEFINITIONS
  //

  object _ID: Column.Int() {
    override fun set(conValues: ContentValues, value: kotlin.Int?) = super.set(conValues, value!!)
    override val createSQL: kotlin.String
      get() = super.createSQL + " PRIMARY KEY"
  }

  object IFID: Column.String() {
    override fun set(conValues: ContentValues, value: kotlin.String?) = super.set(conValues, value!!)
    override val createSQL: kotlin.String
      get() = super.createSQL + " UNIQUE NOT NULL"
  }

  object Author: Column.String()
  object AverageRating: Column.Double()
  object CoverArtURL: Column.String()
  object Description: Column.String()
  object FirstPublished: Column.String()
  object Forgiveness: Column.String()
  object Genre: Column.String()
  object Language: Column.String()
  object Link: Column.String()
  object LookedUp: Column.String()
  object Path: Column.String()
  object Series: Column.String()
  object SeriesNumber: Column.Int()
  object RatingCountAvg: Column.Int()
  object RatingCountTotal: Column.Int()
  object StarRating: Column.Int()
  object Title: Column.String()
  object TUID: Column.String()

  object Table {
    val name = "Story"
    val columns = listOf(
        _ID,
        IFID,
        Author,
        AverageRating,
        CoverArtURL,
        Description,
        FirstPublished,
        Forgiveness,
        Genre,
        Language,
        Link,
        LookedUp,
        Path,
        Series,
        SeriesNumber,
        RatingCountAvg,
        RatingCountTotal,
        StarRating,
        Title,
        TUID
    )

    val createSQL: String
      get() = columns
          .map { it.createSQL }
          .joinToString(
              prefix = "CREATE TABLE ${name} (",
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
      Author[this] = value
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

