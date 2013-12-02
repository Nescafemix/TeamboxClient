package com.teambox.client.ui.fragments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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

import com.teambox.client.Application;
import com.teambox.client.R;
import com.teambox.client.Utilities;
import com.teambox.client.db.AccountTable;
import com.teambox.client.ui.activities.MainActivity;

/**
 * Fragment which contains and manages account info
 * 
 * @author Joan Fuentes
 * 
 */
public class ProfileSummaryFragment extends BaseFragment {

	private class LoadDataInViewsAsyncTask extends
			AsyncTask<Void, Void, AccountTable> {

		@Override
		protected AccountTable doInBackground(Void... params) {

			return getInfoToShow();

		}

		private AccountTable getInfoToShow() {
			List<AccountTable> accounts = AccountTable
					.listAll(AccountTable.class);

			if (accounts.size() > 0) {
				return accounts.get(0);
			}
			return null;
		}

		@Override
		protected void onPostExecute(AccountTable account) {
			if (account != null) {
				mAccountUser = account;
				refreshInfoInViews(account);
			}
		}
	}

	private class LoadProfilePhotoAsyncTask extends
			AsyncTask<Void, Bitmap, Bitmap> {

		@Override
		protected Bitmap doInBackground(Void... params) {
			Bitmap image = null;

			if (mAccountUser.profileAvatarUrlCached == null) {

				image = downloadImageAndUpdateDB();

			} else if (mAccountUser.profileAvatarUrlCached
					.equalsIgnoreCase(mAccountUser.profileAvatarUrl)) {

				image = getCachedBitmap(image);

			} else {

				// Load the current image but continue downloading the new image
				image = getCachedBitmap(image);
				publishProgress(image);

				image = downloadImageAndUpdateDB();
			}

			return image;
		}

		private Bitmap downloadImageAndUpdateDB() {
			Bitmap image;
			image = Utilities.getBitmapFromURL(mAccountUser.profileAvatarUrl);
			if (image != null) {
				File file = saveFileInCache(image);

				updateCachedImageInDBRecord(file);
			}
			return image;
		}

		private Bitmap getCachedBitmap(Bitmap image) {
			File file = new File(getActivity().getCacheDir(),
					mAccountUser.profileAvatarUrlLocalFile);
			try {
				FileInputStream is = new FileInputStream(file);
				image = BitmapFactory.decodeStream(is);
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return image;
		}

		public File getTempFile(Context context, String name) {
			File file = null;

			try {
				file = File.createTempFile(name, null, context.getCacheDir());
			} catch (IOException e) {
				// Error while creating file
			}
			return file;
		}

		@Override
		protected void onPostExecute(Bitmap image) {
			if (image != null) {
				mImageViewProfilePhoto.setImageBitmap(image);
			}
		}

		@Override
		protected void onProgressUpdate(Bitmap... image) {
			super.onProgressUpdate(image);

			if (image != null) {
				mImageViewProfilePhoto.setImageBitmap(image[0]);
			}

		}

		private File saveFileInCache(Bitmap image) {
			File file = getTempFile(getActivity(), "profile_picture");
			try {
				FileOutputStream out = new FileOutputStream(file);
				image.compress(Bitmap.CompressFormat.PNG, 90, out);
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return file;
		}

		private void updateCachedImageInDBRecord(File file) {
			mAccountUser.profileAvatarUrlCached = mAccountUser.profileAvatarUrl;
			mAccountUser.profileAvatarUrlLocalFile = file.getName();
			mAccountUser.save();
		}
	}

	private TextView mTextViewName;
	private TextView mTextViewEmail;
	private ImageView mImageViewProfilePhoto;
	private AccountTable mAccountUser;
	private LoadProfilePhotoAsyncTask mLoadProfilePhotoAsyncTask;
	private LoadDataInViewsAsyncTask mLoadDataInViewsAsyncTask;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setupActionBar();

		refreshDataInViews();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_profile_summary,
				container, false);

		mTextViewName = (TextView) view.findViewById(R.id.textViewName);
		mTextViewEmail = (TextView) view.findViewById(R.id.textViewEmail);
		mImageViewProfilePhoto = (ImageView) view
				.findViewById(R.id.imageViewProfilePhoto);

		((Button) view.findViewById(R.id.buttonLogout))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Application.processLogout(getActivity(),
								getFragmentManager());
					}
				});

		return view;
	}

	@Override
	public void onStop() {

		if (mLoadProfilePhotoAsyncTask != null)
			mLoadProfilePhotoAsyncTask.cancel(true);

		if (mLoadDataInViewsAsyncTask != null)
			mLoadDataInViewsAsyncTask.cancel(true);

		super.onStop();
	}

	@Override
	public void refreshDataInViews() {

		mLoadDataInViewsAsyncTask = new LoadDataInViewsAsyncTask();
		mLoadDataInViewsAsyncTask.execute();
	}

	public void refreshInfoInViews(AccountTable account) {
		mTextViewName.setText(account.firstName + " " + account.lastName);
		mTextViewEmail.setText("( " + account.email + " )");

		mLoadProfilePhotoAsyncTask = new LoadProfilePhotoAsyncTask();
		mLoadProfilePhotoAsyncTask.execute();
	}

	private void setupActionBar() {
		final ActionBar actionBar = ((MainActivity) getActivity())
				.getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	}

}
