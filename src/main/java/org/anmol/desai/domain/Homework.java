package org.anmol.desai.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Homework {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long _id;
	
	private String title;
	
	private String question;
	
	@Temporal(TemporalType.DATE)
	private java.util.Date duedate;
	
	protected Homework(){}
	
	
	public Homework(String title, String question, java.util.Date duedate){
		this.title = title;
		this.question = question;
		this.duedate = duedate;
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
