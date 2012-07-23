package com.vu.scs.fb.bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 
 * @author Villagrove Team
 * Base Class used for friends list and extended by PersonDetail for basic profile
 */
public class Person implements Comparable<Person> {
	private int rowId;
	private String firstName; // basic
	private String lastName; // basic
	private String name;
	private String homePhone;
	private String cellPhone;
	private String workPhone;
	private String emailId;

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", getName()).toString();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Person))
			return false;
		Person castOther = (Person) other;
		return new EqualsBuilder().append(this.getName(), castOther.getName()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getName()).toHashCode();
	}

	@Override
	public int compareTo(Person o) {
		if (o == null || !(o instanceof Person)) {
            return -1;
        }
        Person otherPerson = (Person) o;
        if (this.getName() == null || otherPerson.getName() == null) {
            return -1;
        }
        return this.getName().compareTo(otherPerson.getName());
	}

}
