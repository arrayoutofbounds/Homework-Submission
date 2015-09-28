package org.anmol.desai.services;

import org.anmol.desai.domain.Homework;

public class HomeworkMapper {
	
	// dto has a pointless value inserted into its id because the database creates the id due to the generator.
		static Homework toDomainModel(org.anmol.desai.dto.Homework dtoHomework) {
			Homework fullHomework = new Homework(
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
