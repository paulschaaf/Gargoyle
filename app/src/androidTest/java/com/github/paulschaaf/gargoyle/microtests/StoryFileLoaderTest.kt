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

import android.os.Environment
import android.support.test.runner.AndroidJUnit4
import org.andglk.babel.Babel
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class StoryFileLoaderTest {
  val storyDirName = (Environment.getExternalStorageDirectory().absolutePath // == /storage/emulated/0
      + File.separator + "Interactive Fiction")
//  val rootDir = File(storyDirName)

//  @Test
//  fun verifyStoryFileFoundInAssets() {
//    val testContext = InstrumentationRegistry.getInstrumentation().context
//    val assets = testContext.assets
//    val lostPig = assets.open("LostPig.z8").reader()
//    assertThat(lostPig).isNotNull
//  }

//  @Test
//  fun verifyStoryFileFoundThroughClassloader() {
//    val lostPig = javaClass.classLoader.getResourceAsStream("LostPig.z8").readBytes().toString()
//    val result = Babel.examine(lostPig)
//    assertThat(lostPig).isNotNull
//  }

  @Test
  fun verifyStoryFilesFoundByName() {
    val enchanterSpec = storyDirName + File.separator + "Enchanter.dat"
    val result = Babel.examine(enchanterSpec)
    // todo pschaaf 10/291/17 16:10: make this do something
//    assertThat(result).isNotEmpty
  }
}