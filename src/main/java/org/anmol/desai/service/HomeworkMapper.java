package org.anmol.desai.service;

import org.anmol.desai.domain.Homework;

public class HomeworkMapper {
	
	// domain hw takes in (string, string, date)
	
	// dto has a pointless value inserted into its id because the database creates the id due to the generator.
		static org.anmol.desai.domain.Homework toDomainModel(org.anmol.desai.dto.Homework dtoHomework) {
			org.anmol.desai.domain.Homework fullHomework = new Homework(
					dtoHomework.getTitle(),
					dtoHomework.getQuestion(),
					dtoHomework.getDuedate()
					);
			return fullHomework;
		}
		
		static org.anmol.desai.dto.Homework toDto(org.anmol.desai.domain.Homework hw) {
			org.anmol.desai.dto.Homework dtoHomework = 
					new org.anmol.desai.dto.Homework(
							hw.get_id(),
							hw.getTitle(),
							hw.getQuestion(),
							hw.getDuedate());				
			return dtoHomework;
			
		}

}
