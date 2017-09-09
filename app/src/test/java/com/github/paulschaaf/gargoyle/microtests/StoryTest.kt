package com.github.paulschaaf.gargoyle.microtests

import android.content.ContentValues
import com.github.paulschaaf.gargoyle.model.Story
import com.github.paulschaaf.gargoyle.model.Story.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.runners.MockitoJUnitRunner
import java.io.InvalidObjectException

/**
 * Created by pschaaf on 9/1/17.
 */
@RunWith(MockitoJUnitRunner::class)
class StoryTest {
  val properties = mapOf(
      Author to "P.G. Schaaf",
      AverageRating to 4.5,
      Description to "Your greatest adventure lies ahead! (Then left, down the stairs, and through the second door on the right.)",
      FirstPublished to "5/6/2017",
      Forgiveness to "hard",
      Genre to "Adventure",
      _ID to 31415,
      IFID to "ifid_zork_pi",
      Language to "EN/US",
      Link to "http://paulschaaf.com/",
      LookedUp to "9/2/2017",
      Path to "/var/data/IntFic.dat",
      RatingCountAvg to 17,
      RatingCountTotal to 137,
      Series to "Zork",
      SeriesNumber to 5,
      StarRating to 5,
      Title to "Zork 3.14"
  )

  val contentValues = mock(ContentValues::class.java)

  init {
    properties.forEach { column, value->
      when (value) {
        is Double -> `when`(contentValues.getAsDouble(column.name)).thenReturn(value)
        is Int    -> `when`(contentValues.getAsInteger(column.name)).thenReturn(value)
        is Long   -> `when`(contentValues.getAsLong(column.name)).thenReturn(value)
        is String -> `when`(contentValues.get(column.name)).thenReturn(value.toString())
        else      -> {
          throw InvalidObjectException("Test setup does not handle columns of ype " + value.javaClass.name + ".")
        }
      }
    }
  }

  var story = Story.valueOf(contentValues)

  @Test
  fun readProperties() {
    with(story) {
      assertEquals(properties[Author], author)
      assertEquals(properties[AverageRating], averageRating)
      assertEquals(properties[Description], description)
      assertEquals(properties[FirstPublished], firstPublished)
      assertEquals(properties[Forgiveness], forgiveness)
      assertEquals(properties[Genre], genre)
      assertEquals(properties[IFID], ifId)
      assertEquals(properties[_ID], id)
      assertEquals(properties[Language], language)
      assertEquals(properties[Link], link)
      assertEquals(properties[LookedUp], lookedUp)
      assertEquals(properties[Path], path)
      assertEquals(properties[Series], series)
      assertEquals(properties[SeriesNumber], seriesNumber)
      assertEquals(properties[RatingCountAvg], ratingCountAvg)
      assertEquals(properties[RatingCountTotal], ratingCountTotal)
      assertEquals(properties[StarRating], starRating)
      assertEquals(properties[Title], title)
    }
  }

  @Test
  fun updateProperties() {
    assertEquals(contentValues.get(Story.Author.name), story.author)

    val newAuthor = "P.G. Schaaf, Jr., et. al."
    `when`(contentValues.get(Story.Author.name)).thenReturn(newAuthor)
    assertEquals(newAuthor, story.author)
  }

  @Test
  fun verifyFileExistsWorks() {
    `when`(contentValues.get(Story.Path.name)).thenReturn("/no-such-file")
    assertEquals(false, story.exists)

    `when`(contentValues.get(Story.Path.name)).thenReturn("/dev/null")
    assertEquals(true, story.exists)
  }
}