package org.anmol.desai.dto;


import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAccessType;

import org.anmol.desai.dto.Homework;
import org.anmol.desai.dto.User;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@XmlRootElement(name="answer")
@XmlAccessorType(XmlAccessType.FIELD)
public class Answer {

	@XmlAttribute(name="answer-id")
	private Long _id;

	@XmlElement(name="answer-body")
	protected String body;

	@XmlElement(name="answer-user")
	protected User user;

	@XmlElement(name="homework-question")
	protected Homework hw;

	protected Answer(){}
	
	// the constructor that the client side will call to send over a dto object to the server
	// the id is put in randomly and does not matter as the database creates the,
	// this constructor calls the other constructor with the arguments given
	
	// BOTH THE USER AND HOMEWORK ARE DTOs. BECAUSE ONLY DTO ARE PASSED BY CLIENT SIDE
	
	public Answer(String body, User user, Homework hw) throws IllegalArgumentException {
		this(0L,body, user, hw);
	}

	// the constructor that is called by the web service and converts a domain model "Answer" to a dto of Answer
	public Answer(Long _id, String body, User user, Homework hw){
		this._id = _id;
		this.body = body;
		this.user = user;
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
	

	public User getUser() {
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


	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("Answer: {[");
		buffer.append(_id);
		buffer.append("]; ");
		
		
		if(body != null){
			buffer.append(body);
			buffer.append(", ");
		}
		
		if(user != null){
			buffer.append(user.getFirstNameUserDto() + " " + user.getLastNameUserDto());
			buffer.append(", ");
		}
		
		if(hw != null){
			buffer.append(hw.getTitle() + " " + hw.getQuestion());
			
		}
		buffer.append(" }");
		
		return buffer.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Answer))
            return false;
        if (obj == this)
            return true;

        Answer a = (Answer) obj;
        return new EqualsBuilder().
            append(_id, a._id).
            append(body, a.body).
            append(user, a.user).
            append(hw, a.hw).
            isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31). 
	            append(_id).
	            append(body).
	            append(user).
	            append(hw).
	            toHashCode();
	}


}
