package org.anmol.desai.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {
	
	@Id
	@GeneratedValue(generator=DatabaseConstants.ID_GENERATOR)
	private Long _id;
	
	@Column(nullable = false)
	private String firstName;
	
	@Column(nullable = false)
	private String lastName;
	
	@OneToMany(mappedBy="user",fetch=FetchType.LAZY,cascade = CascadeType.PERSIST) // cascade means that if user is persisted then so is the answer that belongs to that user
	private List<Answer> answers = new ArrayList<Answer>();  // this has a list of answers and hence the user knows about all its answers.
	
	protected User(){}
		
	public User(String firstName, String lastName){
		this.firstName = firstName;
		this.lastName = lastName;
	}


	public void addAnswer(Answer a){
		answers.add(a);
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public Long get_id() {
		return _id;
	}

	public void set_id(Long _id) {
		this._id = _id;
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
	
	
	

}
