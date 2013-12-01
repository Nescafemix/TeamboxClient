package com.teambox.client.ui.fragments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.teambox.client.Application;
import com.teambox.client.R;
import com.teambox.client.Utilities;
import com.teambox.client.db.AccountTable;
import com.teambox.client.ui.activities.MainActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileSummaryFragment extends BaseFragment{

	TextView textViewName;
	TextView textViewEmail;
	ImageView imageViewProfilePhoto;
	AccountTable accountUser;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_profile_summary, container, false);
		
		textViewName = (TextView) view.findViewById(R.id.textViewName);
		textViewEmail = (TextView) view.findViewById(R.id.textViewEmail);
		imageViewProfilePhoto = (ImageView) view.findViewById(R.id.imageViewProfilePhoto);
		
		((Button) view.findViewById(R.id.buttonLogout)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Application.processLogout(getActivity(), getFragmentManager());
			}
		});
		
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	    setupActionBar();
		
		refreshDataInViews();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public void onDetach() {
		super.onDetach();

	}

	@Override
	public void refreshDataInViews() {

		new LoadDataInViewsAsyncTask().execute();
		
	}

	private void setupActionBar() {
		final ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
	    actionBar.setDisplayShowTitleEnabled(true);
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	}

	
	private class LoadDataInViewsAsyncTask extends AsyncTask<Void, Void, AccountTable>{

		@Override
		protected AccountTable doInBackground(Void... params) {
	
			return getInfoToShow();
			
		}
		
		@Override
		protected void onPostExecute(AccountTable account) {
		    if(account != null)
		    {
		    	accountUser = account;
		    	refreshInfoInViews(account);
		    }
		}		 

		private AccountTable getInfoToShow(){
			List<AccountTable> accounts = AccountTable.listAll(AccountTable.class);

			if(accounts.size()>0)
			{
				return accounts.get(0);
			}
			return null;
		}
	}

	/**
	 * @param account
	 */
	public void refreshInfoInViews(AccountTable account) {
		textViewName.setText(account.firstName + " " + account.lastName);
		textViewEmail.setText("( "+ account.email + " )");
		new LoadProfilePhotoAsyncTask().execute();
	}


	private class LoadProfilePhotoAsyncTask extends AsyncTask<Void, Void, Bitmap>{

		@Override
		protected Bitmap doInBackground(Void... params) {
			Bitmap image = null;
			
			if(accountUser.profileAvatarUrlCached == null)
			{
				// Download and save the file
				image = Utilities.getBitmapFromURL(accountUser.profileAvatarUrl);
				if (image != null)
				{
					File file = getTempFile(getActivity(), "profile_picture");
					try {
					       FileOutputStream out = new FileOutputStream(file);
					       image.compress(Bitmap.CompressFormat.PNG, 90, out);
					       out.flush();
					       out.close();
					} catch (Exception e) {
					       e.printStackTrace();
					}

					// Save image in local storage & update image saved at register in DB
					accountUser.profileAvatarUrlCached = accountUser.profileAvatarUrl;
					accountUser.profileAvatarUrlLocalFile = file.getName();
					accountUser.save();
				}
			}
			else if(accountUser.profileAvatarUrlCached.equalsIgnoreCase(accountUser.profileAvatarUrl)){
				// Load the cached file
				File file = new File(getActivity().getCacheDir(),accountUser.profileAvatarUrlLocalFile);
				try {
					   FileInputStream is = new FileInputStream(file);
					   image = BitmapFactory.decodeStream(is);
				       is.close();
				} catch (Exception e) {
				       e.printStackTrace();
				}
				
			
			}else{
				// Download and save the file
				image = Utilities.getBitmapFromURL(accountUser.profileAvatarUrl);
				if (image != null)
				{
					File file = getTempFile(getActivity(), "profile_picture");
					try {
					       FileOutputStream out = new FileOutputStream(file);
					       image.compress(Bitmap.CompressFormat.PNG, 90, out);
					       out.flush();
					       out.close();
					} catch (Exception e) {
					       e.printStackTrace();
					}
					
					// Save image in local storage & update image saved at register in DB
					accountUser.profileAvatarUrlCached = accountUser.profileAvatarUrl;
					accountUser.profileAvatarUrlLocalFile = file.getName();
					accountUser.save();
				}
			}
			if(image == null)
			{
			       image = Utilities.getBitmapFromURL(accountUser.profileAvatarUrl);
			}
			
			
	
			return image;
		}
		
		@Override
		protected void onPostExecute(Bitmap image) {
		   if(image != null)
		   {
			   imageViewProfilePhoto.setImageBitmap(image);
		   }
		}		 

		
		public File getTempFile(Context context, String name) {
		    File file = null;

		    try {
		        file = File.createTempFile(name, null, context.getCacheDir());
		    }
		    catch (IOException e) {
		        // Error while creating file
		    }
		    return file;
		}
	}

	
}
