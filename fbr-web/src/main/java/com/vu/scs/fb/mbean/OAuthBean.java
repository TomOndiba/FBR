package com.vu.scs.fb.mbean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@ManagedBean(name = "oauthBean")
@SessionScoped
public class OAuthBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String oauthCode;
	private String accessToken;

	public String getOauthCode() {
		return oauthCode;
	}

	public void setOauthCode(String oauthCode) {
		this.oauthCode = oauthCode;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public void invalidate() {
		setOauthCode(null);
		setAccessToken(null);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("oauthCode", getOauthCode()).append("accessToken", getAccessToken()).toString();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof OAuthBean))
			return false;
		OAuthBean castOther = (OAuthBean) other;
		return new EqualsBuilder().append(this.getOauthCode(), castOther.getOauthCode()).append(this.getAccessToken(), castOther.getAccessToken()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getOauthCode()).append(getAccessToken()).toHashCode();
	}

}
