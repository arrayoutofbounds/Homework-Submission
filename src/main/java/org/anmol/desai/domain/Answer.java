package org.anmol.desai.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;


@Entity
public class Answer {
	
	@Id
	//@GeneratedValue(generator=DatabaseConstants.ID_GENERATOR)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long _id;
	
	@Column(nullable = false) // this prevents the answer being stored from being empty, and occupying space in db.
	protected String body;
	
	@ManyToOne(fetch = FetchType.LAZY) // this loads the answer object only and not the associated user student.
	@JoinColumn(name="USER_ID",nullable=false)
	protected User user;
	
	@OneToOne (optional = false, cascade = CascadeType.PERSIST) // answer MUST have a homework assigned. When answer is saved, so is the question.
	@JoinColumn(name="HOMEWORK_ID",nullable=false)
	protected Homework hw;
	
	protected Answer(){}
	
	public Answer(String body, User student,Homework hw){
		this.body = body;
		this.user = student;
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
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Homework getHw() {
		return hw;
	}

	public void setHw(Homework hw) {
		this.hw = hw;
	}

	public User getUser() {
		return user;
	}
	
	

	
	
}
