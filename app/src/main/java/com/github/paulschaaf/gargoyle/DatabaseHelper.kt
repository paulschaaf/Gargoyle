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

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.github.paulschaaf.gargoyle.database.SqlTable
import com.github.paulschaaf.gargoyle.database.StoryTable
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import org.jetbrains.anko.db.createTable
import org.jetbrains.anko.db.dropTable

class DatabaseHelper(context: Context):
    ManagedSQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
  companion object {
    val DATABASE_NAME = "gargoyle.db"
    val DATABASE_VERSION = 1
    val TABLES = listOf<SqlTable>(StoryTable)

    private var instance: DatabaseHelper? = null

    @Synchronized
    fun getInstance(ctx: Context): DatabaseHelper {
      if (instance == null) {
        instance = DatabaseHelper(ctx.applicationContext)
      }
      return instance!!
    }
  }

  override fun onCreate(db: SQLiteDatabase) {
    // Here you create tables
    TABLES.forEach {
      db.createTable(
          it.tableName,
          true,
          *it.columns.map { (_, column)-> column.createSQL }.toTypedArray()
      )
    }
  }

  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    // Here you can upgrade tables, as usual
    TABLES.forEach { db.dropTable(it.tableName, true) }
  }

//  fun insertStory(story: Story): Long {
//    val writableDatabase = writableDatabase
//    val rowID = writableDatabase.insert(StoryTable.tableName, null, story.contentValues)
//    if (rowID == -1L) Log.e(TAG, "The insert failed!")
//    return rowID
//  }
//
//  fun rebuildDatabase() {
//    val db = writableDatabase
//    db.execSQL("DROP TABLE " + StoryTable.tableName)
//    db.delete(Story.TableName, null, null)
//    db.close()
//  }

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

//  fun updateStory(story: Story) = writableDatabase.update(
//      StoryTable.tableName,
//      story.contentValues,
//      "${StoryTable.id} = ${story.id}",
//      null
//  )

  // SIMPLE ACCESSORS

//  val newCursor: Cursor
//    get() {
//      val cursor = readableDatabase.query(
//          StoryTable.tableName, null, null, null, null, null,
//          StoryTable.title.name + " COLLATE NOCASE"
//      )
//      cursor.moveToFirst()
//      return cursor
//    }

}

val Context.database: DatabaseHelper
  get() = DatabaseHelper.getInstance(applicationContext)