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

package com.github.paulschaaf.gargoyle

/**
 * Created by pschaaf on 9/8/17.
 */
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.github.paulschaaf.gargoyle.database.StoryTable
import com.github.paulschaaf.gargoyle.model.Story

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
  override fun onCreate(db: SQLiteDatabase) = db.execSQL(StoryTable.createSQL)

  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

  fun insertStory(story: Story): Long {
    val writableDatabase = writableDatabase
    val rowID = writableDatabase.insert(StoryTable.name, null, story.contentValues)
    if (rowID == -1L) Log.e(TAG, "The insert failed!")
    return rowID
  }

  fun rebuildDatabase() {
    val db = writableDatabase
    db.execSQL("DROP TABLE " + StoryTable.name)
//    db.delete(Story.TableName, null, null)
    db.close()
  }

//  fun deleteStory(story: Story): IIntColumn {
//    var success = true
//    val storyFile = story.file
//    if (storyFile != null && storyFile!!.exists()) success = success and storyFile!!.delete()
//
//    val coverFile = story.cover
//    if (coverFile != null && coverFile!!.exists()) success = success and coverFile!!.delete()
//
//    val db = writableDatabase
//    val storyId = story.id
//    return db.delete(Story.TableName, Story.id + "=?", arrayOf(storyId))
//  }

  fun updateStory(story: Story) = writableDatabase.update(
      StoryTable.name,
      story.contentValues,
      "${StoryTable.id} = ${story.id}",
      null
  )

  // SIMPLE ACCESSORS

  val newCursor: Cursor
    get() {
      val cursor = readableDatabase.query(
          StoryTable.name, null, null, null, null, null,
          StoryTable.title.name + " COLLATE NOCASE"
      )
      cursor.moveToFirst()
      return cursor
    }

  companion object {
//    val Story = 1
//    val GAME_ID = 2
//    val GAME_IFID = 3
    val TAG = "DatabaseHelper"
    val DATABASE_NAME = "gargoyle.db"
    val DATABASE_VERSION = 2
  }
}
