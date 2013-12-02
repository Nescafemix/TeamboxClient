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

package com.teambox.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.FrameLayout;

/**
 * Class with static methods used throughout the application
 * 
 * @author Joan Fuentes
 * 
 */
public class Utilities {

	/**
	 * Download a bitmap from an url (imagePath)
	 * 
	 * @param imagePath
	 * @return Bitmap image requested
	 */
	public static Bitmap getBitmapFromURL(String imagePath) {
		Bitmap bmp = null;
		HttpURLConnection connection = null;

		try {
			URL url = new URL(imagePath);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);
			connection.setInstanceFollowRedirects(true);
			connection.connect();
			bmp = BitmapFactory.decodeStream(connection.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}
		return bmp;

	}

	/**
	 * Check if the device is a Tablet.
	 * If the framelayout "lateral_frame" exists in the loaded layout, user is
	 * using a Tablet (o maybe a padphone as device XD)
	 * 
	 * @param activity
	 * @return boolean true if is a Tablet, otherwise false
	 */
	public static boolean isDeviceATablet(Activity activity) {

		return ((FrameLayout) (activity.findViewById(R.id.lateral_frame)) != null);
	}

}
