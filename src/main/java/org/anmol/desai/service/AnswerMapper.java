package org.anmol.desai.service;

import org.anmol.desai.dto.Answer;
import org.anmol.desai.dto.Homework;
import org.anmol.desai.dto.User;

public class AnswerMapper {
	
	// dto has a pointless value inserted into its id because the database creates the id due to the generator.
	static Answer toDomainModel(org.anmol.desai.dto.Answer dtoAnswer) {
		Answer fullAnswer = new Answer(
				dtoAnswer.getBody(),
				dtoAnswer.getUser(),
				dtoAnswer.getHw()
				);
		return fullAnswer;
	}
	
	// this goes to the dto Answer and uses the second constructor to make a dto from a domain model. So 
	// map accordingly
	public static org.anmol.desai.dto.Answer toDto(org.anmol.desai.domain.Answer answer) {
		org.anmol.desai.dto.Answer dtoAnswer = 
				new org.anmol.desai.dto.Answer(
						answer.get_id(),
						answer.getBody(),
						UserMapper.toDto(answer.getUser()),
						HomeworkMapper.toDto(answer.getHw()));				
		return dtoAnswer;
		
	}

}
