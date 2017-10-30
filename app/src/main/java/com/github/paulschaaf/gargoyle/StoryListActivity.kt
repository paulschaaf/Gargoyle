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

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.paulschaaf.gargoyle.model.Story
import com.github.paulschaaf.gargoyle.model.StoryListContent
import kotlinx.android.synthetic.main.activity_story_list.*
import kotlinx.android.synthetic.main.story_list.*
import kotlinx.android.synthetic.main.story_list_content.view.*

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [StoryDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class StoryListActivity: AppCompatActivity() {

  /**
   * Whether or not the activity is in two-pane mode, i.e. running on a tablet
   * device.
   */
  private var mTwoPane: Boolean = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_story_list)

    setSupportActionBar(toolbar)
    toolbar.title = title

    fab.setOnClickListener { view->
      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        .setAction("Action", null).show()
    }

    if (story_detail_container != null) {
      // The detail container view will be present only in the
      // large-screen layouts (res/values-w900dp).
      // If this view is present, then the
      // activity should be in two-pane mode.
      mTwoPane = true
    }

    setupRecyclerView(story_list)
  }

  private fun setupRecyclerView(recyclerView: RecyclerView) {
    StoryListContent.setup(applicationContext)
    recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, StoryListContent.STORIES, mTwoPane)
  }

  class SimpleItemRecyclerViewAdapter(
      private val mParentActivity: StoryListActivity,
      private val mStories: List<Story>,
      private val mTwoPane: Boolean
  ):
      RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
      mOnClickListener = View.OnClickListener { v->
        val item = v.tag as Story
        if (mTwoPane) {
          val fragment = StoryDetailFragment().apply {
            arguments = Bundle()
            arguments.putString(StoryDetailFragment.ARG_ITEM_ID, item.id)
          }
          mParentActivity.supportFragmentManager
            .beginTransaction()
            .replace(R.id.story_detail_container, fragment)
            .commit()
        }
        else {
          val intent = Intent(v.context, StoryDetailActivity::class.java).apply {
            putExtra(StoryDetailFragment.ARG_ITEM_ID, item.id)
          }
          v.context.startActivity(intent)
        }
      }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.story_list_content, parent, false)
      return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val story = mStories[position]
      holder.mIdView.text = story.id
      holder.mContentView.text = story.title

      with(holder.itemView) {
        tag = story
        setOnClickListener(mOnClickListener)
      }
    }

    override fun getItemCount(): Int = mStories.size

    inner class ViewHolder(mView: View): RecyclerView.ViewHolder(mView) {
      val mIdView: TextView = mView.id_text
      val mContentView: TextView = mView.content
    }
  }
}
