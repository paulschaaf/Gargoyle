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

package com.github.paulschaaf.gargoyle

import com.github.paulschaaf.gargoyle.model.IFDBStory
import org.fest.assertions.api.AbstractAssert
import org.fest.assertions.api.Assertions.assertThat
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.full.memberProperties

fun assertThat(prop: KMutableProperty0<Double?>) =
    assertThat(prop.invoke())
      .describedAs(prop.name)

fun assertThat(prop: KMutableProperty0<Float?>) =
    assertThat(prop.invoke())
      .describedAs(prop.name)

fun assertThat(prop: KMutableProperty0<Int?>) =
    assertThat(prop.invoke())
      .describedAs(prop.name)

fun assertThat(prop: KMutableProperty0<String?>) =
    assertThat(prop.invoke())
      .describedAs(prop.name)

class IFDBStoryAssert internal constructor(actual: IFDBStory):
    AbstractAssert<IFDBStoryAssert, IFDBStory>(actual, IFDBStoryAssert::class.java) {

  fun isDescribedBy(other: IFDBStory) = IFDBStory::class.memberProperties.forEach { prop->
    assertThat(prop(other))
      .describedAs("actual value in '${prop.name}'")
      .isEqualTo(prop(other))

    print("verified property: " + prop.name)
  }
}

fun assertThat(story: IFDBStory) = IFDBStoryAssert(story)