package com.vu.scs.fb.bean;

/**
 * 
 * @author Villagrove Team Used for basic profile
 * 
 */
public class PersonDetail extends Person {

	private String birthDay;
	private String sex; // basic
	private String relationshipStatus;
	private String language;
	private String workedAt;
	private String studiedAt;
	private String livesIn;

	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getRelationshipStatus() {
		return relationshipStatus;
	}

	public void setRelationshipStatus(String relationshipStatus) {
		this.relationshipStatus = relationshipStatus;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getWorkedAt() {
		return workedAt;
	}

	public void setWorkedAt(String workedAt) {
		this.workedAt = workedAt;
	}

	public String getStudiedAt() {
		return studiedAt;
	}

	public void setStudiedAt(String studiedAt) {
		this.studiedAt = studiedAt;
	}

	public String getLivesIn() {
		return livesIn;
	}

	public void setLivesIn(String livesIn) {
		this.livesIn = livesIn;
	}

}
