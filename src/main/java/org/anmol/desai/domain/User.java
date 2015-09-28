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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;




import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

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
	
	// if the user is deleted then all the answers it had are also deleted. (makes sense if someone graduates their class, you can remove them from the user db and remove their answers)
	@OneToMany(mappedBy="user",fetch=FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.REMOVE}) // cascade means that if user is persisted then so is the answer that belongs to that user
	private List<Answer> answers = new ArrayList<Answer>();  // this has a list of answers and hence the user knows about all its answers.
	
	
	// each user has a list of homeworks assigned to them
	@ManyToMany(cascade = CascadeType.PERSIST,fetch=FetchType.EAGER)
	@JoinTable(
			name="Homwork_Assigned", 
			joinColumns = @JoinColumn(name = "USER_ID"),
			inverseJoinColumns = @JoinColumn(name="HOMEWORK_ID")
	)
	private List<Homework> homeworkAssigned = new ArrayList<Homework>(); 
	
	protected User(){}
		
	public User(String firstName, String lastName){
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	@XmlTransient
	public List<Homework> getHomeworkAssigned() {
		return homeworkAssigned;
	}

	public void setHomeworkAssigned(List<Homework> homeworkAssigned) {
		this.homeworkAssigned = homeworkAssigned;
	}
	
	public void addHomework(Homework hw){
		homeworkAssigned.add(hw);
	}

	public void addAnswer(Answer a){
		answers.add(a);
	}

	public List<Answer> getAnswersUser() {
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
