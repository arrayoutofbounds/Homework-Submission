package org.anmol.desai.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@XmlRootElement(name="homework")
@XmlAccessorType(XmlAccessType.FIELD)
public class Homework {

	@XmlAttribute(name="homework-id")
	private Long _id;
	
	@XmlElement(name="homework-title")
	private String title;

	@XmlElement(name="homework-question")
	private String question;

	@XmlElement(name="homework-duedate")
	private java.util.Date duedate;
	
	protected Homework(){}
	
	// this will be called by client , which sends this dto
	public Homework(String title, String question, java.util.Date duedate){
		this(0L,title,question,duedate);
	}
	
	// this will be called by the server side to convert from domain model to a dto
	public Homework(Long _id, String title, String question, java.util.Date duedate){
		this._id = _id;
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
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("Homework: {[");
		buffer.append(_id);
		buffer.append("]; ");
		
		
		if(title != null){
			buffer.append(title);
			buffer.append(", ");
		}
		
		if(question != null){
			buffer.append(question);
			buffer.append(", ");
		}
		
		if(duedate != null){
			buffer.append(duedate);
			
		}
		buffer.append(" }");
		
		return buffer.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Homework))
            return false;
        if (obj == this)
            return true;

        Homework a = (Homework) obj;
        return new EqualsBuilder().
            append(_id, a._id).
            append(title, a.title).
            append(question, a.question).
            append(duedate, a.duedate).
            isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31). 
	            append(_id).
	            append(title).
	            append(question).
	            append(duedate).
	            toHashCode();
	}



}
