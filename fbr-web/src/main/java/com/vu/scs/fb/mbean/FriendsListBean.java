package com.vu.scs.fb.mbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vu.scs.fb.bean.Person;
import com.vu.scs.fb.bean.PersonDetail;

@ManagedBean
@RequestScoped
public class FriendsListBean implements Serializable {

	private static Logger logger = LoggerFactory.getLogger(BasicProfileBean.class);

	private static final long serialVersionUID = 1L;

	private String accessToken;

	private PersonDetail personDetail;

	private List<Person> personList;

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

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@ManagedProperty(value = "#{dashboardBean}")
	// this is EL name of your bean
	private DashboardBean dashboardBean;

	public void setDashboardBean(DashboardBean dashboardBean) {
		this.dashboardBean = dashboardBean;
	}

	@PostConstruct
	private void init() {
		personList = dashboardBean.getPersonList();
		logger.debug("personList: "+personList);
	}

}