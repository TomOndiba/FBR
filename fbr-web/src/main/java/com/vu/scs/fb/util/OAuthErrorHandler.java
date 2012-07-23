package com.vu.scs.fb.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonMappingException;

public class OAuthErrorHandler {
	private static Logger logger = LoggerFactory.getLogger(OAuthErrorHandler.class);

	public static void handle(String jsonErrorText) throws OAuthError {
		OAuthError oauthError = extractErrorMessage(jsonErrorText);
		throw oauthError;
	}

	private static OAuthError extractErrorMessage(String jsonText) {

		OAuthError oauthError = new OAuthError();

		try {

			JsonFactory jsonFactory = new JsonFactory();
			JsonParser jParser = jsonFactory.createJsonParser(jsonText);

			while (jParser.nextToken() != JsonToken.END_OBJECT) {

				String fieldname = jParser.getCurrentName();

				if ("message".equals(fieldname)) {
					// current token is "message",
					// move to next, which is "message"'s value
					jParser.nextToken();
					String errorMessage = jParser.getText();
					oauthError.setErrorMessage(errorMessage);
					logger.debug("errorMessage: " + errorMessage);

				} else if ("type".equals(fieldname)) {
					jParser.nextToken();
					String errorType = jParser.getText();
					oauthError.setErrorType(errorType);
					logger.debug("errorType: " + errorType);

				} else if ("code".equals(fieldname)) {
					jParser.nextToken();
					String errorCode = jParser.getText();
					oauthError.setErrorCode(errorCode);
					logger.debug("errorCode: " + errorCode);

				} else if ("error_subcode".equals(fieldname)) {
					jParser.nextToken();
					String errorSubcode = jParser.getText();
					oauthError.setErrorSubcode(errorSubcode);
					logger.debug("errorSubcode: " + errorSubcode);

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

		return oauthError;

	}

}
