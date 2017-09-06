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
//    val AUTHORITY = "org.andglk.hunkypunk.HunkyPunk"
//    val CONTENT_URI = Uri.parseStory("content://$AUTHORITY/games")

//    val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.andglk.game"
//    val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.andglk.game"
//    val DEFAULT_SORT_ORDER = "lower(title) ASC"

    fun valueOf(contentValues: ContentValues) = Story(contentValues)

//    fun fromCursor(cursor: Cursor): Story? {
//      return if (!cursor.isBeforeFirst && !cursor.isAfterLast) {
//        val cv = ContentValues()
//        DatabaseUtils.cursorRowToContentValues(cursor, cv)
//        valueOf(cv)
//      }
//      else null
//    }
  }

  constructor(): this(ContentValues())

  constructor(id: String, link: String): this() {
    this.id = id
    this.link = link
  }
//  constructor(aFile: File): this() {
//    path = aFile.absolutePath
//    // read then set the ifId
////    with(aFile) { ifId = Babel.examine(file) }
//
//    file = aFile
//
//    // connect to IFDB
//
//    // populate from IFDB
//    contentValues = ContentValues()
//
//    // write to database
//  }

  sealed class Column<T> {
    val name
      get() = this.javaClass.simpleName

    operator fun get(story: Story): T = get(story.contentValues)
    operator fun set(story: Story, value: T) = set(story.contentValues, value)

    abstract operator fun get(conValues: ContentValues): T
    abstract operator fun set(conValues: ContentValues, value: T)

    abstract class Double: Column<kotlin.Double?>() {
      override operator fun get(conValues: ContentValues) = conValues.getAsDouble(name)
      override operator fun set(conValues: ContentValues, value: kotlin.Double?) = conValues.put(name, value)
    }

    abstract class Float: Column<kotlin.Float?>() {
      override operator fun get(conValues: ContentValues) = conValues.getAsFloat(name)
      override operator fun set(conValues: ContentValues, value: kotlin.Float?) = conValues.put(name, value)
    }

    abstract class Int: Column<kotlin.Int?>() {
      override operator fun get(conValues: ContentValues) = conValues.getAsInteger(name)
      override operator fun set(conValues: ContentValues, value: kotlin.Int?) = conValues.put(name, value)
    }

    abstract class Long: Column<kotlin.Long?>() {
      override operator fun get(conValues: ContentValues) = conValues.getAsLong(name)
      override operator fun set(conValues: ContentValues, value: kotlin.Long?) = conValues.put(name, value)
    }

    abstract class String: Column<kotlin.String?>() {
      override operator fun get(conValues: ContentValues) = conValues.getAsString(name)
      override operator fun set(conValues: ContentValues, value: kotlin.String?) = conValues.put(name, value?.trim())
    }
  }

  //
  // COLUMN DEFINITIONS
  //

  object Author: Column.String()
  object AverageRating: Column.Double()
  object Description: Column.String()
  object FirstPublished: Column.String()
  object Forgiveness: Column.String()
  object Genre: Column.String()
  object IFID: Column.String()
  object _ID: Column.String()
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

  //
  // METHODS
  //

  override fun toString() = title + " #" + ifId

  //
  // DERIVED PROPERTIES
  //

//  val cover
//    get() = File(Paths.COVERS.directory(), ifId)
//
//  val coverDrawable
//    get() = if (cover.exists()) Drawable.createFromPath(cover.path) else null
//
//  val coverURI
//    get() = if (cover.exists()) Uri.fromFile(cover) else null

  val exists: Boolean
    get() = file.exists()

  val file: File
    get() = File(path)

  var ifId
    get() = IFID[this]
    set(value) {
      IFID[this] = value
//      val last = uri.lastPathSegment
//      if (last == null) return
//      id = java.lang.Long.parseLong(last)
    }

//  val uri
//    get() = Uri.withAppendedPath(CONTENT_URI, ifId)

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
  // NON-PUBLICLY-EDITABLE PROPERTIES
  //

  var lookedUp
    get() = LookedUp[this]
    private set(value) {
      LookedUp[this] = value
    }

  init {
    lookedUp = Date().toString()
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
}

