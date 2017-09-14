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

  val AverageRating = DoubleColumn("averageRating")

  val Author = StringColumn("author")
  val CoverArtURL = StringColumn("coverArtURL")
  val Description = StringColumn("description")
  val FirstPublished = StringColumn("firstPublished")
  val Forgiveness = StringColumn("forgiveness")
  val Genre = StringColumn("genre")
  val IFID = StringColumn.nonNull("IFID", "UNIQUE NOT NULL")
  val Language = StringColumn("language")
  val Link = StringColumn("link")
  val LookedUp = StringColumn("lookedUp")
  val Path = StringColumn("path")
  val Series = StringColumn("series")
  val TUID = StringColumn("tUID")
  val Title = StringColumn("title")

  val _ID = IntColumn.nonNull("_id", "PRIMARY KEY")
  val RatingCountAvg = IntColumn("ratingCountAvg")
  val RatingCountTotal = IntColumn("ratingCountTotal")
  val SeriesNumber = IntColumn("seriesNumber")
  val StarRating = DoubleColumn("starRating")

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
