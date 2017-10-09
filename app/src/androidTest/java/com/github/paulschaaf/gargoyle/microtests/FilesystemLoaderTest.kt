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

package com.github.paulschaaf.gargoyle.microtests

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FilesystemLoaderTest {

//  val rootDirName = (Environment.getExternalStorageDirectory().absolutePath // == /storage/emulated/0
//      + R.string.dir_stories)
//  val rootDir = File(rootDirName)

  @Test
  fun verifyStoryFilesFound() {
    val testContext = InstrumentationRegistry.getInstrumentation().context
    val lostPig = testContext.assets.open("LostPig.z8").reader()
    print("Story files in rootDir")
  }
}