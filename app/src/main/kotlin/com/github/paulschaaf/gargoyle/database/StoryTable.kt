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

package com.github.paulschaaf.gargoyle.database

object StoryTable: SqlTable {
  override val name = "Story"

  val AverageRating = DoubleColumn("AverageRating")

  val Author = StringColumn("Author")
  val CoverArtURL = StringColumn("coverArtURL")
  val Description = StringColumn("description")
  val FirstPublished = StringColumn("FirstPublished")
  val Forgiveness = StringColumn("Forgiveness")
  val Genre = StringColumn("Genre")
  val IFID = StringColumn("IFID", "UNIQUE NOT NULL")
  val Language = StringColumn("Language")
  val Link = StringColumn("Link")
  val LookedUp = StringColumn("LookedUp")
  val Path = StringColumn("Path")
  val Series = StringColumn("Series")
  val TUID = StringColumn("TUID")
  val Title = StringColumn("Title")

  val _ID = IntColumn("_id", "PRIMARY KEY")
  val RatingCountAvg = IntColumn("RatingCountAvg")
  val RatingCountTotal = IntColumn("RatingCountTotal")
  val SeriesNumber = IntColumn("SeriesNumber")
  val StarRating = IntColumn("StarRating")

  override val columns: List<IColumn<*>> = listOf(
      _ID,
      Author,
      AverageRating,
      CoverArtURL,
      Description,
      FirstPublished,
      Forgiveness,
      Genre,
      IFID,
      Language,
      Link,
      LookedUp,
      Path,
      RatingCountAvg,
      RatingCountTotal,
      Series,
      SeriesNumber,
      TUID,
      Title
  )
}
