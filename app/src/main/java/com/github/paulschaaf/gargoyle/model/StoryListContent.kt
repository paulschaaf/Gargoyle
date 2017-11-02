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

import android.content.Context
import android.database.DatabaseUtils
import com.github.paulschaaf.gargoyle.DatabaseHelper
import java.util.*

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 */
object StoryListContent {
  val STORIES: MutableList<Story> = ArrayList()

  /**
   * A map of items by ID
   */
  val ITEM_MAP: MutableMap<String, Story> = HashMap()

  private val COUNT = 25

  fun setup(context: Context) {
    val cursor = DatabaseHelper.getInstance(context).newCursor
    cursor.use {
      val newStory = Story()
      DatabaseUtils.cursorRowToContentValues(it, newStory.contentValues)
      ITEM_MAP.put(newStory.id, newStory)
      STORIES.add(newStory)
    }
  }

//  private fun makeDetails(position: Int): String {
//    val builder = StringBuilder()
//    builder.append("Details about Item: ").append(position)
//    for (i in 0..position - 1) {
//      builder.append("\nMore details information here.")
//    }
//    return builder.toString()
//  }
}
