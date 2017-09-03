/*
 * Copyright � 2015 Paul Schaaf <paul.schaaf@gmail.com>
 *
 * This file is part of Hunky Punk.
 *
 * Hunky Punk is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Hunky Punk is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Hunky Punk. If not,
 * see <http://www.gnu.org/licenses/>.
 */

package com.github.paulschaaf.gargoyle.model

import android.content.ContentValues
import android.database.Cursor
import android.database.DatabaseUtils
import android.provider.BaseColumns

import java.io.File
import java.io.RandomAccessFile
import java.util.Collections

class Story private constructor(val contentValues: ContentValues): BaseColumns {
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

//  enum class Cols(val getter: ContentValues.(String)->String) {
//    Author(ContentValues::getAsString(this.name)),// {game: Story -> game.contentValues.getAsString(Cols.Author.name)}),
//    Title ({game: Story -> game.contentValues.getAsString(Cols.Author.name)});
//  }
//
//  val getters = hashMapOf(
//      Cols.Author to { game: Story -> game.contentValues.getAsString(Cols.Author.name)}
//  )

  sealed class Column<T> {
    val name
      get() = this.javaClass.simpleName

    override fun toString() = name

    operator fun get(story: Story): T = get(story.contentValues)
    operator fun set(story: Story, value: T) = set(story.contentValues, value)

    abstract operator fun get(conValues: ContentValues): T
    abstract operator fun set(conValues: ContentValues, value: T)

    abstract class String: Column<kotlin.String>() {
      override operator fun get(conValues: ContentValues) = conValues.getAsString(name).trim()
      override operator fun set(conValues: ContentValues, value: kotlin.String) = conValues.put(name, value.trim())
    }

    abstract class Int: Column<kotlin.Int>() {
      override operator fun get(conValues: ContentValues) = conValues.getAsInteger(name)
      override operator fun set(conValues: ContentValues, value: kotlin.Int) = conValues.put(name, value)
    }

    abstract class Long: Column<kotlin.Long>() {
      override operator fun get(conValues: ContentValues) = conValues.getAsLong(name)
      override operator fun set(conValues: ContentValues, value: kotlin.Long) = conValues.put(name, value)
    }
  }

  //
  // COLUMN DEFINITIONS
  //

  object Author: Column.String()
  object AverageRating: Column.String()
  object Description: Column.String()
  object FirstPublished: Column.String()
  object Forgiveness: Column.String()
  object Genre: Column.String()
  object Collection: Column.String()
  object Headline: Column.String()
  object IFID: Column.String()
  object _ID: Column.Long()
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
  // COMPANION OBJECT
  //

  companion object {
    private val _all_instances = mutableMapOf<String, Story>()
    val ALL_INSTANCES = Collections.unmodifiableMap(_all_instances)

    val AUTHORITY = "org.andglk.hunkypunk.HunkyPunk"
//    val CONTENT_URI = Uri.parse("content://$AUTHORITY/games")

    val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.andglk.game"
    val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.andglk.game"
    val DEFAULT_SORT_ORDER = "lower(title) ASC"

    fun valueOf(contentValues: ContentValues) = Story(contentValues)

    fun fromCursor(cursor: Cursor): Story? {
      return if (!cursor.isBeforeFirst && !cursor.isAfterLast) {
        val cv = ContentValues()
        DatabaseUtils.cursorRowToContentValues(cursor, cv)
        valueOf(cv)
      }
      else null
    }
  }

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

  var file: File? = null
    private set(value) {
      field = value
    }

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

  var collection
    get() = Collection[this]
    set(value) {
      Collection[this] = value
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

  var group
    get() = Collection[this]
    set(value) {
      Collection[this] = value
    }

  var headline
    get() = Headline[this]
    set(value) {
      Headline[this] = value
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
}

