package org.anmol.desai.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Teacher extends User {
	
	@Column(name="Type")
	private String type = "Teacher";
	
	protected Teacher(){}
	
	public Teacher(String firstName,String lastName){
		super(firstName,lastName);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	

}
