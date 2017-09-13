package com.github.paulschaaf.gargoyle.microtests

import android.content.ContentValues
import com.github.paulschaaf.gargoyle.database.StoryTable
import com.github.paulschaaf.gargoyle.model.Story
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.runners.MockitoJUnitRunner
import java.io.InvalidObjectException

/**
 * Created by pschaaf on 9/1/17.
 */
@RunWith(MockitoJUnitRunner::class)
class StoryTest {
  val properties = with(StoryTable) {
    mapOf(
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
  }

  val contentValues = mock(ContentValues::class.java)

  init {
    properties.forEach { column, value->
      when (value) {
        is Double -> `when`(contentValues.getAsDouble(column.name)).thenReturn(value)
        is Int    -> `when`(contentValues.getAsInteger(column.name)).thenReturn(value)
        is Long   -> `when`(contentValues.getAsLong(column.name)).thenReturn(value)
        is String -> `when`(contentValues.getAsString(column.name)).thenReturn(value.toString())
        else      -> {
          InvalidObjectException("Test setup does not handle columns of ype " + value.javaClass.name + ".")
        }
      }
    }
  }

  var story = Story.valueOf(contentValues)

  @Test
  fun readPropertiesFromStory() {
    with(StoryTable) {
      assertEquals(properties[Author], story.author)
      assertEquals(properties[AverageRating], story.averageRating)
      assertEquals(properties[Description], story.description)
      assertEquals(properties[FirstPublished], story.firstPublished)
      assertEquals(properties[Forgiveness], story.forgiveness)
      assertEquals(properties[Genre], story.genre)
      assertEquals(properties[IFID], story.ifId)
      assertEquals(properties[_ID], story.id)
      assertEquals(properties[Language], story.language)
      assertEquals(properties[Link], story.link)
      assertEquals(properties[LookedUp], story.lookedUp)
      assertEquals(properties[Path], story.path)
      assertEquals(properties[Series], story.series)
      assertEquals(properties[SeriesNumber], story.seriesNumber)
      assertEquals(properties[RatingCountAvg], story.ratingCountAvg)
      assertEquals(properties[RatingCountTotal], story.ratingCountTotal)
      assertEquals(properties[StarRating], story.starRating)
      assertEquals(properties[Title], story.title)
    }
  }

  @Test
  fun updateProperties() {
    assertEquals(properties[StoryTable.Author], story.author)

    val newAuthor = "P.G. Schaaf, Jr., et. al."
    `when`(contentValues.getAsString(StoryTable.Author.name)).thenReturn(newAuthor)
    assertEquals(newAuthor, story.author)
  }
}