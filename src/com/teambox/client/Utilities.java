package com.teambox.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.teambox.client.db.AccountTable;
import com.teambox.client.db.ProjectTable;
import com.teambox.client.db.TaskTable;
import com.teambox.client.oauth.OAuth;
import com.teambox.client.oauth.OAuthTeamBox;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

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


}
