package com.vu.scs.fb.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import com.vu.scs.fb.bean.PersonDetail;
import com.vu.scs.fb.util.OAuthError;
import com.vu.scs.fb.util.OAuthErrorHandler;

public class BasicProfileService {

	private static Logger logger = LoggerFactory.getLogger(BasicProfileService.class);

	public PersonDetail getUserBasicProfile(String accessToken) throws OAuthError {
		String basicInfoUrl = "https://graph.facebook.com/me";

		PersonDetail personDetail = new PersonDetail();

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

			logger.debug("bp message received: " + message);
			if (message != null && message.contains("error")) {
				OAuthErrorHandler.handle(message);
			} else if (message != null) {
				personDetail = extractPersonDetail(message);
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
		// Now do your thing with the facebook response.
		return personDetail;
	}

	private PersonDetail extractPersonDetail(String jsonText) {

		PersonDetail personDetail = new PersonDetail();
		boolean readUserName = true;
		boolean readLocation = true;
		boolean readWork = true;
		boolean readSchool = true;
		boolean readLanguage = true;

		try {

			JsonFactory jsonFactory = new JsonFactory();

			/*** read from JSON String ***/
			JsonParser jParser = jsonFactory.createJsonParser(jsonText);
			String fieldname = "";

			// loop until token equal to "}"
			while (jParser.nextToken() != null && (fieldname = jParser.getCurrentName()) != "updated_time") {

				if ("location".equals(fieldname) && readLocation) {
					logger.debug("entering to location.. ");
					while (jParser.nextToken() != JsonToken.END_OBJECT) {
						String fname = jParser.getCurrentName();
						if ("name".equals(fname)) {
							// current token is "name",
							// move to next, which is "name"'s value
							jParser.nextToken();
							String location = jParser.getText();
							personDetail.setLivesIn(location);
							logger.debug("location: " + location);

						}
					}
					readLocation = false;

				} else if ("name".equals(fieldname) && readUserName) {
					// current token is "name",
					// move to next, which is "name"'s value
					jParser.nextToken();
					String name = jParser.getText();
					personDetail.setName(name);
					logger.debug("name: " + name);

					readUserName = false;
				} else if ("birthday".equals(fieldname)) {
					jParser.nextToken();
					String birthday = jParser.getText();
					personDetail.setBirthDay(birthday);
					logger.debug("birthday: " + birthday);

				} else if ("gender".equals(fieldname)) {
					jParser.nextToken();
					String gender = jParser.getText();
					personDetail.setSex(gender);
					logger.debug("gender: " + gender);

				} else if ("relationship_status".equals(fieldname)) {
					jParser.nextToken();
					String relationship_status = jParser.getText();
					personDetail.setRelationshipStatus(relationship_status);
					logger.debug("relationship_status: " + relationship_status);

				} else if ("email".equals(fieldname)) {
					jParser.nextToken();
					String email = jParser.getText();
					personDetail.setEmailId(email);
					logger.debug("email: " + email);

				} else if ("work".equals(fieldname) && readWork) {
					jParser.nextToken(); // current token is "[", move next

					while (jParser.nextToken() != JsonToken.END_ARRAY) {
						String fname = jParser.getCurrentName();
						if ("name".equals(fname)) {
							jParser.nextToken();
							String workedAt = jParser.getText();
							logger.debug("workedAt received: " + workedAt);
							if (personDetail.getWorkedAt() != null) {
								personDetail.setWorkedAt(personDetail.getWorkedAt() + ", " + workedAt);
							} else {
								personDetail.setWorkedAt(workedAt);
							}

						}

					}
					readWork = false;
					logger.debug("workedAt: " + personDetail.getWorkedAt());

				} else if ("school".equals(fieldname) && readSchool) {
					logger.debug("entering to school.. ");
					while (jParser.nextToken() != JsonToken.END_ARRAY) {
						String fname = jParser.getCurrentName();
						if ("name".equals(fname)) {
							jParser.nextToken();
							String schoolName = jParser.getText();
							if (StringUtils.isNumeric(schoolName)) {
								continue;
							}
							logger.debug("schoolName received: " + schoolName);
							if (personDetail.getStudiedAt() != null) {
								personDetail.setStudiedAt(personDetail.getStudiedAt() + ", " + schoolName);
							} else {
								personDetail.setStudiedAt(schoolName);
							}

						}

					}
					readSchool = false;
					logger.debug("school name: " + personDetail.getStudiedAt());
				} else if ("languages".equals(fieldname) && readLanguage) {
					logger.debug("entering to languages.. ");
					while (jParser.nextToken() != JsonToken.END_ARRAY) {
						String fname = jParser.getCurrentName();
						if ("name".equals(fname)) {
							jParser.nextToken();
							String lang = jParser.getText();
							logger.debug("lang received: " + lang);
							if (personDetail.getLanguage() != null) {
								personDetail.setLanguage(personDetail.getLanguage() + ", " + lang);
							} else {
								personDetail.setLanguage(lang);
							}

						}

					}
					readLanguage = false;
					logger.debug("Language: " + personDetail.getLanguage());
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

		return personDetail;

	}

}
