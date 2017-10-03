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

package com.github.paulschaaf.gargoyle.model

interface IFDBStory {
  var ifId: String

  var averageRating: Double?
  var starRating: Double?

  var author: String?
  var coverArtURL: String?
  var description: String?
  var firstPublished: String?
  var forgiveness: String?
  var genre: String?
  var language: String?
  var link: String?
  var path: String?
  var series: String?
  var tuid: String?
  var title: String?

  var ratingCountAvg: Int?
  var ratingCountTotal: Int?
  var seriesNumber: Int?
}
