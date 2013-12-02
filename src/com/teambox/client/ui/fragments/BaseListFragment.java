/*
 * Copyright 2013 Joan Fuentes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.teambox.client.ui.fragments;

import android.support.v4.app.ListFragment;
import android.widget.BaseAdapter;

import com.teambox.client.R;
import com.teambox.client.ui.Updatable;

public abstract class BaseListFragment extends ListFragment implements
		Updatable {
	protected static final String ARG_SECTION_NUMBER = "section_number";
	protected static final String ARG_PROJECT_FILTER = "project_filter_id";

	final public void notifyDataSetChangedToAdapter() {
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				if (getListAdapter() != null) {
					BaseListFragment fragment = (BaseListFragment) getFragmentManager()
							.findFragmentById(R.id.content_frame);
					((BaseAdapter) fragment.getListAdapter())
							.notifyDataSetChanged();
				}
			}
		});
	}

}
