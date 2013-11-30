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

package com.teambox.client.ui.activities;

import com.teambox.client.R;
import com.teambox.client.Utilities;
import com.teambox.client.services.UpdateDataIntentService;
import com.teambox.client.ui.fragments.BaseFragment;
import com.teambox.client.ui.fragments.BaseListFragment;
import com.teambox.client.ui.fragments.ProfileFragment;
import com.teambox.client.ui.fragments.ProjectsListFragment;
import com.teambox.client.ui.fragments.TasksListFragment;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {

    public static final String ARG_SECTION_NUMBER = "section_number";
    public static final int FRAGMENT_PROJECTS = 0;
    public static final int FRAGMENT_TASKS = 1;
    public static final int FRAGMENT_PROFILE = 2;
    public static final int FRAGMENT_ABOUT_US = 7;
	
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mFlyoutTitles;
    private BroadcastReceiver mUpdateReceiver;
    private MenuItem refreshMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        mFlyoutTitles = getResources().getStringArray(R.array.flyout_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mFlyoutTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
        
        
        // handler for received Intents for the update process 
        mUpdateReceiver = new BroadcastReceiver() {
          
			@Override
			public void onReceive(Context context, Intent intent) {
		
				switch (intent.getIntExtra(UpdateDataIntentService.EXTRA_BROADCAST_TYPE, 0)) {
					case UpdateDataIntentService.TYPE_UPLOAD_STATUS_CHANGE:
						updateStatusInActivity(intent.getIntExtra(UpdateDataIntentService.EXTRA_UPDATE_STATUS, 0));
						break;
					case UpdateDataIntentService.TYPE_ERROR_ALERT:
						alertUser(intent.getStringExtra(UpdateDataIntentService.EXTRA_ERROR_MESSAGE));
						break;
	
					default:
						break;
				}
	   		}
        };
        
        
    }
    

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        
        // Store the reference to the Refresh button to can control the animation of "uploading" in the action bar
        // the reference is used to stop the animation when the activity changes to background. 
        refreshMenuItem = menu.findItem(R.id.action_refresh);
        
        return super.onCreateOptionsMenu(menu);
    }

	
    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        return super.onPrepareOptionsMenu(menu);
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        
        // Handle action buttons
        switch(item.getItemId()) {
        case R.id.action_refresh:

        	updateProcess();
        	
            return true;
        case R.id.action_logout:
        	
        	Utilities.processLogout(getApplicationContext(),getSupportFragmentManager());
            returnToLogin();
            
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }


	private void updateProcess() {
		updateStatusInActivity(UpdateDataIntentService.STATUS_DOWNLOADING);
		startService(new Intent(this,UpdateDataIntentService.class));
	}




    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
    	Fragment fragment = null;
    	
    	switch (position) {
		case FRAGMENT_PROJECTS:
	        fragment = new ProjectsListFragment();			
			break;
		case FRAGMENT_TASKS:
	        fragment = new TasksListFragment();			
			break;
		case FRAGMENT_PROFILE:
	        fragment = new ProfileFragment();			
			break;

		default:
			break;
		}
    	
    	if(fragment!=null)
    	{
	        Bundle args = new Bundle();
	        args.putInt(ARG_SECTION_NUMBER, position);
	        fragment.setArguments(args);
	
	        FragmentManager fragmentManager = getSupportFragmentManager();
	        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    	}
        // update selected item and title, then close the drawer
        updateSelectedItemInDrawer(position);
        mDrawerLayout.closeDrawer(mDrawerList);
    }


	public void updateSelectedItemInDrawer(int position) {
		mDrawerList.setItemChecked(position, true);
        setTitle(mFlyoutTitles[position]);
	}

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	// Register Receiver to receive messages about .
    	LocalBroadcastManager.getInstance(this).registerReceiver(mUpdateReceiver,
    			new IntentFilter(UpdateDataIntentService.NOTIFICATION_UPDATE_STATUS));
    	
    }
    
    
    @Override
    protected void onPause() {
    	// Unregister since the activity is not visible
    	LocalBroadcastManager.getInstance(this).unregisterReceiver(mUpdateReceiver);

  		refreshMenuItem.setActionView(null);

    	super.onPause();
    }
    
    
	private void alertUser(String errorMessage) {
		Crouton.showText(this,errorMessage,Style.ALERT);
	}			

	
    private void updateStatusInActivity(int updateStatus) {
    	switch (updateStatus) {
		case UpdateDataIntentService.STATUS_COMPLETED:
			refreshMenuItem.setActionView(null);
			Crouton.showText(this,"UPDATE COMPLETED",Style.INFO);
			if (isDeviceATablet())
			{
				((BaseListFragment)getSupportFragmentManager().findFragmentById(R.id.content_frame)).loadDataInViews();
				((BaseFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_profile)).loadDataInViews();
			}else
			{
				switch (mDrawerList.getCheckedItemPosition()) {
				case FRAGMENT_PROFILE:
					((BaseFragment)getSupportFragmentManager().findFragmentById(R.id.content_frame)).loadDataInViews();
					break;

				default:
					((BaseListFragment)getSupportFragmentManager().findFragmentById(R.id.content_frame)).loadDataInViews();
					break;
				}
			}
			break;
		default:
			refreshMenuItem.setActionView(R.drawable.action_progressbar);
			break;
		}
    	    	
	}

    
	/**
	 * If exist the fragment "fragment_profile" in the loaded layout, user is using a Tablet as devicw
	 * 
	 * @return
	 */
	private boolean isDeviceATablet() {
		return (((BaseFragment)getSupportFragmentManager().findFragmentById(R.id.lateral_frame)) != null);
	}


	private void returnToLogin() {
		Intent intent;
		intent = new Intent(this,LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}


}
