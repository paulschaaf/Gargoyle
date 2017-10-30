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

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.paulschaaf.gargoyle.model.Story
import com.github.paulschaaf.gargoyle.model.StoryListContent
import kotlinx.android.synthetic.main.activity_story_detail.*
import kotlinx.android.synthetic.main.story_detail.view.*

/**
 * A fragment representing a single Story detail screen.
 * This fragment is either contained in a [StoryListActivity]
 * in two-pane mode (on tablets) or a [StoryDetailActivity]
 * on handsets.
 */
class StoryDetailFragment: Fragment() {

  /**
   * The dummy content this fragment is presenting.
   */
  private var mStory: Story? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if (arguments.containsKey(ARG_ITEM_ID)) {
      // Load the dummy content specified by the fragment
      // arguments. In a real-world scenario, use a Loader
      // to load content from a content provider.
      mStory = StoryListContent.ITEM_MAP[arguments.getString(ARG_ITEM_ID)]
      mStory?.let {
        activity.toolbar_layout?.title = it.title
      }
    }
  }

  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    val rootView = inflater.inflate(R.layout.story_detail, container, false)

    // Show the dummy content as text in a TextView.
    mStory?.let {
      rootView.story_detail.text = it.description
    }

    return rootView
  }

  companion object {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    const val ARG_ITEM_ID = "item_id"
  }
}
