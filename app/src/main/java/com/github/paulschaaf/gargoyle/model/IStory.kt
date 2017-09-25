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

import java.io.File
import java.io.RandomAccessFile

interface IStory {
  val id: Int
  val ifId: String

  val averageRating: Double?
  val starRating: Double?

  val author: String?
  val coverArtURL: String?
  val description: String?
  val firstPublished: String?
  val forgiveness: String?
  val genre: String?
  val language: String?
  val link: String?
  val lookedUp: String?
  val path: String?
  val series: String?
  val tuid: String?
  val title: String?

  val ratingCountAvg: Int?
  val ratingCountTotal: Int?
  val seriesNumber: Int?

  val exists: Boolean
    get() = file.exists()

  val file: File
    get() = File(path)

  val versionNumber: Int
    get() = RandomAccessFile(path, "r").use { it.readInt() }

  val zCodeVersion
    get() = when (versionNumber) {
      0    -> "0 (unknown)"
      70   -> "unknown (blorbed)"
      else -> versionNumber.toString()
    }
}