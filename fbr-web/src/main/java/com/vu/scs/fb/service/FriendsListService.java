package com.vu.scs.fb.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vu.scs.fb.bean.Person;
import com.vu.scs.fb.util.OAuthError;
import com.vu.scs.fb.util.OAuthErrorHandler;

public class FriendsListService {

	private static Logger logger = LoggerFactory.getLogger(FriendsListService.class);

	public List<Person> getFriendsList(String accessToken) throws OAuthError {
		String basicInfoUrl = "https://graph.facebook.com/me/friends";
		List<Person> personList = new ArrayList<Person>();

		try {
			String charset = "UTF-8";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("access_token", accessToken));
			HttpClient client = new DefaultHttpClient();

			HttpGet httpGet = new HttpGet(basicInfoUrl + "?" + URLEncodedUtils.format(params, charset));

			InputStream response = client.execute(httpGet).getEntity().getContent();

			logger.debug("bp response received: " + response);

			BufferedReader rd = new BufferedReader(new InputStreamReader(response));

			String message = "";
			String lineData;
			while ((lineData = rd.readLine()) != null) {
				message += lineData;
			}

			logger.debug("bp friends list received: " + message);

			if (message != null && message.contains("error")) {
				OAuthErrorHandler.handle(message);
			} else if (message != null) {
				personList = extractFriendsList(message);
			}

		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException received: ", e);
		} catch (IllegalStateException e) {
			logger.error("IllegalStateException received: ", e);
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException received: ", e);
		} catch (IOException e) {
			logger.error("IOException received: ", e);
		}
		return personList;
	}

	private List<Person> extractFriendsList(String jsonText) {

		List<Person> personList = new ArrayList<Person>();
		boolean readFriends = true;
		int rowId = 0;

		try {

			JsonFactory jsonFactory = new JsonFactory();

			/*** read from JSON String ***/
			JsonParser jParser = jsonFactory.createJsonParser(jsonText);
			String fieldname = "";

			while (jParser.nextToken() != null) {
				fieldname = jParser.getCurrentName();

				if ("data".equals(fieldname) && readFriends) {
					logger.debug("entering to friends list.. ");
					while (jParser.nextToken() != JsonToken.END_ARRAY) {
						String fname = jParser.getCurrentName();
						if ("name".equals(fname)) {
							// current token is "name",
							// move to next, which is "name"'s value
							jParser.nextToken();
							String name = jParser.getText();
							logger.debug("name received: " + name);
							Person person = new Person();
							// person.setRowId(++rowId);
							person.setName(name);
							personList.add(person);

						}

					}
					readFriends = false;
				}

			}
			jParser.close();

		} catch (JsonGenerationException e) {
			logger.error("JsonGenerationException received: " + e);
		} catch (JsonMappingException e) {
			logger.error("JsonMappingException received: " + e);
		} catch (IOException e) {
			logger.error("IOException received: " + e);
		}

		// sort the names alphabetically

		Collections.sort(personList);
		// add row num
		for (Person p : personList) {
			p.setRowId(++rowId);
		}

		return personList;

	}

}
