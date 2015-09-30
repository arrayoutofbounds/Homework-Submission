package org.anmol.desai.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;
import org.hibernate.annotations.Cascade;

@Entity
public class Homework {
	
	@Id
	@GeneratedValue(generator=DatabaseConstants.ID_GENERATOR)
	private Long _id;
	
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private String question;
	
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date duedate;
	
	 // each homework has a list of users it is assigned to it. If homework is persisted, then it may not be that the user is also persisted/
	@ManyToMany(mappedBy = "homeworkAssigned",fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
	//@XmlTransient
	private List<User> usersAssigned = new ArrayList<User>();
	

	protected Homework(){}
	
	
	public Homework(String title, String question, java.util.Date duedate){
		this.title = title;
		this.question = question;
		this.duedate = duedate;
	}
	


	public void addUser(User user){
		usersAssigned.add(user); 
	}
	
	//@XmlTransient
	public List<User> getUsersAssigned() {
		return usersAssigned;
	}
	
	

	public void setUsersAssigned(List<User> usersAssigned) {
		this.usersAssigned = usersAssigned;
	}
	
	public Long get_id() {
		return _id;
	}


	public void set_id(Long _id) {
		this._id = _id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getQuestion() {
		return question;
	}


	public void setQuestion(String question) {
		this.question = question;
	}


	public java.util.Date getDuedate() {
		return duedate;
	}


	public void setDuedate(java.util.Date duedate) {
		this.duedate = duedate;
	}
	
	
}
