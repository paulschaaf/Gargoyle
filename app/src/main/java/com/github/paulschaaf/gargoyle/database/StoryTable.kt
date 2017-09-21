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

  val id = PrimaryKeyColumn("_id")
  val ifid = StringColumn.nonNull("ifid", "UNIQUE NOT NULL")

  val author by StringColumn
  val coverArtURL by StringColumn
  val description by StringColumn
  val firstPublished by StringColumn
  val forgiveness by StringColumn
  val genre by StringColumn
  val language by StringColumn
  val link by StringColumn
  val lookedUp by StringColumn
  val path by StringColumn
  val series by StringColumn
  val tuid by StringColumn
  val title by StringColumn
  val averageRating by DoubleColumn
  val ratingCountAvg by IntColumn
  val ratingCountTotal by IntColumn
  val seriesNumber by IntColumn
  val starRating by DoubleColumn

//  val author = StringColumn("author")
//  val coverArtURL = StringColumn("coverArtURL")
//  val description = StringColumn("description")
//  val firstPublished = StringColumn("firstPublished")
//  val forgiveness = StringColumn("forgiveness")
//  val genre = StringColumn("genre")
//  val language = StringColumn("language")
//  val link = StringColumn("link")
//  val lookedUp = StringColumn("lookedUp")
//  val path = StringColumn("path")
//  val series = StringColumn("series")
//  val tuid = StringColumn("tuid")
//  val title = StringColumn("title")
//
//  val averageRating = DoubleColumn("averageRating")
//  val ratingCountAvg = IntColumn("ratingCountAvg")
//  val ratingCountTotal = IntColumn("ratingCountTotal")
//  val seriesNumber = IntColumn("seriesNumber")
//  val starRating = DoubleColumn("starRating")

  override val columns: List<IColumn<*>> = listOf(
      id,
      author,
      averageRating,
      coverArtURL,
      description,
      firstPublished,
      forgiveness,
      genre,
      ifid,
      language,
      link,
      lookedUp,
      path,
      ratingCountAvg,
      ratingCountTotal,
      series,
      seriesNumber,
      tuid,
      title
  )
}
