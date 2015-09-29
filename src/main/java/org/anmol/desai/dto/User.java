package org.anmol.desai.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.anmol.desai.service.AnswerMapper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@XmlRootElement(name="user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User {
	
	@XmlAttribute(name="user-id")
	private Long _id;
	
	@XmlElement(name="user-firstName")
	private String firstName;
	
	@XmlElement(name="user-lastName")
	private String lastName;
	
	@XmlElement(name="user-type")
	private String type;
		
	// this gives the last answer given by the user instead of a list of answers
	//@XmlElement(name="user-lastAnswer")
	//private Answer latestAnswer;
	
	protected User(){}
	
	// this is the constructor used by the client side to send across a dto
	public User(String firstName, String lastName,String type){ //Answer lastAnswer)throws IllegalArgumentException{
		this(0L,firstName,lastName,type);
	}
	
	// this is the constructor used by the server to convert from domain to dto
	public User(Long id,String firstName, String lastName,String type){        // org.anmol.desai.domain.Answer latestAnswer){
		this._id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.type = type;
		//this.latestAnswer = AnswerMapper.toDto( latestAnswer);
	}

	public Long get_id_UserDto() {
		return _id;
	}

	public void set_id(Long _id) {
		this._id = _id;
	}

	public String getFirstNameUserDto() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastNameUserDto() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
	
	public String getTypeUserDto() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("User: { [");
		buffer.append(_id);
		buffer.append("]; ");
		
		
		if(firstName != null) {
			buffer.append(firstName);
			buffer.append(", ");
		}
		if(lastName != null) {
			buffer.append(lastName);
			buffer.append(", ");
		}
		if(type != null){
			buffer.append(type);
		}
		
		buffer.append(" }");
		
		return buffer.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof User))
            return false;
        if (obj == this)
            return true;

        User a = (User) obj;
        return new EqualsBuilder().
            append(_id, a._id).
            append(firstName, a.firstName).
            append(lastName, a.lastName).
            //append(latestAnswer, a.latestAnswer).
            isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31). 
	            append(_id).
	            append(firstName).
	            append(lastName).
	            //append(latestAnswer).
	            toHashCode();
	}

	
	
	
	
	

}


