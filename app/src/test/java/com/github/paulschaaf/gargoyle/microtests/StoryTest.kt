package com.github.paulschaaf.gargoyle.microtests

import android.content.ContentValues
import com.github.paulschaaf.gargoyle.model.Story
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.runners.MockitoJUnitRunner
import com.github.paulschaaf.gargoyle.model.Story.IntColumn.*
import com.github.paulschaaf.gargoyle.model.Story.DoubleColumn.*
import com.github.paulschaaf.gargoyle.model.Story.StringColumn.*

/**
 * Created by pschaaf on 9/1/17.
 */
@RunWith(MockitoJUnitRunner::class)
class StoryTest {

  companion object {
    val runNumber = Math.random()

    val stringValues = Story.StringColumn.values().associate({ it to "_${it.columnName}_${runNumber}" })
    val intValues = Story.IntColumn.values().associate({ it to it.columnName.hashCode() })
    val doubleValues = Story.DoubleColumn.values().associate({ it to it.columnName.hashCode().toDouble() })
  }

//      Author to "P.G. Schaaf",
//      AverageRating to 4.5,
//      Description to "Your greatest adventure lies ahead! (Then left, down the stairs, and through the second door on the right.)",
//      FirstPublished to "5/6/2017",
//      Forgiveness to "hard",
//      Genre to "Adventure",
//      _ID to 31415,
//      IFID to "ifid_zork_pi",
//      Language to "EN/US",
//      Link to "http://paulschaaf.com/",
//      LookedUp to "9/2/2017",
//      Path to "/var/data/IntFic.dat",
//      RatingCountAvg to 17,
//      RatingCountTotal to 137,
//      Series to "Zork",
//      SeriesNumber to 5,
//      StarRating to 5,
//      Title to "Zork 3.14"
//  )

  val contentValues = mock(ContentValues::class.java)

  init {
    stringValues.forEach { (col, value)->
      `when`(contentValues.getAsString(col.columnName)).thenReturn(value)
    }
    intValues.forEach { (col, value)->
      `when`(contentValues.getAsInteger(col.columnName)).thenReturn(value)
    }
    doubleValues.forEach { (col, value)->
      `when`(contentValues.getAsDouble(col.columnName)).thenReturn(value)
    }
  }

  var story = Story.valueOf(contentValues)

  @Test
  fun readPropertiesFromStory() {
    with(story) {
      assertEquals(stringValues[Author], author)
      assertEquals(doubleValues[AverageRating], averageRating)
      assertEquals(stringValues[Description], description)
      assertEquals(stringValues[FirstPublished], firstPublished)
      assertEquals(stringValues[Forgiveness], forgiveness)
      assertEquals(stringValues[Genre], genre)
      assertEquals(stringValues[IFID], ifId)
      assertEquals(intValues[_ID], id)
      assertEquals(stringValues[Language], language)
      assertEquals(stringValues[Link], link)
      assertEquals(stringValues[LookedUp], lookedUp)
      assertEquals(stringValues[Path], path)
      assertEquals(stringValues[Series], series)
      assertEquals(intValues[SeriesNumber], seriesNumber)
      assertEquals(intValues[RatingCountAvg], ratingCountAvg)
      assertEquals(intValues[RatingCountTotal], ratingCountTotal)
      assertEquals(intValues[StarRating], starRating)
      assertEquals(stringValues[Title], title)
    }
  }

  @Test
  fun updateProperties() {
    assertEquals(stringValues[Author], story.author)

    val newAuthor = "P.G. Schaaf, Jr., et. al."
    `when`(contentValues.getAsString(Author.name)).thenReturn(newAuthor)
    assertEquals(newAuthor, story.author)
  }

  @Test
  fun verifyFileExistsWorks() {
    `when`(contentValues.get(Story.StringColumn.Path.columnName)).thenReturn("/no-such-file")
    assertEquals(false, story.exists)

    `when`(contentValues.get(Story.StringColumn.Path.columnName)).thenReturn("/dev/null")
    assertEquals(true, story.exists)
  }
}