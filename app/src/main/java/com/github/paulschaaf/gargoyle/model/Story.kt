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

import android.content.ContentValues
import set
import java.io.File
import java.io.RandomAccessFile
import kotlin.reflect.full.memberProperties

interface IFDBStorySnapshot: IFDBStory {
  val id: String
  val lookedUp: String
}

abstract class Story: IFDBStorySnapshot {
  override val id: String = ""
  override val lookedUp: String = ""

  companion object {
    fun create(block: EditableStory.() -> Unit): Story {
      val newStory = EditableStory()
      block(newStory)
      return newStory
    }
  }

  override fun toString() = title + " #" + ifId

  val file
    get() = if (path == null) null else File(path)

  val exists
    get() = file?.exists() == true

  val versionNumber
    get() = RandomAccessFile(path, "r").use { it.readInt() }

  val zCodeVersion
    get() = when (versionNumber) {
      0    -> "0 (unknown)"
      70   -> "unknown (blorbed)"
      else -> versionNumber.toString()
    }

  fun toContentValues(): ContentValues {
    val cv = ContentValues()
    IFDBStorySnapshot::class.memberProperties.forEach { prop->
      cv.set(prop.name, prop.get(this))
    }
    return cv
  }

  class EditableStory: Story() {
    override var id: String = "-error-"
    override var lookedUp: String = ""
    override var author: String? = null
    override var contact: String? = null
    override var coverArtURL: String? = null
    override var description: String? = null
    override var firstPublished: String? = null
    override var forgiveness: String? = null
    override var genre: String? = null
    override var ifId: String = "-error-"
    override var language: String? = null
    override var link: String? = null
    override var path: String? = null
    override var series: String? = null
    override var title: String = "-Unknown-"
    override var tuid: String = ""

    override var averageRating: Double? = null
    var averageRatingString
      get() = averageRating.toString()
      set(value) {
        averageRating = value.toDouble()
      }

    override var starRating: Double? = null
    var starRatingString
      get() = starRating.toString()
      set(value) {
        starRating = value.toDoubleOrNull()
      }

    override var ratingCountAvg: Int? = null
    var ratingCountAvgString
      get() = ratingCountAvg.toString()
      set(value) {
        ratingCountAvg = value.toIntOrNull()
      }

    override var ratingCountTotal: Int? = null
    var ratingCountTotalString
      get() = ratingCountTotal.toString()
      set(value) {
        ratingCountTotal = value.toIntOrNull()
      }

    override var seriesNumber: Int? = null
    var seriesNumberString
      get() = seriesNumber.toString()
      set(value) {
        seriesNumber = value.toIntOrNull()
      }

//    private operator fun getValue(editableStory: Story.EditableStory, property: KProperty<*>): String =
//        when (property) {
//          EditableStory::averageRatingStr  -> averageRating.toString()
//          EditableStory::starRatingStr     -> starRating.toString()
//          EditableStory::ratingCountAvgStr -> ratingCountAvg.toString()
//          else                             -> ""
//        }
//
//    private operator fun setValue(editableStory: Story.EditableStory, property: KProperty<*>, s: String) {
//      when (property) {
//        EditableStory::averageRatingStr  -> averageRating = s.toDoubleOrNull()
//        EditableStory::starRatingStr     -> starRating.toString()
//        EditableStory::ratingCountAvgStr -> ratingCountAvg.toString()
//        else                             -> ""
//      }
//    }
  }
}

