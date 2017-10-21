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
import org.fest.assertions.api.Assertions
import org.fest.assertions.api.Assertions.assertThat
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

fun assertThat(prop: KProperty0<Double?>) =
    assertThat(prop.invoke())
      .describedAs(prop.name)

fun assertThat(prop: KProperty0<Float?>) =
    assertThat(prop.invoke())
      .describedAs(prop.name)

fun assertThat(prop: KProperty0<Int?>) =
    assertThat(prop.invoke())
      .describedAs(prop.name)

fun assertThat(prop: KProperty0<String?>) =
    assertThat(prop.invoke())
      .describedAs(prop.name)

fun <T> assertThat(prop: KProperty1<T, *>, actual: T) =
    assertThat(prop.invoke(actual))
      .describedAs(prop.name)

class IFDBStoryAssert internal constructor(actual: IFDBStory):
    AbstractAssert<IFDBStoryAssert, IFDBStory>(actual, IFDBStoryAssert::class.java) {

  fun isDescribedBy(other: IFDBStory) {
    val failures = StringBuilder()
    IFDBStory::class.memberProperties.forEach { prop->
      val actualValue = prop(actual)
      val expectedValue = prop(other)

      if (actualValue != expectedValue) {
        failures.append('\n')
          .append(prop.name).append('\n')
          .append(".  expected: ").append(expectedValue).append('\n')
          .append(".   but was: ").append(actualValue)
      }
    }

    if (failures.isNotEmpty()) {
      failures.insert(0, "${other.title} ${other.link}")
      Assertions.fail(failures.toString())
    }
  }
}

fun assertThat(story: IFDBStory) = IFDBStoryAssert(story)