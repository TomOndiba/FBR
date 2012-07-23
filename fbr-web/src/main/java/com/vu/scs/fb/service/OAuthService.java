package com.vu.scs.fb.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vu.scs.fb.util.FbrConstants;
import com.vu.scs.fb.util.OAuthError;
import com.vu.scs.fb.util.OAuthErrorHandler;

public class OAuthService {
	private static Logger logger = LoggerFactory.getLogger(OAuthService.class);

	public String getFacebookOAuthToken(String code) throws OAuthError {
		logger.debug("trying to retrieve token with the code: " + code);
		String accessToken = "";

		String redirect_uri = FbrConstants.FBR_DASHBOARD_URI;

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(FbrConstants.FB_ACCESS_TOKEN_URI);

		try {

			String[][] parameters = { { "client_id", FbrConstants.CLIENT_APP_ID }, { "client_secret", FbrConstants.APP_SECRET },
					{ "redirect_uri", redirect_uri }, { "code", code } };

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);

			for (int i = 0; i < parameters.length; i++) {
				nameValuePairs.add(new BasicNameValuePair(parameters[i][0], parameters[i][1]));
			}

			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse resp = client.execute(post);

			logger.debug("OAuth Toten FB response received: " + resp);

			BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));

			String message = "";
			String lineData;
			while ((lineData = rd.readLine()) != null) {
				message += lineData;
			}

			logger.debug("FB response message received: " + message);

			String token = null;

			// Add more safety traps
			String[] params = message.split("&");
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					if (params[i].contains("access_token")) {
						String[] B = params[i].split("=");
						if (B != null) {
							token = B[1];
						}
						break;
					}
				}
			} else if (message != null && message.contains("error")) {
					OAuthErrorHandler.handle(message);
			}

			logger.debug("token received: " + token);

			accessToken = token;

		} catch (OAuthError e) {
			logger.error("OAuthError received while requesting for facebook OAuth acceess token", e);
			throw e;
		} catch (Exception e) {
			logger.error("Exception received while requesting for facebook OAuth acceess token", e);
		}

		return accessToken;

	}

}
