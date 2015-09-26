package org.anmol.desai.services;

import org.anmol.desai.domain.Answer;

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
	
	static org.anmol.desai.dto.Answer toDto(Answer answer) {
		org.anmol.desai.dto.Answer dtoAnswer = 
				new org.anmol.desai.dto.Answer(
						answer.get_id(),
						answer.getBody(),
						answer.getUser(),
						answer.getHw());				
		return dtoAnswer;
		
	}

}
