package com.vu.scs.fb.mbean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vu.scs.fb.bean.Person;
import com.vu.scs.fb.bean.PersonDetail;
import com.vu.scs.fb.service.BasicProfileService;
import com.vu.scs.fb.service.FriendsListService;
import com.vu.scs.fb.service.OAuthService;
import com.vu.scs.fb.util.FbrConstants;
import com.vu.scs.fb.util.OAuthError;

@ManagedBean
@RequestScoped
public class DashboardBean implements Serializable {

	private static Logger logger = LoggerFactory.getLogger(DashboardBean.class);

	private OAuthError oauthError;

	private String accessToken;

	private static final long serialVersionUID = 1L;

	private String code;

	private String state;

	private List<Person> personList;

	private PersonDetail personDetail;

	@ManagedProperty(value = "#{oauthBean}")
	private OAuthBean oauthBean;

	public OAuthBean getOauthBean() {
		return oauthBean;
	}

	public void setOauthBean(OAuthBean oauthBean) {
		this.oauthBean = oauthBean;
	}

	public List<Person> getPersonList() {
		return personList;
	}

	public void setPersonList(List<Person> personList) {
		this.personList = personList;
	}

	public PersonDetail getPersonDetail() {
		return personDetail;
	}

	public void setPersonDetail(PersonDetail personDetail) {
		this.personDetail = personDetail;
	}

	public String getCode() {
		return code;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public OAuthError getOauthError() {
		return oauthError;
	}

	public void setOauthError(OAuthError oauthError) {
		this.oauthError = oauthError;
	}

	@PostConstruct
	public void init() {

		logger.debug("entering init..");

		// for debugging purpose
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Set<Entry<String, Object>> s = sessionMap.entrySet();
		for (Map.Entry<String, Object> entry : sessionMap.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			logger.debug("session key: " + key + ", value: " + value);
		}
		// end debugging

		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

		String error_reason = req.getParameter("error_reason");
		if (error_reason != null) {
			try {
				// you may want to pass this error to UI
				String error_redirect_uri = "http://localhost:8080/fbr-web/accessDenied.jsf";
				String error_desc = req.getParameter("error_description");

				logger.debug("User denied access to the FBR app, error_reason: " + error_reason + ", error_desc: " + error_desc);
				logger.debug("redirecting to " + error_redirect_uri);
				// ((HttpServletResponse)
				// FacesContext.getCurrentInstance().getExternalContext().getResponse()).sendRedirect(error_redirect_uri);
				errorRedirect(error_redirect_uri);
			} catch (Exception e) {
				logger.error("Exception while redirecting to FBR access denied error page", e);
			}
		}

		String newCode = req.getParameter("code");

		logger.debug("newCode received: " + newCode);
		
		

		if (newCode != null && StringUtils.isEmpty(oauthBean.getOauthCode())) {
//			OAuthBean oauthBean = new OAuthBean();
			// int ret = retrieveToken(newCode);
			OAuthService oauthService = new OAuthService();
			try {
				accessToken = oauthService.getFacebookOAuthToken(newCode);
				oauthBean.setAccessToken(accessToken);
//				sessionMap.put("oauthBean", OAuthBean);
			} catch (OAuthError e) {
				this.setOauthError(e);
				logger.error("OAuthError received: " + e.getErrorMessage());
				errorRedirect(FbrConstants.FBR_ERROR_URI);
			}
			oauthBean.setOauthCode(newCode);
			this.code = newCode;
		} else {
			this.code = oauthBean.getOauthCode();
			accessToken = oauthBean.getAccessToken();
		}

	}

	private void errorRedirect(String error_redirect_uri) {
		try {
			logger.debug("redirecting to " + error_redirect_uri);
			oauthBean.invalidate();
			((HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse()).sendRedirect(error_redirect_uri);
		} catch (Exception e) {
			logger.error("Exception while redirecting to FBR error page", e);
		}
	}

	public String userBasicProfile() {
		code = oauthBean.getOauthCode();
		accessToken = oauthBean.getAccessToken();
		
		logger.debug("entering  userBasicProfile... with code " + code);
		logger.debug("entering  userBasicProfile... with accessToken " + accessToken);

		if (StringUtils.isEmpty(code) || StringUtils.isEmpty(accessToken)) {
			return "home";
		}
		BasicProfileService basicProfileService = new BasicProfileService();
		try {
			personDetail = basicProfileService.getUserBasicProfile(accessToken);
		} catch (OAuthError e) {
			this.setOauthError(e);
			logger.error("OAuthError received: " + e.getErrorMessage());
			oauthBean.invalidate();
			return "error";
		}

		logger.debug("end of userBasicProfile...");
		return "basicProfile";

	}

	public String friendsList() {
		code = oauthBean.getOauthCode();
		accessToken = oauthBean.getAccessToken();
		
		logger.debug("entering  userBasicProfile... with code " + code);
		logger.debug("entering  friendsList... with accessToken " + accessToken);

		if (StringUtils.isEmpty(code) || StringUtils.isEmpty(accessToken)) {
			return "home";
		}

		FriendsListService friendsListService = new FriendsListService();
		try {
			personList = friendsListService.getFriendsList(accessToken);
		} catch (OAuthError e) {
			this.setOauthError(e);
			logger.error("OAuthError received: " + e.getErrorMessage());
			oauthBean.invalidate();
			return "error";
		}
		logger.debug("end of friendsList.");
		return "friendsList";

	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}