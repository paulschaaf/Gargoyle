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
import com.github.paulschaaf.gargoyle.database.IColumn
import com.github.paulschaaf.gargoyle.database.StoryTable
import java.io.File
import java.io.RandomAccessFile
import java.util.*
import kotlin.reflect.KProperty

class Story private constructor(val contentValues: ContentValues): IStory {
  // Allows an IColumn wrapping my contentValues to be a delegate for my IStory properties
  operator fun <T> IColumn<T>.getValue(story: Story, property: KProperty<*>): T
      = get(story.contentValues)

  operator fun <T> IColumn<T>.setValue(story: Story, property: KProperty<*>, value: T)
      = set(story.contentValues, value)


  companion object {
    fun valueOf(contentValues: ContentValues) = Story(contentValues)

    val table = StoryTable
  }

  constructor(): this(ContentValues()) {
    lookedUp = Date().toString()
  }

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

  override var author by table.author
//  var author2: String? = table.author.getValue(contentValues, Story::author) // todo pschaaf 09/263/17 15:09: Remove this debugging code

  override var averageRating by table.averageRating
  override var coverArtURL by table.coverArtURL
  override var description by table.description
  override var firstPublished by table.firstPublished
  override var forgiveness by table.forgiveness
  override var genre by table.genre
  override var id by table.id
  override var ifId by table.ifid
  override var language by table.language
  override var link by table.link
  override var lookedUp by table.lookedUp
  override var path by table.path
  override var ratingCountAvg by table.ratingCountAvg
  override var ratingCountTotal by table.ratingCountTotal
  override var series by table.series
  override var seriesNumber by table.seriesNumber
  override var starRating by table.starRating
  override var title by table.title
  override var tuid by table.tuid
}
