package com.github.paulschaaf.gargoyle

import android.content.ContentValues
import com.github.paulschaaf.gargoyle.model.Game
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
class GameTest {
  var contentValues = with(mock(ContentValues::class.java)) {
    `when`(getAsString(Game.Author.name)).thenReturn("P.G. Schaaf")
    `when`(getAsString(Game.AverageRating.name)).thenReturn("4.5")
    `when`(getAsString(Game.Description.name)).thenReturn("The long-awaited mid-quel")
    `when`(getAsString(Game.FirstPublished.name)).thenReturn("5/6/2017")
    `when`(getAsString(Game.Forgiveness.name)).thenReturn("hard")
    `when`(getAsString(Game.Genre.name)).thenReturn("Adventure")
    `when`(getAsString(Game.Collection.name)).thenReturn("Zork")
    `when`(getAsString(Game.Headline.name)).thenReturn("Your greatest adventure lies ahead, then left, and through the second door on the right.")
    `when`(getAsString(Game.IFID.name)).thenReturn("ifid_zork_pi")
    `when`(getAsLong(Game._ID.name)).thenReturn(31415)
    `when`(getAsString(Game.Language.name)).thenReturn("EN/US")
    `when`(getAsString(Game.Link.name)).thenReturn("http://paulschaaf.com/")
    `when`(getAsString(Game.LookedUp.name)).thenReturn("9/2/2017")
    `when`(getAsString(Game.Path.name)).thenReturn("/var/data/IntFic")
    `when`(getAsString(Game.Series.name)).thenReturn("Zork")
    `when`(getAsInteger(Game.SeriesNumber.name)).thenReturn(5)
    `when`(getAsInteger(Game.RatingCountAvg.name)).thenReturn(17)
    `when`(getAsInteger(Game.RatingCountTotal.name)).thenReturn(137)
    `when`(getAsInteger(Game.StarRating.name)).thenReturn(5)
    `when`(getAsString(Game.Title.name)).thenReturn("Zork 3.14")
    this
  }
  var sampleGame = Game.valueOf(contentValues)

  @Test
  fun readProperties() {
    with(contentValues) {
      assertEquals(getAsString(Game.Author.name), sampleGame.author)
      assertEquals(getAsString(Game.AverageRating.name), sampleGame.averageRating)
      assertEquals(getAsString(Game.Description.name), sampleGame.description)
      assertEquals(getAsString(Game.FirstPublished.name), sampleGame.firstPublished)
      assertEquals(getAsString(Game.Forgiveness.name), sampleGame.forgiveness)
      assertEquals(getAsString(Game.Genre.name), sampleGame.genre)
      assertEquals(getAsString(Game.Collection.name), sampleGame.collection)
      assertEquals(getAsString(Game.Headline.name), sampleGame.headline)
      assertEquals(getAsString(Game.IFID.name), sampleGame.ifId)
      assertEquals(getAsLong(Game._ID.name), sampleGame.id)
      assertEquals(getAsString(Game.Language.name), sampleGame.language)
      assertEquals(getAsString(Game.Link.name), sampleGame.link)
      assertEquals(getAsString(Game.LookedUp.name), sampleGame.lookedUp)
      assertEquals(getAsString(Game.Path.name), sampleGame.path)
      assertEquals(getAsString(Game.Series.name), sampleGame.series)
      assertEquals(getAsInteger(Game.SeriesNumber.name), sampleGame.seriesNumber)
      assertEquals(getAsInteger(Game.RatingCountAvg.name), sampleGame.ratingCountAvg)
      assertEquals(getAsInteger(Game.RatingCountTotal.name), sampleGame.ratingCountTotal)
      assertEquals(getAsInteger(Game.StarRating.name), sampleGame.starRating)
      assertEquals(getAsString(Game.Title.name), sampleGame.title)
    }
  }

  @Test
  fun updateProperties() {

  }
}