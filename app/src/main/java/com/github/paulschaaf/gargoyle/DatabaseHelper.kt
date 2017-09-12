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

//  fun deleteStory(story: Story): IntColumn {
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

  /***
   * Experimental
   */

  interface Column<T> {
    val name: kotlin.String
      get() = this.javaClass.simpleName

    operator fun plus(other: Any?) = name + other?.toString()

    val createSQL: kotlin.String

    operator fun get(story: Story): T = get(story.contentValues)
    operator fun set(story: Story, value: T) = set(story.contentValues, value)

    operator fun get(conValues: ContentValues): T
    operator fun set(conValues: ContentValues, value: T)

    interface Double: Column<kotlin.Double?> {
      override operator fun get(conValues: ContentValues) = conValues.getAsDouble(name)
      override operator fun set(conValues: ContentValues, value: kotlin.Double?) = conValues.put(name, value)
      override val createSQL: kotlin.String
        get() = "${name} DOUBLE"
    }

    interface Int: Column<kotlin.Int?> {
      override operator fun get(conValues: ContentValues) = conValues.getAsInteger(name)
      override operator fun set(conValues: ContentValues, value: kotlin.Int?) = conValues.put(name, value)
      override val createSQL: kotlin.String
        get() = "${name} INTEGER"
    }

    interface String: Column<kotlin.String?> {
      override operator fun get(conValues: ContentValues) = conValues.get(name)?.toString()
      override operator fun set(conValues: ContentValues, value: kotlin.String?) = conValues.put(name, value?.trim())
      override val createSQL: kotlin.String
        get() = "${name} TEXT"
    }
  }
  enum class StoryColumn() : Column {
    Author {
      override fun <T, U: Column.String> accessor(): U {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
      }
    },
    AverageRating;

    abstract fun <T, U: Column<*>> accessor(): U
  }
}
