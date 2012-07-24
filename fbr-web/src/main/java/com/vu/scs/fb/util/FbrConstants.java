package com.vu.scs.fb.util;

public interface FbrConstants {
	// Pal
	// String HOST_PREFIX = "http://localhost:8080";
	// String CLIENT_APP_ID = "247266912049837";
	// String APP_SECRET = "5624e24f6f98a2835da033422ba93798";
	// http://localhost:8080/fbr-web/home.jsf

	// Kodi
	String HOST_PREFIX = "http://71.225.132.109:8090";
	String CLIENT_APP_ID = "358829927522628";
	String APP_SECRET = "5d470688943d6c6c9f941cc900b5d7de";
	// http://71.225.132.109:8090/fbr-web/home.jsf

	String STATE = "fbr123fbr";

	String DOC_BASE = "/fbr-web";

	String FB_ACCESS_TOKEN_URI = "https://graph.facebook.com/oauth/access_token";
	String FB_BASIC_INFO_URI = "https://graph.facebook.com/me";
	String FB_FRIENDS_LIST_URI = "https://graph.facebook.com/me/friends";

	String FBR_DASHBOARD_URI = HOST_PREFIX + DOC_BASE + "/dashboard.jsf";
	String FBR_ERROR_URI = HOST_PREFIX + DOC_BASE + "/error.jsf";
	String FBR_ACCESS_DENIED_URI = HOST_PREFIX + DOC_BASE + "/accessDenied.jsf";

}
