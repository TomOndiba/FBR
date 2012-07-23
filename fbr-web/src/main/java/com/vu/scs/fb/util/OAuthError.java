package com.vu.scs.fb.util;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class OAuthError extends Exception {

	private static final long serialVersionUID = 1L;

	private String errorMessage;
	private String errorType;
	private String errorCode;
	private String errorSubcode;

	public OAuthError() {
		super();
	}
	
	public OAuthError(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorSubcode() {
		return errorSubcode;
	}

	public void setErrorSubcode(String errorSubcode) {
		this.errorSubcode = errorSubcode;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("errorMessage", getErrorMessage()).toString();
	}

}
