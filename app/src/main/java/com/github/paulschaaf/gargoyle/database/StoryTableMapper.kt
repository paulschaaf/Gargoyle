/*
 * Copyright © 2018 P.G. Schaaf <paul.schaaf@gmail.com>
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

import com.github.paulschaaf.gargoyle.model.IFDBStory
import kotlin.reflect.full.createType
import kotlin.reflect.full.memberProperties

object StoryTableMapper {
  val a = Int::class.createType(nullable = true)
  val b = Int::class.java

  fun intType(): Int = 0
  private fun intTypeNullable(): Int? = 0
  fun bar() = Int::class.createType(nullable = true)

  fun convertToContentValues(story: IFDBStory) {
    IFDBStory::class.memberProperties.forEach { prop->
      if (prop.returnType == ::intType.returnType) {

      }
      else if (prop.returnType == ::intTypeNullable.returnType) {

      }
    }
  }
}