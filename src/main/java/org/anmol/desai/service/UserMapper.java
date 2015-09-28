package org.anmol.desai.service;

import java.util.List;

import org.anmol.desai.domain.Homework;
import org.hamcrest.core.IsInstanceOf;

public class UserMapper {

	// dto has a pointless value inserted into its id because the database creates the id due to the generator.
	static org.anmol.desai.domain.User toDomainModel(org.anmol.desai.dto.User dtoUser) {
		
		// if the user type is student then map to a student object.
		// else map to a teacher object
		
		if(dtoUser.getTypeUserDto().equals("Student")){
			org.anmol.desai.domain.User fullUser = new org.anmol.desai.domain.Student(
					dtoUser.getFirstNameUserDto(),
					dtoUser.getLastNameUserDto()
					);
			return fullUser;
		}else{
			org.anmol.desai.domain.User fullUser = new org.anmol.desai.domain.Teacher(
					dtoUser.getFirstNameUserDto(),
					dtoUser.getLastNameUserDto()
					);
			return fullUser;
		}
	}

	static org.anmol.desai.dto.User toDto(org.anmol.desai.domain.User user) {
		
		String type;
		
		// check if the instance is student or teacher
		if(user instanceof org.anmol.desai.domain.Student){
			// it is a student so set the type to be "student"
			type = "Student";
		}else{
			type = "Teacher";
		}
		
		// get the latest answer. that is at the end of th answers list assuming it is "added".
		//List<org.anmol.desai.domain.Answer> l =  user.getAnswersUser();
		
		//there is a infinite loop here.
		//System.out.println(l.size() +"");
		
		
		
		org.anmol.desai.dto.User dtoUser = 
				new org.anmol.desai.dto.User(
						user.get_id(),
						user.getFirstName(),
						user.getLastName(),
						type);				
		return dtoUser;

	}

}
