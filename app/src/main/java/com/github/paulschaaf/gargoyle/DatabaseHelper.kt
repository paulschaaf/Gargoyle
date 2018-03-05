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

package com.github.paulschaaf.gargoyle

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.github.paulschaaf.gargoyle.database.SqlTable
import com.github.paulschaaf.gargoyle.database.StoryFileLoader
import com.github.paulschaaf.gargoyle.database.StoryTable
import com.github.paulschaaf.gargoyle.ifdb.IFDBService
import com.github.paulschaaf.gargoyle.model.Story
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import org.jetbrains.anko.db.MapRowParser
import org.jetbrains.anko.db.RowParser
import org.jetbrains.anko.db.createTable
import org.jetbrains.anko.db.dropTable
import org.jetbrains.anko.db.select


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

  override fun onCreate(db: SQLiteDatabase) = createTables(db)

  private fun createTables(db: SQLiteDatabase) {
    TABLES.forEach {
      db.createTable(
          it.tableName,
          true,
          *it.columns.map { column-> column.createSQL }.toTypedArray()
      )
    }
  }

  fun populateTables() {
    // 1. Scan the disk to create a Map<IFID, File> of the files
    val unregisteredFiles = StoryFileLoader.readStoryFiles().toMutableMap()

    // 2. Read the DB to create a Map<IFID, PK> of the database records
    val storyRows = readableDatabase
      .select(StoryTable.tableName, StoryTable.ifId.name, StoryTable.id.name)
      .parseList(stringPairParser)

    storyRows.forEach { (ifId, publicKey)->
      if (unregisteredFiles.containsKey(ifId)) unregisteredFiles.remove(ifId)
      else deleteStory(publicKey)  // 3. Delete any db records that have no corresponding filename
    }

    // 4. Process the new unregistered files by looking them up in IFDB and then INSERTing a new row.
    unregisteredFiles.forEach { (ifId, file)->
      val story = IFDBService(ifId).lookup()
//      story.file = file
      insertStory(story)
    }
  }

  private val stringPairParser = object: RowParser<Pair<String, String>> {
    override fun parseRow(columns: Array<Any?>): Pair<String, String> =
        columns[0].toString() to columns[1].toString()
  }

  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    TABLES.forEach { db.dropTable(it.tableName, true) }
  }

  fun insertStory(story: Story): Long {
    val rowID = writableDatabase.use {
      it.insert(StoryTable.tableName,
                null,
                story.toContentValues()
      )
    }
//    if (rowID == -1L) Log.e(TAG, "The insert failed!")
    return rowID
  }

  fun rebuildDatabase() = writableDatabase.use { it.dropTable(StoryTable.tableName, true) }

  fun deleteStory(vararg storyID: String): Boolean {
    return writableDatabase.use {
      it.delete(StoryTable.tableName, StoryTable.id.name + "=?", storyID)
      true
    }
    // todo pschaaf 01/24/18 15:01: remove cover file
    // todo pschaaf 01/21/18 21:01 then notify the user
  }

  fun deleteStory(story: Story): Boolean = deleteStory(story.id)

  fun updateStory(story: Story) = writableDatabase.update(
      StoryTable.tableName,
      story.toContentValues(),
      "${StoryTable.id} = ${story.id}",
      null
  )

  // SIMPLE ACCESSORS

  val newCursor: Cursor
    get() {
      return readableDatabase.query(
          /* table         */ StoryTable.tableName,
          /* columns       */ null,
          /* selection     */ null,
          /* selectionArgs */ null,
          /* groupBy       */ null,
          /* having        */ null,
          /* orderBy       */ StoryTable.title.name + " COLLATE NOCASE"
      )
    }

  val stories: Collection<Story>
    get() {
      val stories = ArrayList<Story>()
      newCursor.use { cursor->
        if (cursor.moveToFirst()) {
          do {
//            val newStory = Story()
//            DatabaseUtils.cursorRowToContentValues(cursor, newStory.toContentValues())
//            stories.add(newStory)
          }
          while (cursor.moveToNext())
        }
        return stories
      }
    }
}

val Context.database: DatabaseHelper
  get() = DatabaseHelper.getInstance(applicationContext)

class StoryParser: MapRowParser<Story> {
  override fun parseRow(columns: Map<String, Any?>): Story {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}