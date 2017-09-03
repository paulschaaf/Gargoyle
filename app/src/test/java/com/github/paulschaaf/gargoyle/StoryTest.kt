package com.github.paulschaaf.gargoyle

import android.content.ContentValues
import com.github.paulschaaf.gargoyle.model.Story
import com.github.paulschaaf.gargoyle.model.Story.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.runners.MockitoJUnitRunner

/**
 * Created by pschaaf on 9/1/17.
 */
@RunWith(MockitoJUnitRunner::class)
class StoryTest {
  val properties = mapOf(
      Author to "P.G. Schaaf",
      AverageRating to "4.5",
      AverageRating to "4.5",
      Description to "The somewhat-long-awaited midquel",
      FirstPublished to "5/6/2017",
      Forgiveness to "hard",
      Genre to "Adventure",
      Collection to "Zork",
      Headline to "Your greatest adventure lies ahead! Then left, down the stairs, and through the second door on the right.",
      _ID to 31415L,
      IFID to "ifid_zork_pi",
      Language to "EN/US",
      Link to "http://paulschaaf.com/",
      LookedUp to "9/2/2017",
      Path to "/var/data/IntFic",
      RatingCountAvg to 17,
      RatingCountTotal to 137,
      Series to "Zork",
      SeriesNumber to 5,
      StarRating to 5,
      Title to "Zork 3.14"
  )

  val contentValues = mock(ContentValues::class.java)
  lateinit var story: Story

  init {
    properties.forEach { column, value ->
      when (value) {
        is Int    -> `when`(contentValues.getAsInteger(column.name)).thenReturn(value)
        is Long   -> `when`(contentValues.getAsLong(column.name)).thenReturn(value)
        is String -> `when`(contentValues.getAsString(column.name)).thenReturn(value)
      }
    }
    story = Story.valueOf(contentValues)
  }

  @Test
  fun readProperties() {
    with(story) {
      assertEquals(properties[Author], author)
      assertEquals(properties[AverageRating], averageRating)
      assertEquals(properties[Description], description)
      assertEquals(properties[FirstPublished], firstPublished)
      assertEquals(properties[Forgiveness], forgiveness)
      assertEquals(properties[Genre], genre)
      assertEquals(properties[Collection], collection)
      assertEquals(properties[Headline], headline)
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
    assertEquals(contentValues.getAsString(Story.Author.name), story.author)

    val newAuthor = "P.G. Schaaf, Jr., et. al."
    `when`(contentValues.getAsString(Story.Author.name)).thenReturn(newAuthor)
    assertEquals(newAuthor, story.author)
  }
}