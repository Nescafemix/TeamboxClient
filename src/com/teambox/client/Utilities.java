package com.teambox.client;

import com.teambox.client.oauth.OAuth;
import com.teambox.client.oauth.OAuthTeamBox;

import android.content.Context;
import android.content.SharedPreferences;
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
		// TODO
	}


}
