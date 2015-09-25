package org.anmol.desai.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;


@Entity
public class Answer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long _id;
	
	protected String body;
	
	@ManyToOne
	protected User student;
	
	@OneToOne
	protected Homework hw;
	
	protected Answer(){}
	
	public Answer(String body, User student,Homework hw){
		this.body = body;
		this.student = student;
		this.hw = hw;
	}

	public Long get_id() {
		return _id;
	}

	public void set_id(Long _id) {
		this._id = _id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public User getStudent() {
		return student;
	}

	public void setUser(User student) {
		this.student = student;
	}

	public Homework getHw() {
		return hw;
	}

	public void setHw(Homework hw) {
		this.hw = hw;
	}
	
	

	
	
}
