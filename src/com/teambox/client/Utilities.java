package com.teambox.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.teambox.client.db.AccountTable;
import com.teambox.client.db.ProjectTable;
import com.teambox.client.db.TaskTable;
import com.teambox.client.oauth.OAuth;
import com.teambox.client.oauth.OAuthTeamBox;
import com.teambox.client.ui.activities.LoginActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.FrameLayout;

public class Utilities {
	
	public static void deleteCookies(Context context) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
	}

	public static String getAccessToken(Context context) {
		 SharedPreferences sharedPreferences = context.getSharedPreferences(Application.APP_STORE_PREF_FILE, Application.MODE_PRIVATE);
		 
		 return sharedPreferences.getString(Application.TOKEN_PREF_KEY, "0");
	}
	
	public static void processLogout(Context context, FragmentManager fragmentManager) {
		
		//1) Delete coockies to avoid login info in webView
		Utilities.deleteCookies(context);
		
		//2) Delete Token from SharedPreferences
		SharedPreferences sharespreferences = context.getSharedPreferences(Application.APP_STORE_PREF_FILE, Application.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharespreferences.edit();
		editor.remove(Application.TOKEN_PREF_KEY);
		editor.commit();
		
		//3) Delete Credential 
		OAuth oAuth = OAuthTeamBox.newInstance(context, fragmentManager);
		oAuth.deleteCredential("userId");
		
		//4) Delete BBDD
		AccountTable.deleteAll(AccountTable.class);
		ProjectTable.deleteAll(ProjectTable.class);
		TaskTable.deleteAll(TaskTable.class);
	}

	public static void returnToLoginScreen(Context context) {
		Intent intent;
		intent = new Intent(context, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	
	public static Bitmap getBitmapFromURL(String imagePath)
	{
	    Bitmap bmp = null;
	    HttpURLConnection connection = null;

	    try
	    {
	        URL url = new URL(imagePath);
	        connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.setConnectTimeout(30000);
	        connection.setReadTimeout(30000);
	        connection.setInstanceFollowRedirects(true);
	        connection.connect();
	        bmp = BitmapFactory.decodeStream(connection.getInputStream());
	    }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	    }
	    finally
	    {
	        connection.disconnect();
	    }
	    return bmp;
	    		
	}

	/**
	 * @param key
	 * @param tag
	 */
	public static void setTaskStatusFilterSelectionInSharedPreferences(Context context, int value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(Application.APP_STORE_PREF_FILE, Application.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(Application.ARG_FILTER_TASK_STATUS,value);
		editor.commit();
		
	}

	public static int getTaskStatusFilterSelectionInSharedPreferences(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(Application.APP_STORE_PREF_FILE, Application.MODE_PRIVATE);
		
		return sharedPreferences.getInt(Application.ARG_FILTER_TASK_STATUS, 1);

	}

	public static long getTaskStatusFilterValueInSharedPreferences(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(Application.APP_STORE_PREF_FILE, Application.MODE_PRIVATE);
		int positionStored = sharedPreferences.getInt(Application.ARG_FILTER_TASK_STATUS, 1);
		
		return Long.valueOf(context.getResources().getStringArray(R.array.task_status_values_array)[positionStored-1]).longValue();

	}

	/**
	 * If the framelayout "lateral_frame" exists in the loaded layout, user is using a Tablet 
	 * (o maybe a padphone as device XD)
	 * 
	 * @return
	 */
	public static boolean isDeviceATablet(Activity activity) {
	
		return ((FrameLayout)(activity.findViewById(R.id.lateral_frame)) != null);
	}


}
