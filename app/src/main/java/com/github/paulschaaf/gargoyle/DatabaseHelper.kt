package com.github.paulschaaf.gargoyle

/**
 * Created by pschaaf on 9/8/17.
 */
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.github.paulschaaf.gargoyle.model.Story

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
  override fun onCreate(db: SQLiteDatabase) = db.execSQL(Story.Table.createSQL)

  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

  fun insertStory(story: Story): Long {
    val writableDatabase = writableDatabase
    val rowID = writableDatabase.insert(Story.Table.name, null, story.contentValues)
    if (rowID == -1L) Log.e(TAG, "The insert failed!")
    return rowID
  }

  fun rebuildDatabase() {
    val db = writableDatabase
    db.execSQL("DROP TABLE " + Story.Table.name);
//    db.delete(Story.TableName, null, null)
    db.close()
  }

//  fun deleteStory(story: Story): IntSqlColumn {
//    var success = true
//    val storyFile = story.file
//    if (storyFile != null && storyFile!!.exists()) success = success and storyFile!!.delete()
//
//    val coverFile = story.cover
//    if (coverFile != null && coverFile!!.exists()) success = success and coverFile!!.delete()
//
//    val db = writableDatabase
//    val storyId = story.id
//    return db.delete(Story.TableName, Story._ID + "=?", arrayOf(storyId))
//  }

  fun updateStory(story: Story) = writableDatabase.update(
      Story.Table.name,
      story.contentValues,
      "${Story.IntColumn._ID} = ${story.id}",
      null
  )

  // SIMPLE ACCESSORS

  val newCursor: Cursor
    get() {
      val cursor = readableDatabase.query(Story.Table.name, null, null, null, null, null,
                                          Story.StringColumn.Title.columnName + " COLLATE NOCASE")
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
